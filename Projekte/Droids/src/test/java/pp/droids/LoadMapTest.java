package pp.droids;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pp.droids.model.DroidsGameModel;
import pp.droids.model.item.Enemy;
import pp.droids.model.item.Obstacle;
import pp.droids.model.item.Rocket;
import pp.util.DoubleVec;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static pp.droids.GamePlayTest.assertDoubleVecEquals;

public class LoadMapTest {
    private static final String FILE_NAME = "/maps/map.xml";
    private static final double EPS = 0.001;

    private DroidsGameModel gameModel;

    @BeforeEach
    public void setUp() throws IOException, XMLStreamException, URISyntaxException {
        gameModel = new DroidsGameModel(new Properties());
        final String path = getClass().getResource(FILE_NAME).toURI().getPath();
        gameModel.loadMap(new File(path));
    }

    @Test
    public void checkLoadedMap() {
        // Check, if map was loaded successfully
        assertNotNull(gameModel.getDroidsMap());

        // Check, if height and width are loaded successfully
        assertEquals(15, gameModel.getDroidsMap().getHeight());
        assertEquals(25, gameModel.getDroidsMap().getWidth());

        // Check, if the size of all loaded items are right
        assertNotNull(gameModel.getDroidsMap().getDroid());
        assertEquals(5, gameModel.getDroidsMap().getObstacles().size());
        assertEquals(5, gameModel.getDroidsMap().getEnemies().size());
        assertEquals(2, gameModel.getDroidsMap().getRockets().size());
    }

    @Test
    public void checkLoadedDroid() {
        // Check if Droid is on right position
        assertDoubleVecEquals(new DoubleVec(12, 7), gameModel.getDroidsMap().getDroid().getPos(), EPS);
    }

    @Test
    public void checkLoadedEnemies() {
        final Iterator<DoubleVec> it = List.of(new DoubleVec(13, 12),
                                               new DoubleVec(10, 11),
                                               new DoubleVec(4, 9),
                                               new DoubleVec(12, 10),
                                               new DoubleVec(21, 12))
                                           .iterator();
        assertEquals(5, gameModel.getDroidsMap().getEnemies().size());
        for (Enemy enemy : gameModel.getDroidsMap().getEnemies())
            assertDoubleVecEquals(it.next(), enemy.getPos(), EPS);
    }

    @Test
    public void checkLoadedObstacles() {
        final Iterator<DoubleVec> it = List.of(new DoubleVec(9, 9),
                                               new DoubleVec(4, 8),
                                               new DoubleVec(22, 7),
                                               new DoubleVec(1, 8),
                                               new DoubleVec(2, 12))
                                           .iterator();
        assertEquals(5, gameModel.getDroidsMap().getObstacles().size());
        for (Obstacle obstacle : gameModel.getDroidsMap().getObstacles())
            assertDoubleVecEquals(it.next(), obstacle.getPos(), EPS);
    }

    @Test
    public void checkLoadedRockets() {
        final Iterator<DoubleVec> it = List.of(new DoubleVec(9, 7),
                                               new DoubleVec(9, 5))
                                           .iterator();
        assertEquals(2, gameModel.getDroidsMap().getRockets().size());
        for (Rocket rocket : gameModel.getDroidsMap().getRockets())
            assertDoubleVecEquals(it.next(), rocket.getPos(), EPS);
    }
}
