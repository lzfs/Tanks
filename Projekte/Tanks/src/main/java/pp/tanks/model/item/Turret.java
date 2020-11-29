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
    private double cadence;
    private final double currentCadence;
    public final ItemEnum projectileType;

    public Turret(int weight, double reloadTime, int mag, double cadence, ItemEnum projectileType) {
        //this.damage = damage;
        //this.bounced = bounced;
        this.weight = weight;
        this.reloadTime = reloadTime;
        this.mag = new double[mag];
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
        if (delta < -1 || delta > 1) return;
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
     * @return the size of the magazine
     */
    public int getMagSize() {
        return mag.length;
    }

    /**
     * @return the weight of the tank
     */
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
     * @return boolean depending on the turret's ability to shoot
     */
    public boolean canShoot() {
        for (double d : mag) {
            if (d == 0.0 && cadence == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * creates a new projectile
     *
     * @param model  TODO
     * @param data   TODO
     * @param target new target-position
     * @return
     */
    public abstract Projectile mkProjectile(Model model, ProjectileData data, DoubleVec target);

    /**
     * @return number of bounces
     */
    public abstract int getBounces();

    /**
     * creates new turret
     *
     * @param turret correct turret-type
     * @return new turret
     */
    public static Turret mkTurret(ItemEnum turret) {
        if (turret == ItemEnum.LIGHT_TURRET) return new LightTurret();
        else if (turret == ItemEnum.NORMAL_TURRET) return new NormalTurret();
        else return new HeavyTurret();
    }
}
