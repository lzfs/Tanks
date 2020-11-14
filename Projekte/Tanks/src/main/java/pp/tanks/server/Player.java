package pp.tanks.server;

import pp.network.IConnection;
import pp.tanks.message.server.IServerMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * The class "Player" represents a player on the server.
 * The task of the class is defined in the following methods.
 */
public class Player {
    private final IConnection<IServerMessage> connection;

    private final List<Long>ping = new ArrayList<>();
    private final List<Long>nano = new ArrayList<>();
    private String infoText = "";
    //private ClientState state = ClientState.INIT;

    /**
     * creates new player
     * @param conn the connection to the client represented by this player
     */
    public Player(IConnection<IServerMessage> conn) {
        this.connection = conn;
    }

    /**
     * Returns the connection to the client represented by this player.
     *
     * @return connection
     */
    public IConnection<IServerMessage> getConnection() {
        return connection;
    }


    /**
     * Adds a new ping-time to the ping-list
     * @param ping represents the given ping
     */
    public void addPing(long ping) {
        this.ping.add(ping);
    }

    /**
     * adds a new nano-time to the nano-list
     * @param nano represents the given nano
     */
    public void addNano(long nano) {
        this.nano.add(nano);
    }

    /**
     * finds the index of the smallest ping
     * @return the nanoOffset at the computed index
     */
    public long getOffset() {
        long smallestPing = ping.get(0);
        int indexPing = 0; //describes the index of the smallestPing
        for (int k = 0; k < ping.size(); k++) {
            if (ping.get(k) < smallestPing) {
                indexPing = k;
                smallestPing = ping.get(k);
            }
        }
        /*System.out.println("ping:");  // used for visual test of the serverteam
        for (long p : ping) System.out.println(p);
        System.out.println(ping.size());
        System.out.println("\nnano:");
        for (long p : nano) System.out.println(p);
        System.out.println(nano.size());*/
        return nano.get(indexPing);
    }


    /*
     * sets the control state for the player client
     *//* add this method after including the class "ClientState"
    public void setState(ClientState state) {
        this.state = state;
    }
    */

    /**
     * sets the info text to be displayed
     */
    public void setInfoText(String infoText){
        this.infoText = infoText;
    }

}
