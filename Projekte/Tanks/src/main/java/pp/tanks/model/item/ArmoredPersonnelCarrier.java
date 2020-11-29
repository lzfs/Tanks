package pp.tanks.model.item;

import pp.tanks.message.data.TankData;
import pp.tanks.model.Model;
import pp.tanks.model.item.navigation.Navigator;
import pp.util.DoubleVec;
import pp.util.IntVec;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a ArmoredPersonnelCarrier, a specified type of a COMEnemy
 * A ArmoredPersonnelCarrier is a light Tank: light Turret and light armor but very fast
 * Because its so fast it orientates to drive towards the enemy and constantly shoots at them
 */
public class ArmoredPersonnelCarrier extends COMEnemy {
    // private final List<DoubleVec> path = new LinkedList<>();

    public ArmoredPersonnelCarrier(Model model, TankData data) {
        super(model, 3, new LightArmor(), new LightTurret(), data);
    }

    /**
     * specifies the behaviour of an APC (driving towards the playersTank position and constantly shooting at him)
     *
     * @param delta
     */
    @Override
    public void behaviour(double delta) {
        getData().setTurretDir(model.getTanksMap().getTank(player1).getPos().sub(this.getPos()));
        if (!shootIsBlocked() && canShoot() && Math.random() < 0.8) {
            if (canShoot()) {
                shoot(model.getTanksMap().getTank(player1).getPos());
            }
        }
        else if (path == null || path.isEmpty()) {
            Tank playersTank = model.getTanksMap().getTank(player1);
            DoubleVec targetPos = playersTank.getPos().add(playersTank.getMoveDir().getVec().add(new DoubleVec(2, 2)));
            if (!isWithinMap(targetPos)) {
                navigateTo(targetPos);
            } else if (!isWithinMap(targetPos.sub(new DoubleVec(0,4)))) {
                navigateTo(targetPos.sub(new DoubleVec(0,4)));
            } else {
                navigateTo(playersTank.getPos());
            }
        }
    }

    /**
     * Returns the path the droid shall follow.
     */
    public List<DoubleVec> getPath() {
        return Collections.unmodifiableList(path);
    }
}
