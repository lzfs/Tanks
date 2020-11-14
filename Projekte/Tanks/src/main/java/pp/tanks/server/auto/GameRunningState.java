package pp.tanks.server.auto;

import pp.network.IConnection;
import pp.tanks.message.client.MoveMessage;
import pp.tanks.message.client.ShootMessage;
import pp.tanks.message.data.DataTimeItem;
import pp.tanks.message.server.IServerMessage;

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
    private final PlayingState parent;
    private final Queue<DataTimeItem> buffer = new PriorityBlockingQueue<>();
    private DataTimeItem[] working;
    private final Thread workWhatEver = new Thread(this::workBuff);

    /**
     * Constructor of the GameRunningState
     * @param parent the parent of this state, in this case, the PlayingState
     */
    public GameRunningState(PlayingState parent) {
        this.parent = parent;
    }

    /**
     * method used to process the messages that were contained in the buffer list, currently
     * only printing out the data
     */
    public void workBuff(){

    }

    /**
     * Method called upon entering the State. Current implementation only for testing the Timer function,
     * which is run with every 100ms. After that time, the added messages are processed
     */
    @Override
    public void entry() {
        System.out.println("runningState");
        Timer timer = new Timer();
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
}
