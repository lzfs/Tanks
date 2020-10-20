package pp.battleship.model;

import pp.util.IntVec;

/**
 * Represents a Shot
 */
public class Shot extends IntVec {
    public final boolean hit;

    /**
     * Creates a new shot
     *
     * @param x     x position
     * @param y     y position
     * @param hit   whether a ship has been hit or not
     */
    public Shot(int x, int y,boolean hit) {
        super(x, y);
        this.hit =hit;
    }

    /**
     * Creates a new shot
     *
     * @param pos   position of the shot
     * @param hit   whether a ship has been hit or not
     */
    public Shot(IntVec pos, boolean hit) {
        super(pos.x, pos.y);
        this.hit = hit;
    }
}
