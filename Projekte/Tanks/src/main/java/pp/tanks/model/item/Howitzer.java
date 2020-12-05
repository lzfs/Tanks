package pp.tanks.model.item;

import pp.tanks.message.data.TankData;
import pp.tanks.model.Model;
import pp.tanks.model.item.navigation.Navigator;
import pp.util.DoubleVec;
import pp.util.IntVec;

import java.util.ArrayList;
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
    private final List<Block> usedBlocks = new ArrayList<>();
    private DoubleVec roundedPos;

    public Howitzer(Model model, TankData data) {
        super(model, new HeavyArmor(), new HeavyTurret(), data);
        movingCounter = 2;
        this.roundedPos = getHidingBlockPos().add(new DoubleVec(1, 0));
        navigateTo(roundedPos);
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
            shoot(model.getTanksMap().getTank(player1).getPos());
            setPos(roundedPos.add(new DoubleVec(0.1, 0)));
        }
        else if (path == null || path.isEmpty()) {
            if (model.getTanksMap().getTank(player1).getPos().distance(this.getPos()) < 5) {
                roundedPos = getHidingBlockPos().add(new DoubleVec(1, 0));
                navigateTo(roundedPos);
            } else {
                usedBlocks.clear();
            }
        }
    }

    /**
     * @return a block to hide behind
     */
    public DoubleVec getHidingBlockPos() {
        for (int col = model.getTanksMap().getWidth() - 3; col > 2; col--) {
            for (int row = 2; row < model.getTanksMap().getHeight() - 2; row++) {
                for (Block block : model.getTanksMap().getBlocks()) {
                    if (!usedBlocks.contains(block) && block.getPos().equals(new DoubleVec(col, row))) {
                        usedBlocks.add(block);
                        return block.getPos();
                    }
                }
            }
        }
        return getPos(); // default
    }
}
