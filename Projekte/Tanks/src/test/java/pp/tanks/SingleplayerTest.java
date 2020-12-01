package pp.tanks;

import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import pp.tanks.client.TanksApp;
import pp.tanks.controller.Engine;
import pp.tanks.message.data.Data;
import pp.tanks.message.data.DataTimeItem;
import pp.tanks.message.data.ProjectileData;
import pp.tanks.message.data.TankData;
import pp.tanks.model.Model;
import pp.tanks.model.TanksMap;
import pp.tanks.model.item.*;
import pp.util.DoubleVec;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

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

    @Disabled
    public void movementTest() {
        player = new PlayersTank(model, 0.3, new HeavyArmor(), new LightTurret(),
                new TankData(new DoubleVec(3, 6), 0, 100, MoveDirection.STAY, 0.0, new DoubleVec(0, 1), false)); //TODO: multiply
        model.getTanksMap().addPlayerTank(player);
        assertEquals(new DoubleVec(3, 6), player.getPos());
        player.setMoveDirection(MoveDirection.DOWN);
        assertEquals(MoveDirection.DOWN, player.getMoveDir());
        player.setMove(true);
        player.updateMove(5);
        //assertEquals(new DoubleVec(3,11), player.getPos());
    }

    @Test
    public void lightTurretTest() {  //TODO: unvollständig
        long time;
        player = new PlayersTank(model, 0.3, new HeavyArmor(), new LightTurret(),
                new TankData(new DoubleVec(3, 6), 0, 100, MoveDirection.STAY, 0.0, new DoubleVec(0, 1), false)); //TODO: multiply
        model.getTanksMap().addPlayerTank(player);
        assertEquals(0, model.getTanksMap().getProjectiles().size());
        player.shoot(new DoubleVec(10, 6));
        assertFalse(model.getTanksMap().getAddedProjectiles().isEmpty());
        time = System.nanoTime();
        Projectile firstShot = model.getTanksMap().getAddedProjectiles().get(1001);
        model.getTanksMap().getHashProjectiles().put(firstShot.getData().id, firstShot);
        DataTimeItem<ProjectileData> tmp = new DataTimeItem<>(firstShot.getData().mkCopy(), time);
        assertEquals(1, model.getTanksMap().getProjectiles().size());
        firstShot.interpolateData(tmp);
        firstShot.interpolateTime(time + 1_100_000_000);
        assertEquals(new DoubleVec(9.51, 6), model.getTanksMap().getProjectiles().get(0).getPos());
    }

    @Test
    public void normalTurretTest() { //TODO: unvollständig
        long time;
        assertEquals(0, model.getTanksMap().getProjectiles().size());
        player = new PlayersTank(model, 0.3, new HeavyArmor(), new NormalTurret(),
                new TankData(new DoubleVec(3, 6), 0, 100, MoveDirection.STAY, 0.0, new DoubleVec(0, 1), false));
        model.getTanksMap().addPlayerTank(player);
        assertEquals(0, model.getTanksMap().getProjectiles().size());
        player.shoot(new DoubleVec(10, 6));
        assertFalse(model.getTanksMap().getAddedProjectiles().isEmpty());
        time = System.nanoTime();
        Projectile firstShot = model.getTanksMap().getAddedProjectiles().get(1001);
        model.getTanksMap().getHashProjectiles().put(firstShot.getData().id, firstShot);
        DataTimeItem<ProjectileData> tmp = new DataTimeItem<>(firstShot.getData().mkCopy(), time);
        assertEquals(1, model.getTanksMap().getProjectiles().size());
        firstShot.interpolateData(tmp);
        firstShot.interpolateTime(time + 1_100_000_000);
        assertEquals(new DoubleVec(9.51, 6), model.getTanksMap().getProjectiles().get(0).getPos());
    }

    @Test
    public void HeavyTurretTest() {  //TODO: unvollständig
        long time;
        assertEquals(0, model.getTanksMap().getProjectiles().size());
        player = new PlayersTank(model, 0.3, new HeavyArmor(), new HeavyTurret(),
                new TankData(new DoubleVec(3, 6), 0, 100, MoveDirection.STAY, 0.0, new DoubleVec(0, 1), false));
        model.getTanksMap().addPlayerTank(player);
        assertEquals(0, model.getTanksMap().getProjectiles().size());
        player.shoot(new DoubleVec(10, 6));
        assertFalse(model.getTanksMap().getAddedProjectiles().isEmpty());
        time = System.nanoTime();
        Projectile firstShot = model.getTanksMap().getAddedProjectiles().get(1001);
        model.getTanksMap().getHashProjectiles().put(firstShot.getData().id, firstShot);
        DataTimeItem<ProjectileData> tmp = new DataTimeItem<>(firstShot.getData().mkCopy(), time);
        assertEquals(1, model.getTanksMap().getProjectiles().size());
        firstShot.interpolateData(tmp);
        firstShot.interpolateTime(time + 1_100_000_000);
        assertEquals(new DoubleVec(9.51, 6), model.getTanksMap().getProjectiles().get(0).getPos());
    }
}
