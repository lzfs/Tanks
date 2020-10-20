package pp.droids;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pp.droids.model.DroidsGameModel;
import pp.droids.model.item.Droid;
import pp.droids.model.item.Enemy;
import pp.droids.model.item.Projectile;
import pp.util.DoubleVec;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class GamePlayTest {
    private static final double EPS = 0.001;

    private DroidsGameModel gameModel;

    @BeforeEach
    public void setUp() {
        gameModel = new DroidsGameModel(new Properties());
        gameModel.loadRandomMap();
    }

    // Check that enemy is destroyed after four hits
    @Test
    public void enemyDies() {
        final Enemy enemy = gameModel.getDroidsMap().getEnemies().get(0);
        // enemy should be alive
        assertFalse(enemy.isDestroyed());
        // first hit
        enemy.hit();
        // enemy should be alive
        assertFalse(enemy.isDestroyed());
        // three more hits
        enemy.hit();
        enemy.hit();
        enemy.hit();
        // enemy should be dead
        assertTrue(enemy.isDestroyed());
    }

    // Check that droid is destroyed after 40 hits
    @Test
    public void droidDies() {
        final Droid droid = gameModel.getDroidsMap().getDroid();
        // droid should be alive
        assertFalse(droid.isDestroyed());

        // 39 hits for droid
        for (int i = 0; i < 39; i++)
            droid.hit();

        // droid should be alive with only 1 live
        assertFalse(droid.isDestroyed());
        assertEquals(1, droid.getLives());

        // next hit -> dead
        droid.hit();

        // check, if droid is destroyed and not more visible
        assertTrue(droid.isDestroyed());
        assertFalse(droid.isVisible());
    }

    // Check that droid starts in the center of the map
    @Test
    public void playerStartPosition() {
        // calculate start position
        int sx = gameModel.getDroidsMap().getWidth() / 2;
        int sy = gameModel.getDroidsMap().getHeight() / 2;

        // Check, if droid on the right position
        assertDoubleVecEquals(new DoubleVec(sx, sy), gameModel.getDroidsMap().getDroid().getPos(), EPS);
    }

    // Check that droid cannot move to obstacle position
    @Test
    public void droidMoveToObstacle() {
        final Droid droid = gameModel.getDroidsMap().getDroid();
        final DoubleVec startPosition = droid.getPos();

        // try to navigate to obstacle position
        droid.navigateTo(gameModel.getDroidsMap().getObstacles().get(0).getPos());
        droid.update(2);
        // position of droid should not be changed
        assertDoubleVecEquals(startPosition, droid.getPos(), EPS);
    }

    // Check that droid can only move to enemy position if enemy has been destroyed
    @Test
    public void droidMoveToEnemy() {
        final Droid droid = gameModel.getDroidsMap().getDroid();
        final Enemy enemy = gameModel.getDroidsMap().getEnemies().get(0);
        final DoubleVec startPosition = droid.getPos();
        final DoubleVec enemyPosition = enemy.getPos();

        // navigate to enemy position
        droid.navigateTo(enemyPosition);
        droid.update(2);

        // position of droid should not be changed
        assertDoubleVecEquals(startPosition, droid.getPos(), EPS);

        // destroy enemy with hits
        for (int i = 0; i < 4; i++)
            enemy.hit();

        // enemy should be destroyed
        assertTrue(enemy.isDestroyed());

        // navigate to enemy position
        gameModel.update(3);
        droid.navigateTo(enemyPosition);
        droid.update(10);

        // position of droid should be changed
        assertDoubleVecEquals(enemyPosition, droid.getPos(), EPS);
    }

    // Check that droid cannot navigate to a position outside the map
    @Test
    public void droidMoveOutOfField() {
        // get start position of droid
        final Droid droid = gameModel.getDroidsMap().getDroid();
        final DoubleVec startPos = droid.getPos();

        // navigate to field out of range
        droid.navigateTo(new DoubleVec(gameModel.getDroidsMap().getWidth() + 1,
                                       gameModel.getDroidsMap().getHeight() + 1));
        gameModel.update(3);

        // position of droid should not be changed
        assertDoubleVecEquals(startPos, droid.getPos(), EPS);
    }

    // Check that projectile hits the enemy
    @Test
    public void droidProjectileTest() {
        final Droid droid = gameModel.getDroidsMap().getDroid();
        final Enemy enemy = gameModel.getDroidsMap().getEnemies().get(0);
        final Projectile projectile = Projectile.makeDroidProjectile(gameModel,
                                                                     droid.getPos(),
                                                                     enemy.getPos().sub(droid.getPos()));

        // add projectile to game
        gameModel.update(3);
        gameModel.getDroidsMap().add(projectile);

        assertEquals(4, enemy.getLives());

        // loop until enemy was damaged or max time has elapsed
        for (int i = 0; i < 10000 && enemy.getLives() == 4; i++)
            gameModel.update(0.1);

        // check that enemy has been hit once
        assertEquals(3, enemy.getLives());
    }

    // Check that game is won after all enemies are destroyed
    @Test
    public void gameWonTest() {
        assertFalse(gameModel.gameWon());

        // Hit each enemy four times
        for (Enemy enemy : gameModel.getDroidsMap().getEnemies())
            for (int i = 0; i < 4; i++)
                enemy.hit();

        gameModel.update(1);
        assertTrue(gameModel.getDroidsMap().getEnemies().isEmpty());
        assertTrue(gameModel.gameWon());
    }

    // Check that game is lost after the droid has been hit 40 times
    @Test
    public void gameLostTest() {
        assertFalse(gameModel.gameLost());
        final Droid droid = gameModel.getDroidsMap().getDroid();

        // hit the droid 40 times
        for (int i = 0; i < 40 && !droid.isDestroyed(); i++)
            droid.hit();

        gameModel.update(1);

        assertTrue(droid.isDestroyed());
        assertTrue(gameModel.gameLost());
    }

    public static void assertDoubleVecEquals(DoubleVec expected, DoubleVec actual, double distance) {
        if (expected.distanceSq(actual) > distance * distance)
            fail(formatValues(expected, actual));
    }

    public static String formatValues(Object expected, Object actual) {
        return String.format("expected: %s but was: %s", expected, actual);
    }
}
