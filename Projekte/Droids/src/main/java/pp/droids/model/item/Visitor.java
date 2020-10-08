package pp.droids.model.item;

/**
 * Interface for an item visitor following the <em>visitor design pattern</em>.
 */
public interface Visitor {
    void visit(Droid droid);

    void visit(Obstacle obstacle);

    void visit(Enemy enemy);

    void visit(Projectile proj);

    void visit(Rocket rocket);
}
