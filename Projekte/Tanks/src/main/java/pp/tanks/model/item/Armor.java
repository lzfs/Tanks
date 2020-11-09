package pp.tanks.model.item;

/**
 * Represents the armor of a Tank
 */
public class Armor {
    private int armorPoints;
    private int weight;

    public Armor(int armorPoints, int weight) {
        this.armorPoints = armorPoints;
        this.weight = weight;
    }

    public int getWeight() {
        return this.weight;
    }

    public int getArmorPoints() {
        return this.armorPoints;
    }

    /**
     * Reduces armorPoints after getting hit by a projectile
     * @param damage
     */
    public void takeDamage(int damage) {
        this.armorPoints -= damage;
    }

    public void setArmorPoints(int points) {
        this.armorPoints = points;
    }
}
