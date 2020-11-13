package pp.tanks.model.item;


import pp.tanks.message.data.ProjectileData;
import pp.tanks.model.Model;
import pp.tanks.message.data.TankData;
import pp.tanks.notification.TanksNotification;
import pp.util.DoubleVec;

/**
 * abstract base class of all tanks in a {@linkplain pp.tanks.model.TanksMap}
 */
public abstract class Tank extends Item<TankData> {
    protected Turret turret;
    protected Armor armor;
    protected double speed=2;
    protected double rotgeschwind=150;
    protected TankData data;
    //protected double angle = 90;

    protected Tank(Model model, double effectiveRadius, Armor armor, Turret turret, TankData data) {
        super(model, effectiveRadius, data);
        this.armor = armor;
        this.turret = turret;
        this.data=data;
    }

    /**
     * checks if the tank is moving
     * @return
     */
    public boolean isMoving() {
        return data.isMoving();
    }

    public Turret getTurret(){
        return this.turret;
    }

    public Armor getArmor(){
        return armor;
    }

    /**
     * updates the move of a tank
     * @param move
     */
    public void setMove(boolean move) {

        data.setMove(move);
    }

    /**
     * updates the rotation of the turret
     * @param rotation
     */
    public void setRotation(double rotation) {
        data.setRotation(rotation%180);
    }

    public MoveDirection getMoveDir() {
        return data.getMoveDir();
    }

    /**
     * updates the MoveDirection
     * @param dir
     */
    public void setMoveDirection(MoveDirection dir) {
        data.setMoveDir(dir);
    }

    public double getSpeed() {
        return speed;
    }

    public double getRotation() {
        return data.getRotation();
    }

    @Override
    public void setPos(DoubleVec pos) {
        super.setPos(pos);
        data.setPos(pos);
        //System.out.println("Position  "+pos.x +"  " +pos.y);
    }

    /**
     * Called once per frame. Used for updating this item's position etc.
     *
     * @param delta time in seconds since the last update call
     */
    public void update(double delta) {
        turret.update(delta);
        updateMove(delta);
        data.setMove(false);
    }

    /**
     * updates the movement of a tank
     * @param delta
     */
    public void updateMove(double delta) {
        //System.out.println("Pos davor = " + data.getPos().x + "  "  + data.getPos().y);
        collide();
        if (isMoving() && !data.isDestroyed()) {

            Double aktuelleRotation = data.getRotation();
            Double moveDirRotation = data.getMoveDir().getRotation();
            Double tmp = (aktuelleRotation-moveDirRotation+360)%360;
            Double tmp1 = (moveDirRotation-aktuelleRotation+360)%360;
            Double tmp3 = Math.abs(aktuelleRotation-moveDirRotation); //TODO
            if(tmp3<2){
                setPos(getPos().add(getMoveDir().getVec().mult(delta * speed)));
            }
            else if(tmp>tmp1){
                data.setRotation(aktuelleRotation+delta*rotgeschwind);
            }else{
                data.setRotation(aktuelleRotation-delta*rotgeschwind);
            }
            /**/


            //System.out.println("Pos danach = " + data.getPos().x + "  "  + data.getPos().y);
        }
    }

    /**
     * Accept method of the visitor pattern.
     */
    public abstract void accept(Visitor v);

    /**
     * shooting a projectile in the direction of the cursor
     * @param pos
     */
    public void shoot(DoubleVec pos) {
        if(canShoot() && !this.isDestroyed()) {
            turret.shoot();
            Projectile projectile=makeProjectile(pos);
            model.getTanksMap().addProjectile(projectile);
        }
    }

    /**
     * checks if the tank is able to shoot
     * @return
     */
    protected boolean canShoot() {
        return turret.canShoot();
    }

    /**
     * decides which type of projectile the tank is shooting by type of turret it's using
     * @param targetPos
     * @return
     */
    private Projectile makeProjectile(DoubleVec targetPos) {
        //model.notifyReceivers(DroidsNotification.DROID_FIRED);

        //  pos, id,bounce, direction
        //pos vom Tank
        //direction brauche ich aus dem turret
        //System.out.println("Make projectile  "+this.getPos());
        //System.out.println("TURR "+ turret.getDirection().normalize());
        DoubleVec dir = targetPos.sub(this.getPos()).normalize();
        //System.out.println(dir +"  " +turret.getDirection());
        DoubleVec position = this.getPos().add(dir.mult(1.01));
        ProjectileData data = new ProjectileData(position, 1234,4, dir);  //TODO

        model.notifyReceivers(TanksNotification.TANK_FIRED);
        //final DoubleVec dir = DoubleVec.polar(1., getRotation()); //???
        if (turret instanceof LightTurret) {
            return new LightProjectile(model, 0.3, turret.getDamage(), 4,data);  //TODO
        }
        else if (turret instanceof NormalTurret) {
            return new NormalProjectile(model, 0.3,turret.getDamage(), 4,data); //TODO
        }
        else if (turret instanceof HeavyTurret) {
            //EFFECTIVE RADIUS
            return new HeavyProjectile(model, 0.3, turret.getDamage(), 4, targetPos,data); //TODO
        }



        return new LightProjectile(model, 0.3, turret.getDamage(), 4, data);
    }

    /**
     * handles the movement of the tank if it collides with other tanks or blocks in the map
     */
    private void collide() {
        for (Tank tank : model.getTanksMap().getTanks()) {
            if (this!=tank &&collisionWith(tank)) {
                setPos(getPos().sub(getMoveDir().getVec().mult(0.01)));
                setMove(false);
                return;
            }
        }
        for (Block block : model.getTanksMap().getBlocks()) {
            if (collisionWith(block)) {
                setMove(false);
                setPos(getPos().sub(getMoveDir().getVec().mult(0.01)));
                return;
            }
        }
    }

    /**
     * reduces armor points if the tank was hitted by a projectile
     * @param damage
     */
    public void processDamage(int damage) {
        System.out.println("DAMAGE " + damage);
        System.out.println("armor" + armor.getArmorPoints());
        if (armor.getArmorPoints() - damage <= 0) {
            armor.setArmorPoints(0);
            destroy();
            System.out.println("TANK DESTROYED");
        }
        else {
            armor.takeDamage(damage);
        }
        data.setLifepoints(armor.getArmorPoints());
    }
}
