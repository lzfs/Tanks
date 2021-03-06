package pp.tanks.server.auto;

import pp.network.IConnection;
import pp.tanks.message.client.MoveMessage;
import pp.tanks.message.client.ShootMessage;
import pp.tanks.message.client.TurretUpdateMessage;
import pp.tanks.message.data.Data;
import pp.tanks.message.data.DataTimeItem;
import pp.tanks.message.data.ProjectileData;
import pp.tanks.message.data.TankData;
import pp.tanks.message.server.IServerMessage;
import pp.tanks.model.ICollisionObserver;
import pp.tanks.model.Model;
import pp.tanks.model.item.BreakableBlock;
import pp.tanks.model.item.PlayerEnum;
import pp.tanks.model.item.Projectile;
import pp.tanks.model.item.Tank;
import pp.tanks.server.GameMode;
import pp.tanks.server.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * The State representing a running singleplayer or multiplayer game.
 * Data form clients is received here and put into the buffer, which is periodically
 * emptied and the containing messages are then chronologically processes by the server
 */
public class GameRunningState extends TankState implements ICollisionObserver {
    private final Model model;
    private final PlayingState parent;
    private final Queue<DataTimeItem<? extends Data>> buffer = new PriorityBlockingQueue<>();
    private final Queue<TurretUpdateMessage> turretUpdates = new ArrayBlockingQueue<>(5);
    private final GameMode gameMode;
    private final List<DataTimeItem<TankData>> tankDat = new ArrayList<>();
    private final List<DataTimeItem<ProjectileData>> projectileDat = new ArrayList<>();
    private DataTimeItem<? extends Data>[] working;
    private Timer timer;
    private boolean gameEnded = false;
    private final Thread workInputsThread = new Thread(this::workBuff);

    /**
     * Constructor of the GameRunningState
     *
     * @param parent the parent of this state, in this case, the PlayingState
     */
    public GameRunningState(PlayingState parent, Model model, GameMode mode) {
        this.model = model;
        this.parent = parent;
        this.gameMode = mode;
    }

    /**
     * method used to process the messages that were contained in the buffer list, then sends the results to the Players.
     */
    public void workBuff() {
        if (gameEnded) return;
        DataTimeItem<? extends Data>[] tmp = working;
        working = null;
        long timeEnd = System.nanoTime();
        long timeStart = model.getLatestUpdate();
        long step = (timeEnd - timeStart) / 5;

        List<DataTimeItem<? extends Data>> dat = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (tmp.length != 0) {
                for (DataTimeItem<? extends Data> item : tmp) {
                    if (item.serverTime < timeStart + step * (i + 1) && (i == 0 || item.serverTime > timeStart + step * i))
                        dat.add(item);
                }
            }
            makeDatLists(dat);
            processProjectiles(new ArrayList<>(projectileDat));
            processTanks(new ArrayList<>(tankDat));
            processTurretUpdates();
            projectileDat.clear();
            tankDat.clear();
            dat.clear();
            model.update(timeStart + step * (i + 1));
            boolean isGameEnd = isGameEnd();
            for (Player p : parent.getPlayers()) {
                if (isGameEnd) p.sendEndingMessage(gameMode);
                else {
                    p.sendMessages();
                    p.reset();
                }
            }
            if (isGameEnd) {
                gameEnded = true;
                timer.cancel();
                parent.containingState().goToState(parent.containingState().playerReady);
                parent.gameFinished();
            }
        }
        model.setLatestUpdate(timeStart + 5 * step);

        for (Player p : parent.getPlayers()) {
            p.sendMessages();
            p.reset();
        }
    }

    /**
     * Method called upon entering the State. Sets up the workingThread and configures the timer used to sequence
     * the inputs and the player-update-frequency
     */
    @Override
    public void entry() {
        parent.getLogger().info("Game running State");
        timer = new Timer();
        model.setLatestUpdate(System.nanoTime());
        model.getTanksMap().addObserver(this);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                working = buffer.toArray(new DataTimeItem[buffer.size()]);
                buffer.clear();
                workInputsThread.run();
            }
        }, 100, 100);
    }

    /**
     * Override method mandatory to use methods of StateSupport
     *
     * @return the parent, in this case PlayingState
     */
    @Override
    public TankState containingState() {
        return parent;
    }

    /**
     * The function called when receiving a MoveMessage.
     * Adding the message to the Buffer
     *
     * @param msg  hte Message processed
     * @param conn the connection from which the message was send
     */
    @Override
    public void tankMove(MoveMessage msg, IConnection<IServerMessage> conn) {
        buffer.add(msg.dataTime);
    }

    /**
     * The function called when receiving a ShootMessage.
     * Adding the Message to the buffer
     *
     * @param msg hte Message processed
     */
    @Override
    public void shoot(ShootMessage msg) {
        buffer.add(msg.dataTime);
    }

    /**
     * Use a List of Tanks to give it to the players and updates the server map
     *
     * @param tmp the Tanks to process
     */
    private void processTanks(List<DataTimeItem<TankData>> tmp) {
        if (tmp.size() != 0) {
            for (DataTimeItem<TankData> d : tmp) {
                int id = d.data.getId();
                model.getTanksMap().getTank(PlayerEnum.getPlayer(id)).interpolateData(d);
                if (id == 0) parent.getPlayers().get(1).tanks.add((Tank) model.getTanksMap().get(id));
                else parent.getPlayers().get(0).tanks.add((Tank) model.getTanksMap().get(id));
            }
        }
    }

    /**
     * Add Projectiles to map and player
     *
     * @param tmp he projectiles to process
     */
    private void processProjectiles(List<DataTimeItem<ProjectileData>> tmp) {
        if (tmp.size() != 0) {
            for (DataTimeItem<ProjectileData> d : tmp) {
                Projectile r = Projectile.mkProjectile(model, d.data.mkCopy());
                model.getTanksMap().getAddedProjectiles().put(d.data.getId(), r);
                r.interpolateData(d);
                parent.getPlayers().get(r.getEnemy().tankID).projectiles.add(r);
            }
        }
    }

    /**
     * processes the turret updates
     */
    private void processTurretUpdates() {
        if (turretUpdates.isEmpty()) return;
        TurretUpdateMessage[] tmp = turretUpdates.toArray(new TurretUpdateMessage[turretUpdates.size()]);
        turretUpdates.clear();
        for (TurretUpdateMessage msg : tmp) {
            Tank tank = model.getTanksMap().getTank(PlayerEnum.getPlayer(msg.id));
            tank.getLatestOp().data.setTurretDir(msg.turDir);
            int idEn = msg.id == 0 ? 1 : 0;
            if (!parent.getPlayers().get(idEn).tanks.contains(tank)) parent.getPlayers().get(idEn).tanks.add(tank);
        }
    }

    /**
     * separates incoming DataTimeItem and adds the elements of the list to the correct Data List
     *
     * @param dat incoming list of DataTimeItems
     */
    private void makeDatLists(List<DataTimeItem<? extends Data>> dat) {
        if (dat.size() == 0) return;
        for (DataTimeItem<? extends Data> item : dat) {
            if (item.getId() < 2) tankDat.add((DataTimeItem<TankData>) item);
            else projectileDat.add((DataTimeItem<ProjectileData>) item);
        }
    }

    /**
     * supplies the players with the updated projectile and the updated tank (necessary after collisions)
     *
     * @param proj the projectile involved in the collision
     * @param tank the tank involved in the collision
     * @param damage the damage dealt
     * @param dest if the tank is destroyed
     */
    @Override
    public void notifyProjTank(Projectile proj, Tank tank, int damage, boolean dest) {
        if (dest) {
            tank.destroy();
        }
        else {
            tank.processDamage(damage);
            tank.getLatestOp().data.setLifePoints(tank.getArmor().getArmorPoints());
        }
        proj.destroy();
        proj.getLatestOp().data.destroy();

        for (Player pl : parent.getPlayers()) {
            pl.tanks.add(tank);
            pl.projectiles.add(proj);
        }
    }

    /**
     * supplies the players with the updated projectile and the updated BBlock (necessary after collisions)
     *
     * @param proj the projectile involved in the collision
     * @param block the block involved in the collision
     * @param damage the damage dealt
     * @param dest if the block is destroyed
     */
    @Override
    public void notifyProjBBlock(Projectile proj, BreakableBlock block, int damage, boolean dest) {
        if (dest) {
            block.destroy();
        }
        else {
            block.processDamage(damage);
        }
        proj.destroy();
        proj.getLatestOp().data.destroy();

        for (Player pl : parent.getPlayers()) {
            pl.blocks.add(block);
            pl.projectiles.add(proj);
        }
    }

    /**
     * supplies the players with the updated projectiles (necessary after collisions)
     *
     * @param proj1 the projectile1 involved in the collision
     * @param proj2 the projectile2 involved in the collision
     */
    @Override
    public void notifyProjProj(Projectile proj1, Projectile proj2) {
        proj1.destroy();
        proj1.getLatestOp().data.destroy();
        proj2.destroy();
        proj2.getLatestOp().data.destroy();
        for (Player pl : parent.getPlayers()) {
            pl.projectiles.add(proj1);
            pl.projectiles.add(proj2);
        }
    }

    /**
     * sets the winning players game to won
     *
     * @return true if game is finished
     */
    public boolean isGameEnd() {
        if (model.gameFinished()) {
            if (model.getTanksMap().get(0).isDestroyed()) {
                parent.getPlayers().get(1).setGameWon(true);
            }
            else {
                parent.getPlayers().get(0).setGameWon(true);
            }
            return true;
        }

        return false;
    }

    /**
     * In case of a disconnect while playing the opposing player wins
     *
     * @param conn to be closed
     */
    @Override
    public void playerDisconnected(IConnection<IServerMessage> conn) {
        parent.getPlayers().removeIf(p -> p.getConnection() == conn);
        conn.shutdown();
        Player lastPlayer = parent.getPlayers().get(0);
        lastPlayer.otherPlayerDisconnected();
        lastPlayer.setGameWon(true);
        lastPlayer.sendEndingMessage(gameMode);
        parent.goToState(parent.containingState().waitingFor2Player);
    }

    /**
     * receives the turret-updates and adds them to the appropriate list
     */
    @Override
    public void turretUpdate(TurretUpdateMessage msg) {
        turretUpdates.add(msg);
    }
}
