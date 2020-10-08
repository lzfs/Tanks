package pp.droids.model.item;

import pp.droids.model.DroidsGameModel;

public abstract class LivingItem extends Item {
    private static final double FLASH_TIME = 1.;
    private static final double FLASH_INTERVAL = .1;

    private int lives;
    private double stillFlashing;

    /**
     * Creates a new shooting item.
     *
     * @param model           the game model
     * @param effectiveRadius the size of this item in terms of a radius of the bounding circle
     * @param lives           the number of lives that this item initially has
     */
    protected LivingItem(DroidsGameModel model, double effectiveRadius, int lives) {
        super(model, effectiveRadius);
        this.lives = lives;
    }

    /**
     * Returns the number of lives this item still has
     *
     * @return remaining number of lives
     */
    public int getLives() {
        return lives;
    }

    /**
     * Returns whether the item is currently visible
     *
     * @return is visible
     */
    public boolean isVisible() {
        if (isDestroyed()) return false;
        return !isFlashing() || Math.round(stillFlashing / FLASH_INTERVAL) % 2 == 0;
    }

    /**
     * Let the item start flashing after it has been hit, but not yet destroyed.
     */
    private void startFlashing() {
        stillFlashing = FLASH_TIME;
    }

    /**
     * Returns whether this item is still flashing after having been hit.
     */
    private boolean isFlashing() {
        return stillFlashing > 0;
    }

    /**
     * This method is called whenever the item is hit. This  method reduces the number of lives and
     * destroys it (by calling {@linkplain #destroy()}) if there are no lives left.
     */
    public void hit() {
        if (--lives > 0)
            startFlashing();
        else
            destroy();
    }

    /**
     * Updates the item
     *
     * @param delta time in seconds since the last update call
     */
    @Override
    public void update(double delta) {
        if (isFlashing())
            stillFlashing -= delta;
    }
}
