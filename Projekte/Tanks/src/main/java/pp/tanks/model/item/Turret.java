package pp.tanks.model.item;

import pp.tanks.message.data.ProjectileData;
import pp.tanks.model.Model;
import pp.util.DoubleVec;

import java.util.Arrays;

/**
 * base class of turret that are placed on a tank
 */
public abstract class Turret {
    //private int damage;
    //private int bounced;
    private final int weight;
    private final double reloadTime;
    private final double[] mag;
    private DoubleVec direction;
    private double cadence;
    private final double currentCadence;
    public final ItemEnum projectileType;

    public Turret(int weight, double reloadTime, int mag, double cadence, ItemEnum projectileType) {
        //this.damage = damage;
        //this.bounced = bounced;
        this.weight = weight;
        this.reloadTime = reloadTime;
        this.mag = new double[mag];
        this.direction =new DoubleVec(0,0);
        Arrays.fill(this.mag, 0.0);
        this.cadence = 0;
        this.currentCadence = cadence;
        this.projectileType = projectileType;
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



    /**
     * @return s the size of the magazine
     */
    public int getMagSize() {
        return mag.length;
    }

    public int getWeight() {
        return this.weight;
    }

    /**
     * fires a projectile, reduces magazine by one place
     */
    public void shoot() {
        for (int i = 0; i < mag.length; i++) {
            if (mag[i] == 0.0) {
                mag[i] = reloadTime;
                cadence = currentCadence;
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
    public abstract Projectile mkProjectile(Model model, ProjectileData data, DoubleVec target);

    public abstract int getBounces();

    public DoubleVec getDirection() {
        return this.direction;
    }

    public void setDirection(DoubleVec direction) {
        this.direction = direction;
    }
}
