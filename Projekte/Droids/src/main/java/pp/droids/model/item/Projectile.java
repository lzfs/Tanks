package pp.droids.model.item;

import pp.droids.model.DroidsGameModel;
import pp.util.DoubleVec;

/**
 * A class representing projectiles.
 */
public class Projectile extends Item {

    private DoubleVec pos;
    private final DoubleVec direction;
    private final double speed;

    /**
     * Creates a projectile shot by the droid.
     *
     * @param model the game model in which the droid shot
     * @param pos   the position where the projectile starts to fly
     * @param dir   the vector indicating the direction in which the projectile flies.
     */
    public static Projectile makeDroidProjectile(DroidsGameModel model, DoubleVec pos, DoubleVec dir) {
        return new Projectile(model, .4, pos, dir, 10.);
    }

    /**
     * Creates a projectile
     *
     * @param model           the game model
     * @param effectiveRadius the size of this item in terms of a radius of the bounding circle
     * @param pos             the initial position of the projectile
     * @param direction       the direction where this projectile moves
     * @param speed           the speed of the projectile
     */
    private Projectile(DroidsGameModel model, double effectiveRadius, DoubleVec pos, DoubleVec direction, double speed) {
        super(model, effectiveRadius);
        this.pos = pos;
        this.direction = direction;
        this.speed = speed;
    }

    /**
     * Returns the position of the projectile
     *
     * @return position
     */
    @Override
    public DoubleVec getPos() {
        return pos;
    }

    /**
     * Returns the direction of the projectile
     *
     * @return direction
     */
    public DoubleVec getDirection() {
        return direction;
    }

    /**
     * Updates the projectile
     *
     * @param delta delta
     */
    @Override
    public void update(double delta) {
        pos = pos.add(direction.mult(delta * speed));
        if (!model.getDroidsMap().isWithinBorders(pos))
            destroy();
    }

    /**
     * Accept method of the visitor pattern.
     */
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    /**
     * Checks if the projectile hits an obstacle or an enemy. Projectiles are destroyed that way.
     */
    public void processHits() {
        for (Obstacle obs : model.getDroidsMap().getObstacles())
            if (collisionWith(obs)) {
                destroy();
                return;
            }
        for (Enemy enemy : model.getDroidsMap().getEnemies()) {
            if (collisionWith(enemy)) {
                enemy.hit();
                destroy();
                return;
            }
        }
    }
}
