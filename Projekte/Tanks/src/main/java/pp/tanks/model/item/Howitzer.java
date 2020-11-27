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
     *
     * @param delta
     */
    @Override
    public void behaviour(double delta) {
        getData().setTurretDir(model.getTanksMap().getTank(player1).getPos().sub(this.getPos()));
        if (canShoot() && Math.random() < 0.8) {
            if (canShoot()) {
                shoot(model.getTanksMap().getTank(player1).getPos());
            }
        }
        else if (path == null || path.isEmpty()){
            Tank playersTank = model.getTanksMap().getTank(player1);
            DoubleVec targetPos = playersTank.getPos().add(playersTank.getMoveDir().getVec().add(new DoubleVec(2, 2)));
            navigateTo(new DoubleVec(7, 7));
            setPos(new DoubleVec(Math.round(getPos().x), Math.round(getPos().y)));
            setMoveDirection(getMoveDirToVec(path.get(0).sub(getPos())));
            setMove(true);
        }
    }
}
