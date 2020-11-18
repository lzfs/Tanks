package pp.tanks.server.auto;

import pp.tanks.message.client.BackMessage;
import pp.tanks.message.client.StartGameMessage;
import pp.tanks.message.client.UpdateTankConfigMessage;
import pp.tanks.message.server.ServerTankUpdateMessage;
import pp.tanks.model.Model;
import pp.tanks.model.item.ItemEnum;
import pp.tanks.model.item.PlayerEnum;
import pp.tanks.model.item.PlayersTank;
import pp.tanks.server.GameMode;
import pp.tanks.server.Player;

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
        if (msg.gameMode == GameMode.TUTORIAL) {
            model.loadMap("map0.xml");
        }
        else if (msg.gameMode == GameMode.SINGLEPLAYER){
            model.loadMap("map1.xml");
        }
        else {
            model.loadMap("map1.xml");
        }
        for (Player pl : parent.getPlayers()) {
            System.out.println("turret: " + pl.getTurret() + " armor: " + pl.getArmor());
            model.getTanksMap().addPlayerTank(PlayersTank.mkPlayersTank(pl.getTurret(), pl.getArmor()));//funktioniert nicht für Mulitplayer Spiele
        }
        System.out.println("hat geklappt");
        //TODO Nachricht an den Client wie der gegenerische Panzer aussieht bzw. das das Game startet (ModelMessage?)
        parent.playingState.initializeGame(model, msg.gameMode);
        parent.goToState(containingState().playingState);
    }


}
