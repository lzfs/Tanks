package pp.tanks.model.item;

import pp.util.DoubleVec;

import java.util.Arrays;

/**
 * base class of turret that are placed on a tank
 */
public class Turret {
    private int damage;
    private int bounced;
    private int weight;
    private double reloadTime;
    private double[] mag;
    private DoubleVec direction;
    private double cadence;
    private final double currentcadence;

    public Turret(int damage, int bounced, int weight, double reloadTime, int mag, double cadence) {
        this.damage = damage;
        this.bounced = bounced;
        this.weight = weight;
        this.reloadTime = reloadTime;
        this.mag = new double[mag];
        this.direction =new DoubleVec(0,0);
        Arrays.fill(this.mag, 0.0);
        this.cadence = 0;
        this.currentcadence = cadence;
    }

    /**
     * Called once per frame. Used for updating this item's position etc.
     *
     * @param delta time in seconds since the last update call
     */
    public void update(double delta) {
        cadence -= delta;
        if (cadence < 0) {
            cadence = 0;
        }
        for (int i = 0; i < mag.length; i++) {
            mag[i] -= delta;
            if (mag[i] < 0)
                mag[i] = 0;
        }
    }

    public int getBounces(){
        return bounced;
    }


    /**
     * @return s the size of the magazin
     */
    public int getMagSize() {
        return mag.length;
    }

    /**
     * fires a projectile, reduces magazine by one place
     */
    public void shoot() {
        for (int i = 0; i < mag.length; i++) {
            if (mag[i] == 0.0) {
                mag[i] = reloadTime;
                cadence = currentcadence;
                return;
            }
        }
    }

    /**
     * checks if the turret is able to shoot
     * @return
     */
    public boolean canShoot() {
        for (double d : mag) {
            if (d == 0.0 && cadence == 0) {
                return true;
            }
        }
        return false;
    }

    public int getDamage() {
        return damage;
    }

    public DoubleVec getDirection() {
        return this.direction;
    }

    public void setDirection(DoubleVec direction) {
        this.direction = direction;
    }
}
