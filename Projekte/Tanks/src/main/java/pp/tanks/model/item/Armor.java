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

    /**
     * @return current weight
     */
    public int getWeight() {
        return this.weight;
    }

    /**
     * @return current armor-lifepoints
     */
    public int getArmorPoints() {
        return this.armorPoints;
    }

    /**
     * Reduces armorPoints after getting hit by a projectile
     * @param damage reducing damage-points
     */
    public void takeDamage(int damage) {
        this.armorPoints -= damage;
    }

    /**
     * updates armor-lifepoints
     * @param points new armor-lifepoints
     */
    public void setArmorPoints(int points) {
        this.armorPoints = points;
    }
}
