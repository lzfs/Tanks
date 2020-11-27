package pp.tanks.message.data;

import pp.util.DoubleVec;

import java.io.Serializable;

/**
 * the class DataTimeItem is representing the time for messages sent to and from the server
 */
public class DataTimeItem<T extends Data> implements Comparable<DataTimeItem<? extends Data>>, Serializable {
    public final long serverTime;
    public final T data;

    /**
     * creates a new DataTimeItem
     *
     * @param data       sets the data
     * @param serverTime sets the serverTime for the action
     */
    public DataTimeItem(T data, long serverTime) {
        this.data = data;
        this.serverTime = serverTime;
    }

    /**
     * creates a similar copy of the current DataTimeItem-class for working processes
     *
     * @return returns the copy
     */
    public DataTimeItem<T> mkCopy() { return new DataTimeItem<>(this.data, this.serverTime);}

    /**
     * uses the compareTo-methode of Priority-queues
     *
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
    public DoubleVec getPos() {
        return this.data.getPos();
    }

    /**
     * @return current id
     */
    public int getId() {
        return data.getId();
    }

    @Override
    public String toString() {
        return "DataTimeItem: " + "data=" + data.toString() + ", servertime=" + serverTime;
    }
}
