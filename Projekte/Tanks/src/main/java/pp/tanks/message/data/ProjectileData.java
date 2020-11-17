package pp.tanks.message.data;

import pp.tanks.model.item.ItemEnum;
import pp.tanks.model.item.Projectile;
import pp.util.DoubleVec;

/**
 * Represents the data of a projectile that is sent to the server
 */
public class ProjectileData extends Data {
    private DoubleVec dir;
    private int bounce;
    public final ItemEnum type;

    public ProjectileData(DoubleVec pos, int id, int bounce,DoubleVec dir, ItemEnum type) {
        super(pos, id);
        this.bounce = bounce;
        this.dir = dir;
        this.type = type;
    }

    /**
     * creates a similar copy of the current ProjectileData-class for working processes
     * @return returns the copy
     */
    public ProjectileData mkCopy(){ return new ProjectileData(this.getPos(), this.getId(), this.bounce, this.dir, this.type);}

    /**
     * updates the direction of the projectile
     * @param dir given direction
     */
    public void setDir(DoubleVec dir) {
        this.dir = dir;
    }

    public int getBounce() {
        return bounce;
    }

    /**
     * if the projectile hit a reflect-able block, the @bounce counter is reduced by one place
     */
    public void bounced() {
        this.bounce -= 1;
    } //TODO

    public DoubleVec getDir() {
        return this.dir;
    }

    public ItemEnum getType() {
        return type;
    }
}
