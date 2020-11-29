package pp.tanks.server.auto;

import pp.network.IConnection;
import pp.tanks.message.client.PingResponse;
import pp.tanks.message.server.IServerMessage;
import pp.tanks.message.server.PingMessage;
import pp.tanks.message.server.SynchronizeMessage;
import pp.tanks.server.Player;

import java.util.List;

/**
 * State where the Clocks of the clients and the server are getting synchronized by sending the clients an offset they have
 * to add onto their System time
 */
public class SynchronizeState extends TankState {
    private final TankAutomaton parent;
    private int counter = 0;
    private long nanoTime1;
    private int playerCount = 0;
    private final List<Player> players;

    public SynchronizeState(TankAutomaton parent) {
        this.parent = parent;
        this.players = parent.getPlayers();// werden angepasst sobald die Player list im model ist
    }

    /**
     * @return the containing state
     */
    @Override
    public TankAutomaton containingState() {
        return parent;
    }

    /**
     * Method is called when the state is entered
     */
    @Override
    public void entry() {
        parent.getLogger().info("Synchronize State");
        call();
    }

    /**
     * method is called when the server receives a PingResponse from a client
     * and computes the ping and the nano offset for this client. Then it lets the state
     * decide if it should recall this client
     *
     * @param msg  the PingResponse
     * @param conn the connection which was used to send the message
     */
    @Override
    public void pingResponse(PingResponse msg, IConnection<IServerMessage> conn) {
        long ping = (System.nanoTime() - nanoTime1) / 2;
        long nano = nanoTime1 + ping - msg.nanoTime;
        players.get(playerCount).addPing(ping);
        players.get(playerCount).addNano(nano);
        decideRecall();
    }

    /**
     * sends a ping to the current player given in playerCount
     */
    public void call() {
        players.get(playerCount).getConnection().send(new PingMessage());
        nanoTime1 = System.nanoTime();
        counter++;
    }

    /**
     * decides if the client has to be recalled or the next player.
     * after all players are synchronized it sends the message to each player and enters the playing state
     */
    private void decideRecall() {
        if (counter < 10) {
            call();
        }
        else {
            if (playerCount + 1 != players.size()) {
                ++playerCount;
                counter = 0;
                call();
            }
            else {
                long latency = Math.max(players.get(0).getLatency(), players.get(1).getLatency());
                for (Player p : players) {
                    SynchronizeMessage msg = new SynchronizeMessage(p.getOffset(), latency);
                    p.getConnection().send(msg);
                }
                containingState().goToState(parent.playingState);
            }
        }
    }

    @Override
    public void exit() {
        counter = 0;
        playerCount = 0;
    }
}
