package pp.tanks.server.auto;

import pp.network.IConnection;
import pp.tanks.message.client.ClientReadyMsg;
import pp.tanks.message.server.IServerMessage;
import pp.tanks.server.Player;
import pp.tanks.server.TanksServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TankAutomaton extends TankStateMachine {
    private final TanksServer ts;
    private final List<Player> players = new ArrayList<>(); // only for testing has to be in Model

    public TankAutomaton(TanksServer ts) {
        this.ts = ts;
        entry();
    }

    /**
     * the init state of this automaton
     */
    private final TankState init = new TankState() {
        @Override
        public TankAutomaton containingState() {
            return TankAutomaton.this;
        }

        @Override
        public void playerConnected(ClientReadyMsg msg, IConnection<IServerMessage> conn) {
            players.add(new Player(conn));
            if (msg.nachricht.equals("Singleplayer")) {
                containingState().goToState(synchronize);
            }
            if (msg.nachricht.equals("multiplayer")) {
                containingState().goToState(waitingFor2Player);
            }
            //else containingState().goToState();
        }
    };

    /**
     * the state when a multiplayer game is started and a second player needs to connect
     */
    private final TankState waitingFor2Player = new TankState() {
        @Override
        public TankAutomaton containingState() {
            return TankAutomaton.this;
        }

        @Override
        public void playerConnected(ClientReadyMsg msg, IConnection<IServerMessage> conn) {
            players.add(new Player(conn));
            containingState().goToState(synchronize);
        }
    };

    private final TankState synchronize = new SynchronizeState(this);

    /**
     * the state where the players choose their tank
     */
    public final TankState playerReady = new TankState() {
        @Override
        public TankAutomaton containingState() {
            return TankAutomaton.this;
        }

        @Override
        public void entry() {
            System.out.println("Der Hamster liegt hier");
        }
    };


    public final TankState playingState = new PlayingState(this);


    // has to be in Model
    /**
     * Returns the player representing the client with the specified connection.
     *
     * @param conn the connection to the client
     */
    Player getPlayer(IConnection<IServerMessage> conn) {
        final Optional<Player> player = players.stream().filter(p -> p.getConnection() == conn).findAny();
        if (player.isPresent())
            return player.get();
        //LOGGER.severe("no player found with connection " + conn); //NON-NLS
        System.out.println("no player found with connection" + conn);
        return null;
    }

    public List<Player> getPlayers() {
        return players;
    }

    /*
     * Sets the client state to determine the controls
     *
     * @param p        the target player
     * @param infoText info text for the player
     * @param state    control state
     */ /* add this method after adding the class "ClientState"
    void setClientState(Player p, String infoText, ClientState state) {
        p.setState(state);
        p.setInfoText(infoText);
        ts.sendMap(p);
    }
    */


    @Override
    public TankState containingState() {
        return null;
    }

    @Override
    TankAutomaton getAuto() {
        return this;
    }

    @Override
    public TankState init() {
        return init;
    }

}
