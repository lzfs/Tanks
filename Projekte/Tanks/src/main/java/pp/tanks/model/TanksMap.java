package pp.tanks.model;

import pp.tanks.model.item.*;
import pp.util.DoubleVec;
import pp.util.IntVec;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Represents the entire game map. It can be accessed as an unmodifiable {@linkplain java.util.List}
 * of all items consisting of the tanks, blocks and projectiles.
 */
public class TanksMap extends AbstractList<Item> {
    private static final Logger LOGGER = Logger.getLogger(TanksMap.class.getName());
    private List<Tank> tanks = new ArrayList<>();
    private List<ReflectableBlock> reflectableBlocks = new ArrayList<>();
    private List<BreakableBlock> breakableBlocks = new ArrayList<>();
    private List<UnbreakableBlock> unbreakableBlocks = new ArrayList<>();
    private List<Projectile> projectiles = new ArrayList<>(); // TODO use map instead of list
    private final List<Projectile> addedProjectiles = new ArrayList<>();
    private final int width;
    private final int height;
    private final Model model;

    /**
     * Creates a map of the specified size and with a droid at position (0,0)
     */
    public TanksMap(Model model, int width, int height) {
        this.model = model;
        this.width = width;
        this.height = height;
    }

    /**
     * Returns the item (droid, obstacle, etc.) at the specified index in the list of all items.
     * Index 0 always indicates the droid.
     *
     * @param index the index in the list of all items.
     */
    @Override
    public Item get(int index) {
        if (index == 0) return tanks.get(0);
        int i = index - 1;
        if (i < tanks.size()) return tanks.get(i);
        i -= tanks.size();
        if (i < breakableBlocks.size()) return breakableBlocks.get(i);
        i -= breakableBlocks.size();
        if (i < unbreakableBlocks.size()) return unbreakableBlocks.get(i);
        i -= unbreakableBlocks.size();
        if (i < reflectableBlocks.size()) return reflectableBlocks.get(i);
        i -= reflectableBlocks.size();
        return projectiles.get(i);
    }

    /**
     * Checks whether the position (x,y) can be reached from the specified position pos where (x,y) is guaranteed
     * to be a neighbor of pos. This method takes into account that one must not make a diagonal move across a corner
     * of a blocked field, i.e., a field with an obstacle.
     *
     * @param startPos the starting Position
     * @param targetPos the Position you go to
     * @return true or false
     */
    public boolean accessibleFrom(DoubleVec startPos, DoubleVec targetPos) {
        if (blocked(startPos)) return false;
        if (targetPos.x == startPos.x || targetPos.y == startPos.y) return true;
        // this is a diagonal move => don't cut corners with obstacles
        return !blocked(new DoubleVec(startPos.x, targetPos.y)) && !blocked(new DoubleVec(targetPos.x, startPos.y));
    }

    /**
     * Returns whether the specified position is blocked by an obstacle
     *
     * @param pos the targeted position
     * @return true or false
     */
    private boolean blocked(DoubleVec pos) {

        for (Item obs : getBlocks()) {
            if (obs.getPos().equals(pos)) {
                return true;
            }
        }
        for (Item obs : getTanks()) {
            if (obs.getPos().equals(pos)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns all tanks still alive
     *
     * @return the list of all tanks still alive
     */
    public List<Tank> getTanks() {
        return Collections.unmodifiableList(tanks);
    }

    /**
     *
     */
    public Tank getTank() {
        return tanks.get(0);
    }

    /**
     * Returns all blocks placed in the map
     *
     * @return the list of all blocks placed in the map
     */
    public List<Block> getBlocks() {
        List<Block> tmp = new ArrayList<>();
        tmp.addAll(breakableBlocks);
        tmp.addAll(unbreakableBlocks);
        tmp.addAll(reflectableBlocks);
        return Collections.unmodifiableList(tmp);
    }

    /**
     * Returns all reflectable blocks
     *
     * @return the list of all reflectable blocks
     */
    public List<ReflectableBlock> getReflectable() {
        return Collections.unmodifiableList(reflectableBlocks);
    }

    /**
     * Returns all breakable blocks
     *
     * @return the list of all breakable blocks
     */
    public List<BreakableBlock> getBreakableBlocks() {
        return Collections.unmodifiableList(breakableBlocks);
    }

    /**
     * Returns all unbreakable blocks
     *
     * @return the list of all unbreakable blocks
     */
    public List<UnbreakableBlock> getUnbreakableBlocks() {
        return Collections.unmodifiableList(unbreakableBlocks);
    }

    /**
     * Returns all projectiles
     *
     * @return the list of all projectiles
     */
    public List<Projectile> getProjectiles() {
        return Collections.unmodifiableList(projectiles);
    }

    /**
     * Returns the width (i.e., the number of columns) of the map
     *
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height (i.e., the number of rows) of the map
     *
     * @return height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the model
     *
     * @return model
     */
    public Model getModel() {
        return model;
    }

    /**
     * Returns the number of all items in the map
     */
    @Override
    public int size() {
        return tanks.size() + breakableBlocks.size() + unbreakableBlocks.size() + reflectableBlocks.size() + projectiles.size();
    }

    /**
     * Called once per frame. This method calls the update method of each item in this map and removes items that
     * cease to exist.
     *
     * @param deltaTime time in seconds since the last update call
     */
    void update(double deltaTime) {
        for (Item item : this)
            item.update(deltaTime);

        // last update loop or user action may have created new projectiles
        projectiles.addAll(addedProjectiles);
        addedProjectiles.clear();

        // process hits by projectiles
        for (Projectile proj : projectiles)
            proj.processHits();

        // remove all destroyed items, except the droid
        List<Item> removed = new ArrayList<>();
        for (Item item : this)
            if (item.isDestroyed())
                removed.add(item);
        breakableBlocks.removeAll(removed);
        projectiles.removeAll(removed);
    }

    /**
     * Adds a Tank to this map.
     */
    public void addTanks(Tank t) {
        tanks.add(t);
    }

    /**
     * Adds a BreakableBlock to this map.
     */
    public void addBreakableBlock(BreakableBlock bb) {
        breakableBlocks.add(bb);
    }

    /**
     * Adds an UnbreakableBlock to the list of Rockets
     */
    public void addUnbreakableBlock(UnbreakableBlock ub) {
        unbreakableBlocks.add(ub);
    }

    /**
     * Adds a ReflectableBlock to this map.
     */
    public void addReflectableBlocks(ReflectableBlock rb) {
        reflectableBlocks.add(rb);
    }

    /**
     * Adds a projectile to this map.
     */
    public void addProjectile(Projectile p) {
        addedProjectiles.add(p);
    }
}
