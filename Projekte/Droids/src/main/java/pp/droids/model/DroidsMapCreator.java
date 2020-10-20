package pp.droids.model;

import pp.droids.model.item.Enemy;
import pp.droids.model.item.Item;
import pp.droids.model.item.Moon;
import pp.droids.model.item.Obstacle;
import pp.droids.model.item.Rocket;
import pp.util.DoubleVec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static pp.droids.DroidsIntProperty.fieldSizeX;
import static pp.droids.DroidsIntProperty.fieldSizeY;

/**
 * Convenience class for generating random game maps.
 */
class DroidsMapCreator {
    private static final Logger LOGGER = Logger.getLogger(DroidsMapCreator.class.getName());
    private static final int NUM_ENEMIES = 5;
    private static final int NUM_OBSTACLES = 5;
    private static final int NUM_ROCKETS = 2;
    private static final int NUM_MOONS = 2;

    private final DroidsGameModel model;

    /**
     * Creates an instance of this class for the specified game model.
     */
    public DroidsMapCreator(DroidsGameModel model) {
        this.model = model;
    }

    public DroidsMap makeEmptyMap() {
        final int width = fieldSizeX.value(model.getProperties());
        final int height = fieldSizeY.value(model.getProperties());
        return new DroidsMap(model, width, height);
    }

    /**
     * Creates a game map of the specified size with a droid in the middle and some random
     * other items.
     */
    public DroidsMap makeRandomMap() {
        final DroidsMap map = makeEmptyMap();
        int dx = map.getWidth() / 2;
        int dh = map.getHeight() / 2;

        List<DoubleVec> fields = new ArrayList<>(map.getWidth() * map.getHeight());
        for (int i = 0; i < map.getWidth(); i++)
            for (int j = 0; j < map.getHeight(); j++)
                if (i != dx || j != dh)
                    fields.add(new DoubleVec(i, j));
        Collections.shuffle(fields);
        final Iterator<DoubleVec> it = fields.iterator();

        map.getDroid().setPos(new DoubleVec(dx, dh));

        // add obstacles at random positions
        for (int i = 0; i < NUM_OBSTACLES; i++) {
            final Obstacle obs = new Obstacle(model, it.next());
            map.addObstacle(obs);
            double rdm = Math.random();
            if (rdm < 0.8) {
                map.addMoon(new Moon(model, obs.getPos()));
            }
            else {
                map.addMoon(new Moon(model, obs.getPos()));
                map.addMoon(new Moon(model, obs.getPos()));
            }
        }
        // add enemies at random positions
        for (int i = 0; i < NUM_ENEMIES; i++)
            map.addEnemy(new Enemy(model, it.next()));
        // add rockets at fixed positions
        for (int i = 0; i < NUM_ROCKETS; i++) {
            final double y = i * (map.getHeight() / NUM_ROCKETS);
            final Rocket rocket = new Rocket(model, new DoubleVec(map.getWidth() - i, y));
            rocket.setTarget(new DoubleVec(-1., y));
            map.addRocket(rocket);
        }

        if (LOGGER.isLoggable(Level.FINE))
            for (Item item : map)
                LOGGER.fine(item.getClass().getName() + " at " + item.getPos());

        return map;
    }
}
