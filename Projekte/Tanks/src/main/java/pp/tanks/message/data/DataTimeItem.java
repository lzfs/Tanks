package pp.tanks.message.data;

import java.io.Serializable;

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
     * uses the compareTo-methode of Priority-queues
     * @param o given serverTime
     * @return returns 1 or -1 depending which is time is bigger
     */
    @Override
    public int compareTo(DataTimeItem o) {
        return Long.compare(serverTime, o.serverTime);
    }

    @Override
    public String toString() {
        return "" + serverTime;
    }
}
