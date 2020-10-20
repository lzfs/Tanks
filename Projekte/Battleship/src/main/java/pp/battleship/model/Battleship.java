package pp.battleship.model;

import pp.util.IntVec;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a battleship
 */
public class Battleship implements Serializable {
    public final int length;
    private final boolean[] intactBody;
    private IntVec pos = IntVec.NULL;
    private Rotation rot = Rotation.RIGHT;
    /**
     * The cache for method {@linkplain #getAllParts()}.
     */
    private List<IntVec> parts;

    /**
     * Creates a new ship with the specified length with horizontal orientation and
     * with reference position at (0,0).
     *
     * @param length the length of the created ship
     */
    public Battleship(int length) {
        if (length < 1)
            throw new IllegalArgumentException("Non-positive ship length");
        this.length = length;
        intactBody = new boolean[length];
        Arrays.fill(intactBody, true);
    }

    /**
     * Sets position and rotation of this ship to the corresponding values of the specified ship.
     *
     * @param other a ship whose position and rotation are copied to this ship.
     */
    public void setParamsTo(Battleship other) {
        parts = null;
        pos = other.pos;
        rot = other.rot;
    }

    /**
     * Returns the position of the battleship
     *
     * @return      the position
     */
    public IntVec getPos() {
        return pos;
    }

    /**
     * Sets the position of the battleship
     *
     * @param pos   the position to be set
     */
    public void setPos(IntVec pos) {
        parts = null;
        this.pos = pos;
    }

    /**
     * Returns the rotation of the ship
     *
     * @return      the rotation
     */
    public Rotation getRot() {
        return rot;
    }

    /**
     * Sets the rotation of the ship
     *
     * @param rot   rotation to be set
     */
    public void setRot(Rotation rot) {
        parts = null;
        this.rot = rot;
    }

    /**
     * Returns all positions that are covered by this ship.
     *
     * @return a list of position vectors covered by this ship.
     */
    public List<IntVec> getAllParts() {
        if (parts == null) {
            parts = new ArrayList<>(length);
            IntVec part = pos;
            for (int i = 0; i < length; i++) {
                parts.add(part);
                part = rot.next(part);
            }
        }
        return parts;
    }

    /**
     * Checks whether the specified position is covered by this ship and marks this part of the ship
     * as damaged, if this is the case.
     *
     * @param pos a position
     * @return true iff the specified position is covered by this ship.
     * @see #contains(pp.util.IntVec)
     */
    public boolean hit(IntVec pos) {
        final int index = getAllParts().indexOf(pos);
        if (index < 0) return false;
        intactBody[index] = false;
        return true;
    }

    /**
     * Checks whether the specified position is covered by this ship, but does not mark that part of
     * the ship as damaged.
     *
     * @param pos a position
     * @return true iff the specified position is covered by this ship.
     * @see #hit(pp.util.IntVec)
     */
    public boolean contains(IntVec pos) {
        return getAllParts().contains(pos);
    }

    /**
     * Checks whether this has been destroyed by hits.
     *
     * @return true if all parts of this ship have been hit by shots.
     * @see #hit(pp.util.IntVec)
     */
    public boolean isDestroyed() {
        for (boolean intact : intactBody)
            if (intact) return false;
        return true;
    }

    /**
     * Checks whether the specified position lies within the area immediately around this ship.
     *
     * @param pos a position
     * @return true iff  the specified position lies within the area immediately around this ship.
     */
    public boolean tooClose(IntVec pos) {
        return getAllParts().stream().anyMatch(p -> Math.abs(pos.x - p.x) <= 1 && Math.abs(pos.y - p.y) <= 1);
    }
}
