package pp.tanks.model.item;

import pp.tanks.message.data.ProjectileData;
import pp.tanks.model.Model;
import pp.tanks.message.data.Data;
import pp.tanks.message.data.ProjectileData;
import pp.util.DoubleVec;

/**
 * abstract base class for all types of projectiles
 */
public abstract class Projectile extends Item<ProjectileData> {
    protected final int damage;
    protected final Double speed;
    protected final ProjectileData data;
    protected double flag;

    public Projectile(Model model, double effectiveRadius, int damage, Double speed, ProjectileData data) {
        super(model, effectiveRadius, data);
        this.data = data;
        this.damage = damage;
        this.speed = speed;
        this.flag = 2000;
        for(Block i : model.getTanksMap().getBlocks()) {
            if(collisionWith(i)) {
                System.out.println("destroy");
                destroy();
            }
        }
    }

    /**
     * reflect the projectile and gives it a new direction
     */
    /*
    public void reflect() {
        //TODO
        int i = 0;
        while (!collisionWith(model.getTanksMap().getReflectable().get(i))) {
            i++;
        }
        ReflectableBlock rBlock = model.getTanksMap().getReflectable().get(i);
        if (getPos().x <= rBlock.getPos().x + rBlock.getWidth() ||getPos().x >= rBlock.getPos().x - rBlock.getWidth()){
            System.out.println("erstes if");
            //rechts, links
            setDir(new DoubleVec(getDir().x * (-1), getDir().y));
        } else if (getPos().y <= rBlock.getPos().y + rBlock.getHeight() ||
        getPos().y >= rBlock.getPos().y - rBlock.getHeight()){
            System.out.println("zweites if");
            //oben, unten
            setDir(new DoubleVec(getDir().x, getDir().y * (-1)));
        }
    }

     */
    public void reflect() {
        int i = 0;
        while(!collisionWith(model.getTanksMap().getReflectable().get(i))) {
            i++;
        }
        ReflectableBlock rBlock = model.getTanksMap().getReflectable().get(i);
        double width = ((double )rBlock.getWidth()) / 2 ;
        double height = ((double) rBlock.getHeight()) / 2;
        if(getPos().x >= rBlock.getPos().x + width ||  getPos().x <= rBlock.getPos().x - width) {
            //rechts, links
            setDir(new DoubleVec(getDir().x * (-1), getDir().y));
        } else if(getPos().y >= rBlock.getPos().y + height ||  getPos().y <= rBlock.getPos().y - height) {
            //oben, unten
            setDir(new DoubleVec(getDir().x, getDir().y * (-1)));
        } else {
            setDir(new DoubleVec(getDir().x * (-1), getDir().y * (-1)));
        }
    }


    public void setDir(DoubleVec dir) {
        data.setDir(dir);
    }

    public DoubleVec getDir() {
        return data.getDir();
    }

    public int getDamage() {
        return this.damage;
    }

    public ProjectileData getProjectileData() {
        return this.data;
    }

    /**
     * Updates the projectile
     *
     * @param delta delta
     */
    @Override
    public void update(double delta) {
        //System.out.println("Projectile " + data.getPos());
        data.setPos(data.getPos().add(data.getDir().mult(delta * speed)));
        if(flag>0){
            flag-=delta;
        }
        if(flag<0){
            flag=0;
        }
        //System.out.println("!!!!! " + data.getPos());

    }

    /**
     * Checks if the projectile hits an obstacle or an enemy. Projectiles are destroyed that way.
     */
    public void processHits() {
        for (Tank tank : model.getTanksMap().getTanks()) {
            if (collisionWith(tank)&& flag==0) {
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

    /**
     * Accept method of the visitor pattern.
     */
    public abstract void accept(Visitor v);
}
