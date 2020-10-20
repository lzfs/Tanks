package pp.droids.model;

import pp.droids.model.item.Droid;
import pp.droids.model.item.Enemy;
import pp.droids.model.item.Item;
import pp.droids.model.item.Moon;
import pp.droids.model.item.Obstacle;
import pp.droids.model.item.Projectile;
import pp.droids.model.item.Rocket;
import pp.util.DoubleVec;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Represents the entire game map. It can be accessed as an unmodifiable {@linkplain java.util.List}
 * of all items consisting of the droid, obstacles, enemies, rockets, and projectiles.
 */
public class DroidsMap extends AbstractList<Item> {
    private static final Logger LOGGER = Logger.getLogger(DroidsMap.class.getName());

    private final Droid droid;
    // lists of items
    private final List<Obstacle> obstacles = new ArrayList<>();
    private final List<Enemy> enemies = new ArrayList<>();
    private final List<Rocket> rockets = new ArrayList<>();
    private final List<Projectile> projectiles = new ArrayList<>();
    private final List<Moon> moons = new ArrayList<>();
    // list for collecting new projectiles
    private final List<Projectile> addedProjectiles = new ArrayList<>();

    private final int width;
    private final int height;

    /**
     * Creates a map of the specified size and with a droid at position (0,0)
     */
    public DroidsMap(DroidsGameModel model, int width, int height) {
        this.droid = new Droid(model);
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
        if (index == 0) return droid;
        int i = index - 1;
        if (i < obstacles.size()) return obstacles.get(i);
        i -= obstacles.size();
        if (i < enemies.size()) return enemies.get(i);
        i -= enemies.size();
        if (i < rockets.size()) return rockets.get(i);
        i -= rockets.size();
        if (i < moons.size()) return moons.get(i);
        i -= moons.size();
        return projectiles.get(i);
    }

    /**
     * Returns the number of all items in the map
     */
    @Override
    public int size() {
        return 1 + obstacles.size() + enemies.size() + rockets.size() + moons.size() + projectiles.size();
    }

    /**
     * Returns the droid
     *
     * @return droid
     */
    public Droid getDroid() {
        return droid;
    }

    /**
     * Returns the all obstacles
     *
     * @return the list of all obstacles
     */
    public List<Obstacle> getObstacles() {
        return Collections.unmodifiableList(obstacles);
    }

    /**
     * Returns all enemies still alive
     *
     * @return the list of all enemies still alive
     */
    public List<Enemy> getEnemies() {
        return Collections.unmodifiableList(enemies);
    }

    /**
     * Returns all rockets still existing
     *
     * @return the list of all rockets still existing
     */
    public List<Rocket> getRockets() {
        return Collections.unmodifiableList(rockets);
    }

    /**
     * Returns all flying projectiles.
     *
     * @return the list of all flying projectiles
     */
    public List<Projectile> getProjectiles() {
        return Collections.unmodifiableList(projectiles);
    }

    /**
     * Returns all moons.
     *
     * @return the list of all moons
     */
    public List<Moon> getMoons() {return Collections.unmodifiableList(moons);}

    /**
     * Returns the height (i.e., the number of rows) of the map
     *
     * @return height
     */
    public int getHeight() {
        return height;
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
     * Called once per frame. This method calls the update method of each item in this map and removes items that
     * cease to exist.
     *
     * @param deltaTime time in seconds since the last update call
     */
    void update(double deltaTime) {
        // checks if the Droid is not in the field and gives it a new position
        keepDroidInField();

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
        obstacles.removeAll(removed);
        enemies.removeAll(removed);
        rockets.removeAll(removed);
        projectiles.removeAll(removed);
    }

    /**
     * Checks, if a position is within the game map's borders
     *
     * @param pos the value, which should be checked
     */
    public boolean isWithinBorders(DoubleVec pos) {
        return pos.x >= -1 && pos.x <= getWidth() + 1 && pos.y >= -1 && pos.y <= getHeight() + 1;
    }

    /**
     * Increases the speed of all rockets
     *
     * @param f is added to current speed
     */
    public void increaseMovingItemsSpeed(double f) {
        for (Rocket rocket : rockets)
            rocket.increaseSpeed(f);
    }

    /**
     * Adds an enemy to this map.
     */
    public void addEnemy(Enemy e) {
        enemies.add(e);
    }

    /**
     * Adds an obstacle to this map.
     */
    public void addObstacle(Obstacle o) {
        obstacles.add(o);
    }

    /**
     * Adds a Rocket to the list of Rockets
     */
    public void addRocket(Rocket r) {
        rockets.add(r);
    }

    /**
     * Adds a projectile to this map.
     */
    public void add(Projectile p) {
        addedProjectiles.add(p);
    }

    /**
     * Adds a moon to this map.
     * @param m moon to be added
     */
    public void addMoon(Moon m) {
        moons.add(m);
    }

    /**
     * If the Droid leaves the Map it will be set the position of the droid to the exact opposite of the map
     */
    private void keepDroidInField() {
        if (getDroid().getPos() != null) {
            if (getDroid().getPos().x < 0) getDroid().setPos(new DoubleVec(getWidth(), getDroid().getPos().y));
            else if (getDroid().getPos().x > getWidth()) getDroid().setPos(new DoubleVec(0, getDroid().getPos().y));
            else if (getDroid().getPos().y > getHeight()) getDroid().setPos(new DoubleVec(getDroid().getPos().x, 0));
            else if (getDroid().getPos().y < 0) getDroid().setPos(new DoubleVec(getDroid().getPos().x, getHeight()));
        }
    }
}
