package pp.tanks.model.item;

/**
 * Represents the armor of a Tank
 */
public class Armor {
    private int armorPoints;
    private int weight;
    protected int maxPoints;

    public Armor(int armorPoints, int weight) {
        this.armorPoints = armorPoints;
        this.weight = weight;
        this.maxPoints = 100;
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
     * @return returns maximal number of armor-lifepoints
     */
    public int getMaxPoints() {
        return maxPoints;
    }

    /**
     * Reduces armorPoints after getting hit by a projectile
     *
     * @param damage reducing damage-points
     */
    public void takeDamage(int damage) {
        this.armorPoints -= damage;
    }

    /**
     * updates armor-lifepoints
     *
     * @param points new armor-lifepoints
     */
    public void setArmorPoints(int points) {
        this.armorPoints = points;
    }

    /**
     * creates a new armor with the correct attributes
     *
     * @param armor new armor type
     * @return the armor
     */
    public static Armor mkArmor(ItemEnum armor) {
        if (armor == ItemEnum.LIGHT_ARMOR) return new LightArmor();
        else if (armor == ItemEnum.NORMAL_ARMOR) return new NormalArmor();
        else return new HeavyArmor();
    }
}
