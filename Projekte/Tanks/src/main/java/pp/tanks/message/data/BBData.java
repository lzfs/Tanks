package pp.tanks.message.data;

import pp.util.DoubleVec;

/**
 * Represents the data of a breakable block that is sent to the server
 */
public class BBData extends Data {
    private int lifePoints;

    public BBData(DoubleVec pos, int id, int lifePoints, boolean destroyed) {
        super(pos, id, destroyed);
        this.lifePoints = lifePoints;
    }

    /**
     * creates a similar copy of the current BBData-class for working processes
     *
     * @return returns the copy
     */
    public BBData mkCopy() { return new BBData(this.getPos(), this.getId(), this.lifePoints, isDestroyed());}

    /**
     * @return current lifepoints
     */
    public int getLifePoints() {
        return lifePoints;
    }

    /**
     * reduces the lifepoints of the breakable block after getting hit by a projectile
     *
     * @param points number of reducing lifepoints (taken damage)
     */
    public void reduceLifepoints(int points) {
        this.lifePoints -= points;
    }
}
