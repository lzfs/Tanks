package pp.droids;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pp.droids.model.DroidsGameModel;
import pp.droids.model.DroidsMap;
import pp.droids.model.item.Enemy;
import pp.droids.model.item.Obstacle;
import pp.droids.model.item.Projectile;
import pp.droids.model.item.Rocket;
import pp.util.DoubleVec;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class GameMapTest {
    public static final int WIDTH = 15;
    public static final int HEIGHT = 25;

    private DroidsGameModel gameModel;
    private DroidsMap droidsMap;

    @BeforeEach
    public void setUp() {
        gameModel = new DroidsGameModel(new Properties());
        droidsMap = new DroidsMap(gameModel, WIDTH, HEIGHT);
        gameModel.setDroidsMap(droidsMap);
    }

    // Check size
    @Test
    public void mapDimensionTest() {
        assertEquals(WIDTH, gameModel.getDroidsMap().getWidth());
        assertEquals(HEIGHT, gameModel.getDroidsMap().getHeight());
    }

    // Check if Droid is in game
    @Test
    public void droidExist() {
        assertNotNull(gameModel.getDroidsMap().getDroid());
    }

    @Test
    public void enemyExist() {
        final Enemy enemy = new Enemy(gameModel, new DoubleVec(2, 2));
        droidsMap.addEnemy(enemy);
        gameModel.update(1);
        assertEquals(1, gameModel.getDroidsMap().getEnemies().size());
        assertSame(enemy, gameModel.getDroidsMap().getEnemies().get(0));
    }

    @Test
    public void projectileExist() {
        final Projectile projectile = Projectile.makeDroidProjectile(gameModel, new DoubleVec(5, 5), new DoubleVec(1, 1));
        droidsMap.add(projectile);
        gameModel.update(0.1);
        assertEquals(1, gameModel.getDroidsMap().getProjectiles().size());
        assertSame(projectile, gameModel.getDroidsMap().getProjectiles().get(0));
    }

    @Test
    public void obstacleExist() {
        final Obstacle obstacle = new Obstacle(gameModel, new DoubleVec(6, 6));
        droidsMap.addObstacle(obstacle);
        gameModel.update(0.1);
        assertEquals(1, gameModel.getDroidsMap().getObstacles().size());
        assertSame(obstacle, gameModel.getDroidsMap().getObstacles().get(0));
    }

    @Test
    public void rocketExist() {
        final Rocket rocket = new Rocket(gameModel, new DoubleVec(4, 7));
        droidsMap.addRocket(rocket);
        gameModel.update(0.1);
        assertEquals(1, gameModel.getDroidsMap().getRockets().size());
        assertSame(rocket, gameModel.getDroidsMap().getRockets().get(0));
    }
}
