package pp.tanks.server.auto;

import pp.tanks.message.client.BackMessage;
import pp.tanks.message.client.StartGameMessage;
import pp.tanks.message.client.UpdateTankConfigMessage;
import pp.tanks.message.data.TankData;
import pp.tanks.message.server.ServerTankUpdateMessage;
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
        System.out.println("Player Ready State");
    }

    @Override
    public void back(BackMessage msg) {
        for (Player p : parent.getPlayers()) {
            p.getConnection().shutdown();
        }
        parent.getPlayers().clear();
        containingState().goToState(parent.init);
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
        p.setReady();
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

    /**
     * TODO: add JavaDoc
     *
     * @param model
     */
    public void tutorialGame(Model model) {
        Player pl = parent.getPlayers().get(0);
        TankData data1 = new TankData(new DoubleVec(3, 6), 0, 1, MoveDirection.STAY, 0, new DoubleVec(0, 0), false);
        model.getTanksMap().addPlayerTank(PlayersTank.mkPlayersTank(model, pl.getTurret(), pl.getArmor(), data1));
    }

    /**
     * TODO: add JavaDoc
     *
     * @param model
     */
    public void singleplayerGameLvlOne(Model model) {
        Player pl = parent.getPlayers().get(0);
        TankData data1 = new TankData(new DoubleVec(3, 6), 0, 3, MoveDirection.STAY, 0, new DoubleVec(0, 0), false);
        model.getTanksMap().addPlayerTank(PlayersTank.mkPlayersTank(model, pl.getTurret(), pl.getArmor(), data1));
        TankData enemy1 = new TankData(new DoubleVec(18, 7), 1, 20, MoveDirection.STAY, 0, new DoubleVec(0, 0), false);
        TankData enemy2 = new TankData(new DoubleVec(20, 5), 2, 20, MoveDirection.STAY, 0, new DoubleVec(0, 0), false);
        TankData enemy3 = new TankData(new DoubleVec(17, 5), 3, 20, MoveDirection.STAY, 0, new DoubleVec(0, 0), false);
        model.getTanksMap().addCOMTank(new ArmoredPersonnelCarrier(model, enemy1));
        model.getTanksMap().addCOMTank(new Howitzer(model, enemy1));
        model.getTanksMap().addCOMTank(new ArmoredPersonnelCarrier(model, enemy1));
        List<ItemEnum> enums = new ArrayList<>(List.of(ItemEnum.ACP, ItemEnum.HOWITZER, ItemEnum.ACP));
        List<TankData> data = new ArrayList<>(List.of(enemy1, enemy2, enemy3));
    }
}
