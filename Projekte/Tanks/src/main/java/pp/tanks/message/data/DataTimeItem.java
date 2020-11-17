package pp.tanks.message.data;

import java.io.Serializable;

import pp.util.DoubleVec;

/**
 * the class DataTimeItem is representing the time for messages sent to and from the server
 */
public class DataTimeItem implements Comparable<DataTimeItem>, Serializable {
    public final long serverTime;
    public final Data data;

    /**
     * creates a new DataTimeItem
     * @param data sets the data
     * @param serverTime sets the serverTime for the action
     */
    public DataTimeItem(Data data, long serverTime) {
        this.data = data;
        this.serverTime = serverTime;
    }

    /**
     * creates a similar copy of the current DataTimeItem-class for working processes
     * @return returns the copy
     */
    public DataTimeItem mkCopy(){ return new DataTimeItem(this.data, this.serverTime);}

    /**
     * uses the compareTo-methode of Priority-queues
     * @param o given serverTime
     * @return returns 1 or -1 depending which is time is bigger
     */
    @Override
    public int compareTo(DataTimeItem o) {
        return Long.compare(serverTime, o.serverTime);
    }

    /**
     * @return current position
     */
    public DoubleVec getPos(){
        return this.data.getPos();
    }

    /**
     * @return current id
     */
    public int getId() {
        return data.getId();
    }
}
