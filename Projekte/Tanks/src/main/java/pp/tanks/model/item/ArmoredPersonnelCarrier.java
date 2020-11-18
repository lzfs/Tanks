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
    private final List<DoubleVec> path = new LinkedList<>();

    public ArmoredPersonnelCarrier(Model model, TankData data) {
        super(model, 3, new LightArmor(), new LightTurret(), data);
    }

    /**
     * specifies the behaviour of an APC (driving towards the playersTank position and constantly shooting at him)
     * @param delta
     */
    @Override
    public void behaviour(double delta) {
        turret.setDirection(model.getTanksMap().getTank(player).getPos().sub(this.getPos()));
        if(canShoot() && Math.random() < 0.8) {
            if(canShoot()) {
                shoot(model.getTanksMap().getTank(player).getPos());
            }
        } else {
            Tank playersTank = model.getTanksMap().getTank(player);
            DoubleVec targetPos = playersTank.getPos().add(playersTank.getMoveDir().getVec().add(new DoubleVec(2,2)));
            navigateTo(targetPos);
            while (path.size() > 0 && delta > 0.) {
                final DoubleVec target = path.get(0);
                if (getPos().distanceSq(target) < 1e-4) {
                    setPos(target);
                    path.remove(0);
                }
                else {
                    //TODO
                    final double bearing = target.sub(getPos()).angle() % 180;
                    double needToTurnBy = normalizeAngle(bearing - getRotation()) % 180;
                    if (Math.abs(needToTurnBy) > 2) {   //TODO
                        Double currentRot = getRotation();
                        Double moveDirRotation = target.sub(getPos()).normalize().angle();
                        Double tmp = (currentRot - moveDirRotation + 360) % 360;
                        Double tmp1 = (moveDirRotation - currentRot + 360) % 360;
                        if (tmp > tmp1) {
                            setRotation(currentRot + delta * rotationSpeed);
                        } else {
                            setRotation(currentRot - delta * rotationSpeed);
                        }
                        delta = 0.;
                    }
                    else {
                        setRotation((int) bearing);
                        final double distanceToGo = getPos().distance(target);
                        if (distanceToGo >= delta * speed) {
                            DoubleVec dir = target.sub(getPos()).normalize();
                            setPos(getPos().add(dir.mult(delta * speed)));
                            delta = 0.;
                        }
                        else {
                            setPos(target);
                            path.remove(0);
                            delta -= distanceToGo / speed;
                        }
                    }
                }
            }
        }
    }

    /**
     * Searches for an optimal, collision-free path to the specified position and moves the droid there.
     *
     * @param target point to go
     */
    public void navigateTo(DoubleVec target) {
        path.clear();
        Navigator<IntVec> navigator = new TanksNavigator(model.getTanksMap(), getPos().toIntVec(), target.toIntVec());
        List<IntVec> pPath = navigator.findPath();
        if (pPath != null) {
            for (IntVec v : pPath)
                path.add(v.toFloatVec());
            if (path.size() > 1) path.remove(0);
        }
    }

    /**
     * Returns the path the droid shall follow.
     */
    public List<DoubleVec> getPath() {
        return Collections.unmodifiableList(path);
    }


    /**
     * Normalizes the specified angle such the returned angle lies in the range -180 degrees
     * to 180 degrees.
     *
     * @param angle an angle in degrees
     * @return returns an angle equivalent to {@code angle} that lies in the range -180
     * degrees to 180 degrees.
     */
    static double normalizeAngle(double angle) {
        final double res = angle % 360.;
        if (res < -180.) return res + 360.;
        else if (res > 180.) return res - 360.;
        return res;
    }
}
