package pp.tanks.model;

import pp.tanks.message.data.Data;
import pp.tanks.model.item.Block;
import pp.tanks.model.item.BreakableBlock;
import pp.tanks.model.item.COMEnemy;
import pp.tanks.model.item.Item;
import pp.tanks.model.item.Oil;
import pp.tanks.model.item.PlayerEnum;
import pp.tanks.model.item.Projectile;
import pp.tanks.model.item.ReflectableBlock;
import pp.tanks.model.item.Tank;
import pp.tanks.model.item.UnbreakableBlock;
import pp.util.DoubleVec;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Represents the entire game map. It can be accessed as an unmodifiable {@linkplain java.util.List}
 * of all items consisting of the tanks, blocks and projectiles.
 */
public class TanksMap extends AbstractList<Item<? extends Data>> {
    private List<Tank> playersTanks = new ArrayList<>();
    private List<COMEnemy> enemy = new ArrayList<>();
    private List<ReflectableBlock> reflectableBlocks = new ArrayList<>();
    private List<BreakableBlock> breakableBlocks = new ArrayList<>();
    private List<UnbreakableBlock> unbreakableBlocks = new ArrayList<>();
    private List<Oil> oilList = new ArrayList<>();
    private final HashMap<Integer, Projectile> projectiles = new HashMap<>();
    private final HashMap<Integer, Projectile> addedProjectiles = new HashMap<>();
    private final int width;
    private final int height;
    private final Model model;
    private final List<ICollisionObserver> observers = new ArrayList<>();
    private final HashMap<Integer, Item<? extends Data>> hashMap = new HashMap<>();

    /**
     * Creates a map of the specified size and with a droid at position (0,0)
     */
    public TanksMap(Model model, int width, int height) {
        this.model = model;
        this.width = width;
        this.height = height;
    }

    /**
     * Returns the item (tank, block, etc.) at the specified index in the list of all items.
     * Index 0 always indicates the playersTank.
     *
     * @param index the index in the list of all items.
     */
    @Override
    public Item<? extends Data> get(int index) {
        int i = index;
        if (i < playersTanks.size()) return playersTanks.get(i);
        i -= playersTanks.size();
        if (i < enemy.size()) return enemy.get(i);
        i -= enemy.size();
        if (i < breakableBlocks.size()) return breakableBlocks.get(i);
        i -= breakableBlocks.size();
        if (i < unbreakableBlocks.size()) return unbreakableBlocks.get(i);
        i -= unbreakableBlocks.size();
        if (i < reflectableBlocks.size()) return reflectableBlocks.get(i);
        i -= reflectableBlocks.size();
        List<Projectile> tmp = new ArrayList<>(projectiles.values());
        return tmp.get(i);
    }

    //TODO: Sorry: Test if game works without this two methods
    /*
     * Checks whether the position (x,y) can be reached from the specified position pos where (x,y) is guaranteed
     * to be a neighbor of pos. This method takes into account that one must not make a diagonal move across a corner
     * of a blocked field, i.e., a field with an obstacle.
     *
     * @param startPos  the starting Position
     * @param targetPos the Position you go to
     * @return true or false

    public boolean accessibleFrom(DoubleVec startPos, DoubleVec targetPos) {
        if (blocked(startPos)) return false;
        if (targetPos.x == startPos.x || targetPos.y == startPos.y) return true;
        // this is a diagonal move => don't cut corners with obstacles
        return !blocked(new DoubleVec(startPos.x, targetPos.y)) && !blocked(new DoubleVec(targetPos.x, startPos.y));
    }


     * Returns whether the specified position is blocked by an obstacle
     *
     * @param pos the targeted position
     * @return true or false

    private boolean blocked(DoubleVec pos) {
        for (Item obs : getBlocks()) {
            if (obs.getPos().equals(pos)) {
                return true;
            }
        }
        for (Item obs : getCOMTanks()) {
            if (obs.getPos().equals(pos)) {
                return true;
            }
        }
        return false;
    }*/

    /**
     * Returns all tanks still alive
     *
     * @return the list of all tanks still alive
     */
    public List<COMEnemy> getCOMTanks() {
        return Collections.unmodifiableList(enemy);
    }

    public void addCOMTank(COMEnemy tank) {
        enemy.add(tank);
    }

    /**
     * @return list of all tanks
     */
    public List<Tank> getAllTanks() {
        List<Tank> tmp = new ArrayList<>(playersTanks);
        tmp.addAll(enemy);
        return tmp;
    }

    /**
     * returns the playersTank
     */
    public Tank getTank(PlayerEnum player) {
        return playersTanks.get(player.tankID);
    }

    /**
     * sets the playersTank on the first position in the list of tanks
     *
     * @param tank
     */
    public void addPlayerTank(Tank tank) {
        playersTanks.add(tank);
    }

    /**
     * @return hashMap with
     */
    public HashMap<Integer, Projectile> getAddedProjectiles() {
        return addedProjectiles;
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
        return List.copyOf(projectiles.values());
    }

    public HashMap<Integer, Projectile> getHashProjectiles() {
        return projectiles;
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
        return playersTanks.size() + breakableBlocks.size() + unbreakableBlocks.size() + reflectableBlocks.size() + projectiles.size() + enemy.size();
    }

    public List<Oil> getOilList() {
        return oilList;
    }

    public void addOil(Oil oil) {
        oilList.add(oil);
    }

    /**
     * Called once per frame. This method calls the update method of each item in this map and removes items that
     * cease to exist.
     *
     * @param serverTime time in seconds since the last update call
     */
    void update(long serverTime) {
        for (Item item : this) {
            item.update(serverTime);
        }
        projectiles.putAll(addedProjectiles);
        addedProjectiles.clear();
        for (Projectile proj : projectiles.values()) {
            if (!proj.isDestroyed()) {
                proj.processHits();
            }
        }

        List<Item> removed = new ArrayList<>();
        for (Item item : this)
            if (item.isDestroyed())
                removed.add(item);
        breakableBlocks.removeAll(removed);
        projectiles.entrySet().removeIf(e -> removed.contains(e.getValue()));

    }

    /**
     * Adds a Tank to this map.
     */
    public void addTanks(Tank tank) {
        if (tank instanceof COMEnemy) {
            enemy.add((COMEnemy) tank);
        } else {
            playersTanks.add(tank);
        }
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
        addedProjectiles.put(p.getProjectileData().id, p);
    }

    /**
     * adds a new observer to the ICollisionObserver interface
     *
     * @param obs
     */
    public void addObserver(ICollisionObserver obs) {
        observers.add(obs);
    }

    /**
     * deletes all observers
     */
    public void deleteAllObservers() {
        observers.clear();
    }

    /**
     * Notify the observers
     *
     * @param proj
     * @param tank
     * @param damage
     * @param dest
     */
    public void notifyObsT(Projectile proj, Tank tank, int damage, boolean dest) {
        if (observers.isEmpty()) return;
        for (ICollisionObserver obs : observers) {
            obs.notifyProjTank(proj, tank, damage, dest);
        }
    }

    /**
     * Notify the breakable Block observers
     *
     * @param proj
     * @param block
     * @param damage
     * @param dest
     */
    public void notifyObsB(Projectile proj, BreakableBlock block, int damage, boolean dest) {
        if (observers.isEmpty()) return;
        for (ICollisionObserver obs : observers) {
            obs.notifyProjBBlock(proj, block, damage, dest);
        }
    }

    /**
     * Notify the Projectile observers
     *
     * @param proj1
     * @param proj2
     */
    public void notifyObsP(Projectile proj1, Projectile proj2) {
        if (observers.isEmpty()) return;
        for (ICollisionObserver obs : observers) {
            obs.notifyProjProj(proj1, proj2);
        }
    }

    /**
     * Add new Items to the HashMap
     */
    public void updateHashMap() {
        for (Item<? extends Data> item : this) {
            if (!hashMap.containsKey(item.getData().id)) hashMap.put(item.getData().id, item);
        }
    }

    /**
     * !!!NOT for Projectiles!!!
     *
     * @param id
     * @return
     */
    public Item<? extends Data> getFromID(int id) {
        return hashMap.get(id);
    }
}
