package pp.tanks.message.data;

import pp.util.DoubleVec;

/**
 * Represents the data of a breakable block that is sent to the server
 */
public class BBData extends Data {
    private int lifepoints;

    public BBData(DoubleVec pos, int id, int lifepoints) {
        super(pos, id);
        this.lifepoints = lifepoints;
    }
    /**
     * creates a similar copy of the current BBData-class for working processes
     * @return returns the copy
     */
    public BBData mkCopy(){ return new BBData(this.getPos(), this.getId(), this.lifepoints);}

    public int getLifepoints() {
        return lifepoints;
    }

    /**
     * reduces the lifepoints of the breakable block after getting hit by a projectile
     * @param points
     */
    public void reduceLifepoints(int points) {
        this.lifepoints -= points;
    }
}
