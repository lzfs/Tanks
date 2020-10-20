package pp.battleship.server;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Class used to set parameters for the game
 */
public class Config {
    private static final Logger LOGGER = Logger.getLogger(Config.class.getName());

    // Default values
    private final static int MAP_WIDTH = 10;
    private final static int MAP_HEIGHT = 10;
    private final static int HARBOR_WIDTH = 6;
    private final static int HARBOR_HEIGHT = 10;
    private final static int[] YARD = {4, 3, 2, 1};

    private final int mapWidth;
    private final int mapHeight;
    private final int harborWidth;
    private final int harborHeight;
    private final Map<Integer, Integer> shipYard = new HashMap<>();

    /**
     * Constructor used to set default parameters
     */
    public Config() {
        this(MAP_WIDTH, MAP_HEIGHT, HARBOR_WIDTH, HARBOR_HEIGHT, YARD);
    }

    /**
     * Constructor used to set specific parameters
     *
     * @param mapWidth      width of own and opponent map
     * @param mapHeight     height of own and opponent map
     * @param harborWidth   harbor width
     * @param harborHeight  harbor height
     * @param yard          determines the number of ships for each length
     */
    public Config(int mapWidth, int mapHeight, int harborWidth, int harborHeight, int[] yard) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.harborWidth = harborWidth;
        this.harborHeight = harborHeight;
        for (int len = 0; len < yard.length; len++)
            if (yard[len] <= 0)
                LOGGER.severe("number of ships of size " + (len + 1) + " is " + yard[len]); //NON-NLS
            else
                shipYard.put(len + 1, yard[len]);
    }

    /**
     * Returns a map that assigns to each ship length the number of ships of this length.
     * All integer numbers are positive
     */
    public Map<Integer, Integer> getShipYard() {
        return shipYard;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getHarborWidth() {
        return harborWidth;
    }

    public int getHarborHeight() {
        return harborHeight;
    }
}
