package pp.tanks;

import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import pp.tanks.client.TanksApp;
import pp.tanks.controller.Engine;
import pp.tanks.message.data.Data;
import pp.tanks.message.data.TankData;
import pp.tanks.model.Model;
import pp.tanks.model.TanksMap;
import pp.tanks.model.item.*;
import pp.util.DoubleVec;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SingleplayerTest {
    public static final int WIDTH = 23;
    public static final int HEIGHT = 13;

    private Model model;
    private TanksMap map;
    private Properties properties = new Properties();
    private PlayersTank player;


    @BeforeEach
    public void setUp() {
        model = new Model(properties);
        map = new TanksMap(model, WIDTH, HEIGHT);
        model.setTanksMap(map);
    }

    @Test
    public void movementTest(){
        player = new PlayersTank(model, 0.3, new HeavyArmor(), new LightTurret(),
                new TankData(new DoubleVec(3,6), 0, 100, MoveDirection.STAY, 0.0, new DoubleVec(0,1), false)); //TODO: multiply
        model.getTanksMap().addPlayerTank(player);
        assertEquals(new DoubleVec(3,6), player.getPos());
        player.setMoveDirection(MoveDirection.DOWN);
        assertEquals(MoveDirection.DOWN, player.getMoveDir());
        player.setMove(true);
        player.updateMove(5);
        //assertEquals(new DoubleVec(3,11), player.getPos());
    }

    @Test
    public void testLightTurret(){  //TODO: unvollständig
        Long time = System.nanoTime();
        player = new PlayersTank(model, 0.3, new HeavyArmor(), new LightTurret(),
                new TankData(new DoubleVec(3,6), 0, 100, MoveDirection.STAY, 0.0, new DoubleVec(0,1), false)); //TODO: multiply
        model.getTanksMap().addPlayerTank(player);
        assertEquals(0, model.getTanksMap().getProjectiles().size());
        UnbreakableBlock uBlock = new UnbreakableBlock(model, new Data(new DoubleVec(3,10), 10, false));
        model.getTanksMap().addUnbreakableBlock(uBlock);
        player.shoot(new DoubleVec(3,10));

        player.interpolateTime(time);
        player.shoot(new DoubleVec(3,10));

        //assertEquals(1, model.getTanksMap().getProjectiles().size());

        assertEquals(model.getTanksMap().getUnbreakableBlocks().get(0), uBlock);    //always the last test of shoot
    }

    @Test
    public void testNormalTurret(){ //TODO: unvollständig
        assertEquals(0, model.getTanksMap().getProjectiles().size());
        player = new PlayersTank(model, 0.3, new HeavyArmor(), new NormalTurret(),
                new TankData(new DoubleVec(3,6), 0, 100, MoveDirection.STAY, 0.0, new DoubleVec(0,1), false));
        model.getTanksMap().addPlayerTank(player);
        UnbreakableBlock uBlock = new UnbreakableBlock(model, new Data(new DoubleVec(3,10), 10, false));
        model.getTanksMap().addUnbreakableBlock(uBlock);
        player.shoot(new DoubleVec(3,10));
        model.update(System.nanoTime());
        assertEquals(1, model.getTanksMap().getProjectiles().size());

        assertEquals(model.getTanksMap().getUnbreakableBlocks().get(0), uBlock);    //always the last test of shoot
    }

    @Test
    public void testHeavyTurret(){  //TODO: unvollständig
        assertEquals(0, model.getTanksMap().getProjectiles().size());
        player = new PlayersTank(model, 0.3, new HeavyArmor(), new HeavyTurret(),
                new TankData(new DoubleVec(3,6), 0, 100, MoveDirection.STAY, 0.0, new DoubleVec(0,1), false));
        model.getTanksMap().addPlayerTank(player);
        UnbreakableBlock uBlock = new UnbreakableBlock(model, new Data(new DoubleVec(3,10), 10, false));
        model.getTanksMap().addUnbreakableBlock(uBlock);
        player.shoot(new DoubleVec(3,10));
        model.update(System.nanoTime());
        assertEquals(1, model.getTanksMap().getProjectiles().size());

        assertEquals(model.getTanksMap().getUnbreakableBlocks().get(0), uBlock);    //always the last test of shoot
    }

    @Test
    public void testReflectableBlock(){

    }

    @Test
    public void testBreakableBlock(){

    }
}
