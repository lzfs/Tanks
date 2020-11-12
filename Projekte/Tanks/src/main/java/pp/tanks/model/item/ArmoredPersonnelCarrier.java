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
 */
public class ArmoredPersonnelCarrier extends COMEnemy {
    private int movingCounter;

    private final List<DoubleVec> path = new LinkedList<>();

    public ArmoredPersonnelCarrier(Model model, TankData data) {
        super(model, 3, new LightArmor(), new LightTurret(), data);
    }



    @Override
    public void behaviour(double delta) {
        turret.setDirection(model.getTanksMap().getTank().getPos().sub(this.getPos()));
        if(canShoot() && Math.random() < 0.8) {
            for(int i = 0; i < turret.getMagSize(); i++) {
                if(canShoot()) {
                    shoot(model.getTanksMap().getTank().getPos());
                }
            }
        } else {
            Tank playersTank = model.getTanksMap().getTank();
            DoubleVec targetPos = playersTank.getPos().add(playersTank.getMoveDir().getVec());
            //What if pos is blocked??
            navigateTo(targetPos);
            // as long as there is a path to follow and this time slot has still some time left
            while (path.size() > 0 && delta > 0.) {
                final DoubleVec target = path.get(0);
                if (getPos().distanceSq(target) < 1e-4) {
                    // we arte so close... let's jump to the next path point
                    setPos(target);
                    path.remove(0);
                }
                else {
                    final double bearing = target.sub(getPos()).angle();
                    double needToTurnBy = normalizeAngle(bearing - getRotation());
                    // we need to turn the droid such that its rotation coincides with the bearing of the next path point
                    if (Math.abs(needToTurnBy) >= delta * speed) {
                        // we are turning during the rest of this time slot
                        setRotation((int) (getRotation() + Math.signum(needToTurnBy) * delta * speed));
                        delta = 0.;
                    }
                    else {
                        // we first turn the droid
                        setRotation((int) bearing);
                        // and there is some time left in this time slot
                        delta -= Math.abs(needToTurnBy) / speed;
                        final double distanceToGo = getPos().distance(target);
                        if (distanceToGo >= delta * speed) {
                            // we do not reach the next path point in this time slot
                            setPos(getPos().add(DoubleVec.polar(speed * delta, getRotation())));
                            delta = 0.;
                        }
                        else {
                            // we have reached the next path pint in this time slot
                            setPos(target);
                            path.remove(0);
                            // and there is some time left in this time slot
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
            // remove first path point as this represents the field where the droid is currently stying
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
