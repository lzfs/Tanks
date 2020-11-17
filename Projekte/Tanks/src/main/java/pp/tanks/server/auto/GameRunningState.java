package pp.tanks.server.auto;

import pp.network.IConnection;
import pp.tanks.message.client.MoveMessage;
import pp.tanks.message.client.ShootMessage;
import pp.tanks.message.data.DataTimeItem;
import pp.tanks.message.data.ProjectileData;
import pp.tanks.message.server.IServerMessage;
import pp.tanks.model.Model;
import pp.tanks.model.item.Projectile;
import pp.tanks.server.GameMode;
import pp.tanks.server.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * The State representing a running singleplayer or multiplayer game.
 * Data form clients is received here and put into the buffer, which is periodically
 * emptied and the containing messages are then chronologically processes by the server
 */
public class GameRunningState extends TankState {
    private final Model model;
    private final PlayingState parent;
    private final Queue<DataTimeItem> buffer = new PriorityBlockingQueue<>();
    private DataTimeItem[] working;
    private final Thread workWhatEver = new Thread(this::workBuff);
    private final GameMode gameMode;
    private final List<DataTimeItem> tankDat = new ArrayList<>();
    private final List<DataTimeItem> projectileDat = new ArrayList<>();


    /**
     * Constructor of the GameRunningState
     * @param parent the parent of this state, in this case, the PlayingState
     */
    public GameRunningState(PlayingState parent, Model model, GameMode mode) {
        this.model = model;
        this.parent = parent;
        this.gameMode = mode;
    }

    /**
     * method used to process the messages that were contained in the buffer list, currently
     * only printing out the data
     */
    public void workBuff(){
        DataTimeItem[] tmp = working;
        working = null;
        long timeEnd = System.nanoTime();
        long timeStart = model.getLatestUpdate();
        long step = (timeEnd - timeStart) / 5;

        List<DataTimeItem> dat = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (tmp != null) {
                for (DataTimeItem item : tmp) {
                    if (item.serverTime < timeStart + step * (i + 1) && (i == 0 || item.serverTime > timeStart + step * i || i == 4 /* vlt andere Lösung*/)) dat.add(item);
                }
            }
            makeDatLists(dat);
            processProjectiles(timeStart + step * (i + 1));
            processTanks(timeStart + step * (i + 1));
            dat.clear();
        }
        model.setLatestUpdate(timeStart + 5 * step);

        for (Player p : parent.getPlayers()) {
            //p.sendMessages();
            //p.reset();
        }
    }


    /**
     * Method called upon entering the State. Current implementation only for testing the Timer function,
     * which is run with every 100ms. After that time, the added messages are processed
     */
    @Override
    public void entry() {
        System.out.println("runningState");
        Timer timer = new Timer();
        model.setLatestUpdate(System.nanoTime());
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                working = buffer.toArray(new DataTimeItem[buffer.size()]);
                buffer.clear();
                workWhatEver.run();
            }
        }, 100, 100);
    }

    /**
     * Override method mandatory to use methods of StateSupport
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
     * @param msg hte Message processed
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

    private void processTanks(long time) {
        List<DataTimeItem> tmp = new ArrayList<>(tankDat);
        this.tankDat.clear();

    }

    private void processProjectiles(long time) {
        List<DataTimeItem> tmp = new ArrayList<>(projectileDat);
        this.projectileDat.clear();

        /*if (tmp.size() != 0) {
            for (DataTimeItem d : tmp) {
                Projectile r = Projectile.mkProjectile(model,(ProjectileData) d.data.mkCopy());
                model.getTanksMap()..put(d.data.getId(), r);
                r.interpolateData(d);
                parent.getPlayers().get(r.getEnemy().tankId).rockets.add(r);
                r.interpolateTime(time);
            }
        }*/

    }

    /**
     * separates incoming DataTimeItem and adds the elements of the list to the correct Data List
     * @param dat incoming list of DataTimeItems
     */
    private void makeDatLists(List<DataTimeItem> dat) {
        if (dat.size() == 0) return;
        for (DataTimeItem item : dat) {
            if (gameMode == GameMode.SINGLEPLAYER || gameMode == GameMode.TUTORIAL) {
                if (item.getId() < 1) tankDat.add(item);
                else projectileDat.add(item);
            }
            if (gameMode == GameMode.MULTIPLAYER) {
                if (item.getId() < 2) tankDat.add(item);
                else projectileDat.add(item);
            }

        }
    }
}
