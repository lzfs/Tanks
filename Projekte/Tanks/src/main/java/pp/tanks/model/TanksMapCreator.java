package pp.tanks.model;

import pp.tanks.model.item.Armor;
import pp.tanks.model.item.Howitzer;
import pp.tanks.model.item.PlayersTank;
import pp.tanks.model.item.Tank;
import pp.tanks.model.item.Turret;
import pp.util.DoubleVec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static pp.tanks.TanksIntProperty.fieldSizeX;
import static pp.tanks.TanksIntProperty.fieldSizeY;

/**
 * Convenience class for generating random game maps.
 */
class TanksMapCreator {
    private static final Logger LOGGER = Logger.getLogger(TanksMapCreator.class.getName());
    private static final int NUM_ENEMIES = 5;
    private static final int NUM_OBSTACLES = 5;
    private static final int NUM_ROCKETS = 2;
    private static final int NUM_MOONS = 2;

    private final Model model;

    /**
     * Creates an instance of this class for the specified game model.
     */
    public TanksMapCreator(Model model) {
        this.model = model;
    }

    public TanksMap makeEmptyMap() {
        final int width = fieldSizeX.value(model.getProperties());
        final int height = fieldSizeY.value(model.getProperties());
        // changes were made below
        TanksMap map = new TanksMap(model, width, height);
        map.addTanks(new PlayersTank(model, 1, new Armor(5, 5), new Turret(2, 2, 2, 2, 2)));
        System.out.println("here");
        return map;
    }

    /**
     * Creates a game map of the specified size with a tank in the middle and some random
     * other items.
     */ /*
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
            final Rocket rocket = new Rocket(model, new DoubleVec(map.getWidth() - (i + 1), y));
            rocket.setTarget(new DoubleVec(-1., y));
            map.addRocket(rocket);
        }

        if (LOGGER.isLoggable(Level.FINE))
            for (Item item : map)
                LOGGER.fine(item.getClass().getName() + " at " + item.getPos());

        return map;
    }
    */
}

