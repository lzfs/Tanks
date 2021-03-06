package pp.tanks.model.item;

import pp.tanks.message.data.TankData;
import pp.tanks.model.Model;
import pp.util.DoubleVec;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a TankDestroyer, a specified type of a COMEnemy
 * A TankDestroyer is a balanced Tank: normal turret and normal armor, also strong but not the fastest
 * Because its balanced it drives to the target position of the playersTank to cut their way;
 * so its working best in combination with a APC, which drives towards the current position of the playersTank
 */
public class TankDestroyer extends COMEnemy {

    public TankDestroyer(Model model, TankData data) {
        super(model, new NormalArmor(), new NormalTurret(), data);
    }

    /**
     * specifies the behaviour of an TankDestroyer (driving towards the target position of the playersTank and constantly shooting at him)
     *
     * @param delta
     */
    @Override
    public void behaviour(double delta) {
        getData().setTurretDir(model.getTanksMap().getTank(player1).getPos().sub(this.getPos()));
        if (shootIsBlocked() && canShoot() && Math.random() < 0.8) {
            shoot(model.getTanksMap().getTank(player1).getPos());
        } else if (path == null || path.isEmpty()) {
            Tank playersTank = model.getTanksMap().getTank(player1);
            DoubleVec targetPos = playersTank.getPos().add(playersTank.getMoveDir().getVec().mult(2));
            DoubleVec targetPosReverse = playersTank.getPos().add(playersTank.getMoveDir().getVec().mult(-2));
            DoubleVec vec22 = new DoubleVec(0, 2);
            if (isWithinMap(targetPos) && targetPos != playersTank.getPos()) {
                navigateTo(targetPos);
            } else if (isWithinMap(targetPosReverse) && targetPos != playersTank.getPos()) {
                navigateTo(targetPosReverse);
            } else if (isWithinMap(playersTank.getPos().add(vec22))) {
                navigateTo(playersTank.getPos().add(vec22));
            } else {
                navigateTo(playersTank.getPos().sub(vec22));
            }
        }
    }
}
