package pp.tanks.message.data;

import pp.tanks.model.item.MoveDirection;
import pp.util.DoubleVec;

import java.io.Serializable;

/**
 * base class of the data which is sent to the server
 */
public class Data implements Serializable{
    private DoubleVec pos;
    public final int id;
    private boolean destroyed;

    public Data(DoubleVec pos, int id) {
        this.destroyed = false;
        this.pos = pos;
        this.id = id;
    }

    /**
     * update the position of the tank
     * @param pos new position
     */
    public void setPos(DoubleVec pos) {
        this.pos = pos;
    }

    /**
     * destroys the item
     */
    public void destroy() {
        this.destroyed = true;
    }

    /**
     * @return current position
     */
    public DoubleVec getPos() {
        return pos;
    }

    /**
     * checks if the current item is destroyed
     * @return boolean-value
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * updates the destroy-flag
     * @param bool new status
     */
    public void setDestroyed(boolean bool){this.destroyed = bool;}

    /**
     * @return item id
     */
    public int getId() {
        return id;
    }

    /**
     * creates a similar copy of the current Data-class for working processes
     * @return returns the copy
     */
    public Data mkCopy() { return  new Data(this.pos, this.id); }

}
