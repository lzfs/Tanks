package pp.tanks.message.data;

import pp.util.DoubleVec;

import java.io.Serializable;

/**
 * base class of the data which is sent to the server
 */
public class Data implements Serializable{
    private DoubleVec pos;
    private int id;

    public Data(DoubleVec pos, int id) {
        this.destroyed = false;
        this.pos = pos;
        this.id = id;
    }
    private boolean destroyed;

    public void setPos(DoubleVec pos) {
        this.pos = pos;
    }

    public void destroy() {
        this.destroyed = true;
    }

    public DoubleVec getPos() {
        return pos;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public int getId() {
        return id;
    }
}
