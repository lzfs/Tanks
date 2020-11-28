package pp.tanks.server.auto;

import pp.network.IConnection;
import pp.tanks.message.client.BackMessage;
import pp.tanks.message.client.StartGameMessage;
import pp.tanks.message.client.UpdateTankConfigMessage;
import pp.tanks.message.data.TankData;
import pp.tanks.message.server.IServerMessage;
import pp.tanks.message.server.PlayerDisconnectedMessage;
import pp.tanks.message.server.ServerTankUpdateMessage;
import pp.tanks.message.server.SetPlayerMessage;
import pp.tanks.message.server.StartingMultiplayerMessage;
import pp.tanks.model.Model;
import pp.tanks.model.item.ArmoredPersonnelCarrier;
import pp.tanks.model.item.Howitzer;
import pp.tanks.model.item.ItemEnum;
import pp.tanks.model.item.MoveDirection;
import pp.tanks.model.item.PlayerEnum;
import pp.tanks.model.item.PlayersTank;
import pp.tanks.server.GameMode;
import pp.tanks.server.Player;
import pp.util.DoubleVec;

import java.util.ArrayList;
import java.util.List;

public class PlayerReadyState extends TankState {
    private final TankAutomaton parent;

    public PlayerReadyState(TankAutomaton parent) {
        this.parent = parent;
    }

    /**
     * the state where the players choose their tank
     */
    @Override
    public TankAutomaton containingState() {
        return this.parent;
    }

    @Override
    public void entry() {
        for (Player pl : parent.getPlayers()) {
            pl.setReady(false);
            pl.setGameWon(false);
        }
        if (parent.getPlayers().size() > 1) {
            ItemEnum turret = null;
            ItemEnum armor = null;
            for (Player pl : parent.getPlayers()) {
                if (pl.getArmor() != null) {
                    turret = pl.getTurret();
                    armor = pl.getArmor();
                }
                parent.getPlayers().get(pl.playerEnum.getEnemyID()).getConnection().send(new ServerTankUpdateMessage(turret, armor));
                turret = null;
                armor = null;
            }
        }
        parent.getLogger().info("Player Ready State");
    }

    @Override
    public void back(IConnection<IServerMessage> conn) {
        containingState().getPlayers().removeIf(p -> p.getConnection() == conn);
        conn.shutdown();
        //for (Player p : parent.getPlayers()) {
        //p.getConnection().shutdown();
        //}
        Player lastPlayer = parent.getPlayers().get(0);
        lastPlayer.otherPlayerDisconnected();
        containingState().goToState(parent.waitingFor2Player);
    }

    @Override
    public void playerDisconnected(IConnection<IServerMessage> conn) {
        back(conn);
    }

    @Override
    public void updateTankConfig(UpdateTankConfigMessage msg) {
        if (msg.player == PlayerEnum.PLAYER1) {
            parent.getPlayers().get(1).getConnection().send(new ServerTankUpdateMessage(msg.turret, msg.armor));
        }
        else parent.getPlayers().get(0).getConnection().send(new ServerTankUpdateMessage(msg.turret, msg.armor));
    }

    @Override
    public void startGame(StartGameMessage msg) {
        Player p = parent.getPlayers().get(msg.player.tankID);
        p.setArmor(msg.armor);
        p.setTurret(msg.turret);
        p.setReady(true);
        for (Player player : parent.getPlayers()) {
            if (!player.isReady()) return;
        }

        Model model = new Model(parent.getProperties());
        model.loadMap("map1.xml");

        multiplayerGame(model);

        model.getTanksMap().updateHashMap();
        parent.playingState.initializeGame(model, msg.gameMode);
        parent.goToState(containingState().synchronize);
    }

    /**
     * loads new model depending on the gamemode
     *
     * @param gameMode chosen gamemode
     * @return new model
     */
    public Model loadModel(GameMode gameMode) {
        Model model = new Model(parent.getProperties());
        if (gameMode == GameMode.TUTORIAL) {
            model.loadMap("map0.xml");
        }
        else if (gameMode == GameMode.SINGLEPLAYER) {
            model.loadMap("map1.xml");
        }
        else {
            model.loadMap("map1.xml");
        }
        return model;
    }

    /**
     * TODO: add JavaDoc
     *
     * @param model
     */
    public void multiplayerGame(Model model) {
        TankData data1 = new TankData(new DoubleVec(3, 6), 0, 1, MoveDirection.STAY, 0, new DoubleVec(0, 0), false);
        TankData data2 = new TankData(new DoubleVec(20, 6), 1, 1, MoveDirection.STAY, 0, new DoubleVec(-1, 0), false);
        for (Player pl : parent.getPlayers()) {
            ItemEnum turret = parent.getPlayers().get(pl.playerEnum.getEnemyID()).getTurret();
            ItemEnum armor = parent.getPlayers().get(pl.playerEnum.getEnemyID()).getArmor();
            if (pl.playerEnum == PlayerEnum.PLAYER1) {
                pl.getConnection().send(new StartingMultiplayerMessage(turret, armor, data1, data2));
                model.setTank(PlayersTank.mkPlayersTank(model, pl.getTurret(), pl.getArmor(), data1));
            }
            else {
                pl.getConnection().send(new StartingMultiplayerMessage(turret, armor, data2, data1));
                model.setTank(PlayersTank.mkPlayersTank(model, pl.getTurret(), pl.getArmor(), data2));
            }
        }
    }
}
