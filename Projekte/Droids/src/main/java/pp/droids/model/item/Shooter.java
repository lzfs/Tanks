package pp.droids.model.item;

import pp.droids.model.DroidsGameModel;

/**
 * Abstract base class for items that can shoot and that may be hit by projectiles.
 */
public abstract class Shooter extends LivingItem {
    protected double rotation;

    private double stillReloading;
    private final double reloadTime;

    /**
     * Creates a new shooting item.
     *
     * @param model           the game model
     * @param effectiveRadius the size of this item in terms of a radius of the bounding circle
     * @param lives           the number of lives that this item initially has
     * @param reloadTime      the time in seconds that must pass before this item can shoot again
     */
    protected Shooter(DroidsGameModel model, double effectiveRadius, int lives, double reloadTime) {
        super(model, effectiveRadius, lives);
        this.reloadTime = reloadTime;
        startReloading();
    }

    /**
     * Creates a projectile. This method is called whenever this shooter fires.
     */
    abstract Projectile makeProjectile();

    /**
     * Lets the item fire a projectile if it is not reloading. The projectile is
     * created by calling method {@linkplain #makeProjectile()}
     */
    public void fire() {
        if (!isReloading()) {
            startReloading();
            model.getDroidsMap().add(makeProjectile());
        }
    }

    private void startReloading() {
        stillReloading = reloadTime;
    }

    /**
     * Returns whether this item is still reloading.
     */
    private boolean isReloading() {
        return stillReloading > 0;
    }

    /**
     * Returns the rotation of the item in degrees
     */
    public double getRotation() {
        return rotation;
    }

    /**
     * Sets the rotation of the item in degrees
     */
    public void setRotation(double rotation) {
        this.rotation = normalizeAngle(rotation);
    }

    /**
     * Normalizes the specified angle such the returned angle lies in the range -180 degrees
     * to 180 degrees.
     *
     * @param angle an angle in degrees
     * @return returns an angle equivalent to {@code angle} that lies in the range -180
     * degrees to 180 degrees.
     */
    static double normalizeAngle(double angle) {
        final double res = angle % 360.;
        if (res < -180.) return res + 360.;
        else if (res > 180.) return res - 360.;
        return res;
    }

    /**
     * Updates the item
     *
     * @param delta time in seconds since the last update call
     */
    @Override
    public void update(double delta) {
        if (isReloading())
            stillReloading -= delta;
        super.update(delta);
    }
}
