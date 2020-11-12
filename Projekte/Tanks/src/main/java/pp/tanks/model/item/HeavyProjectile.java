package pp.tanks.model.item;

import pp.tanks.message.data.ProjectileData;
import pp.tanks.model.Model;
import pp.util.DoubleVec;

/**
 * Represents a HeavyProjectile that flies across the map an makes damage in an specified radius on the map
 */
public class HeavyProjectile extends Projectile {
    private DoubleVec targetPos;

    public HeavyProjectile(Model model, double effectiveRadius, int damage, double speed, DoubleVec targetPos, ProjectileData data ){
        super(model, effectiveRadius, damage, speed,data);
        this.targetPos=targetPos;
        this.flag=1;
    }

    /**
     * method for test cases
     */
    @Override
    public void isVisible() {}

    /**
     * Accept method of the visitor pattern.
     */
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    /**
     * Called once per frame. Used for updating this item's position etc.
     *
     * @param delta time in seconds since the last update call
     */
    @Override
    public void update(double delta) {
        if(flag>0){
            flag-=delta;
        }
        if(flag<0){
            flag=0;
        }
        data.setPos(data.getPos().add(data.getDir().mult(delta * speed)));
        //TODO schiesst übers ziel hinaus
        if(getPos().distance(targetPos)<=0.3){
            setPos(targetPos);
        }
        if(getPos().x==targetPos.x && getPos().y== targetPos.y) {
            this.effectiveRadius=1.5;
            processHits();
            destroy();
        }
    }

    /**
     * makes damage at a specified radius on the map
     */
    /*
    @Override
    public void processHits() {
        for (Tank tank : model.getTanksMap().getTanks()) {
            if (collisionWith(tank)) {
                tank.processDamage(damage);
                return;
            }
        }
        for (BreakableBlock bblock : model.getTanksMap().getBreakableBlocks()) {
            if (collisionWith(bblock)) {
                bblock.reduce(damage);
                return;
            }
        }
    }

     */
    /**
     * Checks if the projectile hits an obstacle or an enemy. Projectiles are destroyed that way.
     */
    public void processHits() {
        for (Tank tank : model.getTanksMap().getTanks()) {
            if (collisionWith(tank) && flag == 0) {
                System.out.println("HIT tank");
                tank.processDamage(damage);
                destroy();
                return;
            }
        }
        for (BreakableBlock bblock : model.getTanksMap().getBreakableBlocks()) {
            if (collisionWith(bblock)) {
                System.out.println("HIT bblock");
                bblock.reduce(damage);
                destroy();
                return;
            }
        }
        for (ReflectableBlock rBlock : model.getTanksMap().getReflectable()) {
            if (collisionWith(rBlock)) {
                System.out.println("HIT rblock");
                reflect();
                return;
            }
        }
        for (UnbreakableBlock uBlock : model.getTanksMap().getUnbreakableBlocks()) {
            if (collisionWith(uBlock)) {
                System.out.println("HIT ublock");
                destroy();
            }
        }
    }


}
