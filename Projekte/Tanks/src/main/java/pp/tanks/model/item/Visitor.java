package pp.tanks.model.item;

/**
 * Interface for an item visitor following the <em>visitor design pattern</em>.
 */
public interface Visitor {
    void visit(PlayersTank playersTank);

    void visit(Enemy enemy);

    void visit(COMEnemy comEnemy);

    void visit(BreakableBlock bBlock);

    void visit(ReflectableBlock rBlock);

    void visit(UnbreakableBlock uBlock);

    void visit(LightProjectile lightProjectile);

    void visit(NormalProjectile normalProjectile);

    void visit(HeavyProjectile heavyProjectile);
}
