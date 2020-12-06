package pp.tanks.model;

import pp.tanks.model.item.BreakableBlock;
import pp.tanks.model.item.Projectile;
import pp.tanks.model.item.Tank;

public interface ICollisionObserver {

    void notifyProjTank(Projectile proj, Tank tank, int damage, boolean dest);

    void notifyProjBBlock(Projectile proj, BreakableBlock block, int damage, boolean dest);

    void notifyProjProj(Projectile proj1, Projectile proj2);
}
