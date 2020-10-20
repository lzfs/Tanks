package pp.droids;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pp.droids.model.DroidsGameModel;
import pp.droids.model.item.Item;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static pp.droids.GamePlayTest.assertDoubleVecEquals;

public class SaveLoadTest {
    private static final double EPS = 0.001;
    public static final String FILE_NAME = "test-map.xml";

    private DroidsGameModel game;
    private File file;

    @BeforeEach
    public void setUp() throws URISyntaxException {
        // Create random game
        game = new DroidsGameModel(new Properties());
        game.loadRandomMap();

        // Check whether the test file already exists
        file = new File(FILE_NAME);
        if (file.exists())
            fail("File " + FILE_NAME + " already exists. Consider deleting it");
    }

    @Test
    public void saveAndLoad() throws IOException, XMLStreamException, URISyntaxException {
        game.saveMap(file);
        // Check whether test file exists
        assertTrue(file.exists(), "file " + FILE_NAME + " has not been written");

        // Load the file, which has just been written, into a new game
        final DroidsGameModel game2 = new DroidsGameModel(new Properties());
        game2.loadMap(file);

        checkEqualPositions(game.getDroidsMap().getEnemies(),
                            game2.getDroidsMap().getEnemies());
        checkEqualPositions(game.getDroidsMap().getObstacles(),
                            game.getDroidsMap().getObstacles());
        assertDoubleVecEquals(game.getDroidsMap().getDroid().getPos(),
                              game.getDroidsMap().getDroid().getPos(),
                              EPS);
    }

    private void checkEqualPositions(List<? extends Item> expected, List<? extends Item> actual) {
        final Iterator<? extends Item> it1 = expected.iterator();
        final Iterator<? extends Item> it2 = actual.iterator();
        while (it1.hasNext() && it2.hasNext())
            assertDoubleVecEquals(it1.next().getPos(), it2.next().getPos(), EPS);
        assertFalse(it1.hasNext());
        assertFalse(it2.hasNext());
    }

    @AfterEach
    public void tearDown() throws URISyntaxException {
        file.deleteOnExit();
    }
}
