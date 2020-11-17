package pp.tanks.server;

import pp.network.IConnection;
import pp.tanks.message.data.DataTimeItem;
import pp.tanks.message.server.IServerMessage;
import pp.tanks.message.server.ModelMessage;
import pp.tanks.model.item.Item;
import pp.tanks.model.item.ItemEnum;
import pp.tanks.model.item.PlayerEnum;
import pp.tanks.model.item.Projectile;

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
    private ItemEnum turret = null;
    private ItemEnum armor = null;
    private boolean ready;
    public final PlayerEnum playerEnum;
    public final List<Projectile> projectiles = new ArrayList<>();

    /**
     * creates new player
     * @param conn the connection to the client represented by this player
     */
    public Player(IConnection<IServerMessage> conn, PlayerEnum playerEnum) {
        this.playerEnum = playerEnum;
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

    /**
     * sets the info text to be displayed
     */
    public void setInfoText(String infoText){
        this.infoText = infoText;
    }

    public void setTurret(ItemEnum turret) {
        this.turret = turret;
    }

    public void setArmor(ItemEnum armor) {
        this.armor = armor;
    }

    public ItemEnum getTurret() {
        return turret;
    }

    public ItemEnum getArmor() {
        return armor;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady() {
        this.ready = true;
    }

    public void reset() {
        this.projectiles.clear();
    }

    public void sendMessages() {
        //System.out.println("send");
        if (!projectiles.isEmpty()) {
            List<DataTimeItem> r = new ArrayList<>();
            if (!projectiles.isEmpty()) {
                for (Projectile proj : projectiles) {
                    r.add(proj.getLatestOp());
                }
            }
            connection.send(new ModelMessage(null, r));
        }
    }
}
