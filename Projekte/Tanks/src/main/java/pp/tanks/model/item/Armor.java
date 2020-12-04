package pp.tanks.model.item;

/**
 * Represents the armor of a Tank
 */
public class Armor {
    private int armorPoints;
    private final int weight;
    protected int maxPoints;
    private final double effectiveRadius;

    public Armor(int armorPoints, int weight,double effectiveRadius) {
        this.armorPoints = armorPoints;
        this.weight = weight;
        this.maxPoints = 100;
        this.effectiveRadius=effectiveRadius;

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

    public double getEffectiveRadius() {
        return effectiveRadius;
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
