package pp.battleship.model;

import pp.util.IntVec;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A rectangular map which contains ships and registers shots fired at this map. Valid positions
 * within this map have an x coordinate in the range 0..width-1 and y coordinate in the range
 * 0..height-1.
 *
 * @see #getWidth()
 * @see #getHeight()
 */
public class ShipMap implements Serializable {
    /**
     * The ships placed on this map.
     */
    private final List<Battleship> ships = new ArrayList<>();
    /**
     * The shots fired by the opponent.
     */
    private final List<Shot> shots = new ArrayList<>();
    /**
     * The ship that follows the mouse of the player when he places a new ship on this map.
     */
    private Battleship preview;

    private final int width;
    private final int height;

    /**
     * Creates a new and empty map of the specified size.
     *
     * @param width  the number of columns of this map.
     * @param height the number of rows of this map.
     */
    public ShipMap(int width, int height) {
        if (width < 1 || height < 1)
            throw new IllegalArgumentException("Invalid map size");
        this.width = width;
        this.height = height;
    }

    public List<Battleship> getShips() {
        return ships;
    }

    public List<Shot> getShots() {
        return shots;
    }

    public Battleship getPreview() {
        return preview;
    }

    public void setPreview(Battleship preview) {
        this.preview = preview;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * Searches for any ship of this map at the specified position.
     *
     * @param pos a position within this map
     * @return the ship that covers the specified position, or null if there is no such ship
     */
    public Battleship findShipAt(IntVec pos) {
        return ships.stream()
                .filter(ship -> ship.contains(pos))
                .findAny()
                .orElse(null);
    }

    /**
     * Repositions the ships of this map from top to bottom at the left border of
     * this map. Each ship has horizontal orientation.
     */
    public void orderShips() {
        IntVec pos = IntVec.NULL;
        Rotation r = Rotation.DOWN;
        for (Battleship ship : ships) {
            if (!isValid(pos))
                throw new RuntimeException("Too many ships for harbor");
            ship.setPos(pos);
            ship.setRot(Rotation.RIGHT);
            pos = r.next(pos);
        }
    }

    /**
     * Adds a new shot to this map. Any ship that is hit by the shot registers this hit.
     *
     * @param pos where the shot goes
     * @return the new shot added to this map. It contains the information whether the shot hit a ship.
     */
    public Shot shoot(IntVec pos) {
        final boolean hit = ships.stream().anyMatch(ship -> ship.hit(pos));
        final Shot shot = new Shot(pos, hit);
        shots.add(shot);
        return shot;
    }

    /**
     * Returns a new map that contains all the information already known by the opponent.
     */
    public ShipMap knownSoFar() {
        final ShipMap restricted = new ShipMap(width, height);
        restricted.shots.addAll(shots);
        for (Battleship ship : ships)
            if (ship.isDestroyed())
                restricted.ships.add(ship);
        return restricted;
    }

    /**
     * Checks whether the specified ship completely lies within this map and does neither overlap any
     * ship within this map nor is it too close to any of them.
     *
     * @param ship a ship that must not (yet) be contained in this map
     * @return true iff the specified ship would be correctly placed within this map with respect to
     * this map's ships.
     */
    public boolean placedCorrectly(Battleship ship) {
        return ship.getAllParts().stream()
                .allMatch(c -> isValid(c) && ships.stream().noneMatch(s -> s.tooClose(c)));
    }

    /**
     * Checks whether the specified position is within this map.
     *
     * @param pos a position
     * @return true iff the specified position lies within this map
     */
    public boolean isValid(IntVec pos) {
        return pos.x >= 0 && pos.x < width && pos.y >= 0 && pos.y < height;
    }
}
