package pp.tanks.message.data;

import pp.util.DoubleVec;

/**
 * Represents the data of a projectile that is sent to the server
 */
public class ProjectileData extends Data {
    private DoubleVec dir;
    private int bounce;

    public ProjectileData(DoubleVec pos, int id, int bounce) {
        super(pos, id);
        this.bounce = bounce;
    }

    public void setDir(DoubleVec dir) {
        this.dir = dir;
    }

    public int getBounce() {
        return bounce;
    }

    /**
     * if the projectile hit a reflectable block, the @bounce counter is reduced by one place
     */
    public void bounced() {
        this.bounce -= 1;
    }

    public DoubleVec getDir() {
        return this.dir;
    }
}
