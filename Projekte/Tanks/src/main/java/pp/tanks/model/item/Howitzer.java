package pp.tanks.model.item;

import pp.tanks.message.data.TankData;
import pp.tanks.model.Model;
import pp.util.DoubleVec;

/**
 * Represents a Howitzer, a specified type of a COMEnemy
 * A Howitzer is a heavy Tank: Strong Turret and strong armor but very slow
 * Because its so slow it orientates to the borders of the map to avoid an attack
 * from the flank by a fast tank
 */
public class Howitzer extends COMEnemy {
    private int movingCounter;

    protected Howitzer(Model model, TankData data) {
        super(model, 3, new HeavyArmor(), new HeavyTurret(), data);
        movingCounter = 2;
    }

    public void behaviour(double delta) {

        if(isMoving()) {
            super.update(delta);
            movingCounter -= 1;
        } else if(canShoot() && Math.random() < 0.8) {
            turret.setDirection(model.getTanksMap().getTank().getPos().sub(this.getPos()));
            shoot(model.getTanksMap().getTank().getPos());
        }

        if(movingCounter == 0) {
            setMove(false);
            if(Math.random() < 0.2) {
                setMove(true);
                movingCounter = (int) (Math.random() * 5 + 1);

                DoubleVec tmpPos;
                MoveDirection[] movingDirs;
                int idx;

                do {
                    movingDirs = MoveDirection.values();
                    idx = (int) (Math.random()*movingDirs.length);
                    tmpPos = getPos().add(movingDirs[idx].getVec().mult(delta*getSpeed()*movingCounter));
                } while(model.getTanksMap().accessibleFrom(getPos(), tmpPos));

                setMoveDirection(movingDirs[idx]);
            }
        }
    }
}
