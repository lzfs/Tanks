package pp.tanks.model.item;

import pp.tanks.message.data.TankData;
import pp.tanks.model.Model;
import pp.tanks.model.item.navigation.Navigator;
import pp.util.DoubleVec;
import pp.util.IntVec;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a Howitzer, a specified type of a COMEnemy
 * A Howitzer is a heavy Tank: Strong Turret and strong armor but very slow
 * Because its so slow it orientates to the borders of the map to avoid an attack
 * from the flank by a fast tank
 */
public class Howitzer extends COMEnemy {
    private int movingCounter;
    private final List<DoubleVec> path = new LinkedList<>();

    public Howitzer(Model model, TankData data) {
        super(model, 3, new HeavyArmor(), new HeavyTurret(), data);
        movingCounter = 2;
    }

    /**
     * specifies the behaviour of an Howitzer (driving in their half of the map to avoid getting a contact with faster tanks)
     * @param delta
     */
    @Override
    public void behaviour(double delta) {
        getData().setTurretDir(model.getTanksMap().getTank(player).getPos().sub(this.getPos()));
        if (canShoot() && Math.random() < 0.8) {
            if (canShoot()) {
                shoot(model.getTanksMap().getTank(player).getPos());
            }
        }
        else {
            MoveDirection[] movingDirs;
            int idx;
            movingDirs = MoveDirection.values();
            idx = (int) (Math.random() * movingDirs.length);
            DoubleVec targetPos = getPos().add(movingDirs[idx].getVec().mult(7));
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
                    if (Math.abs(needToTurnBy) > 2) {
                        //TODO
                        Double currentRot = getRotation();
                        Double moveDirRotation = target.sub(getPos()).normalize().angle();
                        Double tmp = (currentRot - moveDirRotation + 360) % 360;
                        Double tmp1 = (moveDirRotation - currentRot + 360) % 360;
                        if (tmp > tmp1) {
                            setRotation(currentRot + delta * rotationSpeed);
                        }
                        else {
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
