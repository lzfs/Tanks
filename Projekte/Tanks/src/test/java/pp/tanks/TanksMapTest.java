package pp.tanks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pp.tanks.message.data.BBData;
import pp.tanks.message.data.Data;
import pp.tanks.message.data.ProjectileData;
import pp.tanks.message.data.TankData;
import pp.tanks.model.Model;
import pp.tanks.model.TanksMap;
import pp.tanks.model.item.BreakableBlock;
import pp.tanks.model.item.HeavyArmor;
import pp.tanks.model.item.HeavyProjectile;
import pp.tanks.model.item.HeavyTurret;
import pp.tanks.model.item.Howitzer;
import pp.tanks.model.item.ItemEnum;
import pp.tanks.model.item.LightProjectile;
import pp.tanks.model.item.MoveDirection;
import pp.tanks.model.item.NormalProjectile;
import pp.tanks.model.item.PlayersTank;
import pp.tanks.model.item.Projectile;
import pp.tanks.model.item.ReflectableBlock;
import pp.tanks.model.item.Tank;
import pp.tanks.model.item.UnbreakableBlock;
import pp.util.DoubleVec;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TanksMapTest {
    public static final int WIDTH = 23;
    public static final int HEIGHT = 13;

    private Model model;
    private TanksMap map;

    @BeforeEach
    public void setUp() {
        model = new Model(new Properties());
        map = new TanksMap(model, WIDTH, HEIGHT);
        model.setTanksMap(map);
    }

    @Test
    public void mapDimensionTest() {
        assertEquals(WIDTH, map.getWidth());
        assertEquals(HEIGHT, map.getHeight());
    }

    @Test
    public void existingPlayer() {
        Tank player = new PlayersTank(model, 0.3, new HeavyArmor(), new HeavyTurret(), new TankData(new DoubleVec(3, 6), 0, 100, MoveDirection.STAY, 0.0, new DoubleVec(1, 1), false));
        model.getTanksMap().addTanks(player);
        assertEquals(1, model.getTanksMap().getAllTanks().size());
        assertEquals(player, model.getTanksMap().getAllTanks().get(0));
    }

    @Test
    public void existingEnemy() {
        Tank howitzer = new Howitzer(model, new TankData(new DoubleVec(20, 6), 1000, 100, MoveDirection.STAY, 0.0, new DoubleVec(1, 1), false));
        model.getTanksMap().addTanks(howitzer);
        assertEquals(1, model.getTanksMap().getCOMTanks().size());
        assertEquals(howitzer, model.getTanksMap().getCOMTanks().get(0));
    }

    @Test
    public void existingProjectile() {
        Projectile heavy = new HeavyProjectile(model, new ProjectileData(new DoubleVec(1, 1), 1234, 0, new DoubleVec(1, 1), new DoubleVec(5, 5), ItemEnum.HEAVY_PROJECTILE, false));
        Projectile normal = new NormalProjectile(model, new ProjectileData(new DoubleVec(2, 2), 1235, 3, new DoubleVec(1, 1), new DoubleVec(6, 6), ItemEnum.NORMAL_PROJECTILE, false));
        Projectile light = new LightProjectile(model, new ProjectileData(new DoubleVec(3, 3), 1236, 5, new DoubleVec(1, 1), new DoubleVec(7, 7), ItemEnum.LIGHT_PROJECTILE, false));
        model.getTanksMap().addProjectile(heavy);
        model.getTanksMap().addProjectile(normal);
        model.getTanksMap().addProjectile(light);
        model.update(System.nanoTime());
        model.setLatestUpdate(System.nanoTime());
        assertEquals(3, model.getTanksMap().getProjectiles().size());
        assertEquals(heavy, model.getTanksMap().getProjectiles().get(0));
        assertEquals(normal, model.getTanksMap().getProjectiles().get(1));
        assertEquals(light, model.getTanksMap().getProjectiles().get(2));
    }

    @Test
    public void existingBlocks() {
        BreakableBlock bBlock = new BreakableBlock(model, new BBData(new DoubleVec(1, 3), 5003, 100, false));
        model.getTanksMap().addBreakableBlock(bBlock);
        model.update(System.nanoTime());
        assertEquals(1, model.getTanksMap().getBreakableBlocks().size());
        assertEquals(bBlock, model.getTanksMap().getBreakableBlocks().get(0));
        model.getTanksMap().getBreakableBlocks().get(0).processDamage(100);
        model.update(System.nanoTime());
        assertEquals(0, model.getTanksMap().getBreakableBlocks().size());
        UnbreakableBlock uBlock = new UnbreakableBlock(model, new Data(new DoubleVec(1, 1), 5000, false));
        ReflectableBlock rBlock = new ReflectableBlock(model, new Data(new DoubleVec(1, 2), 5001, false));
        model.getTanksMap().addUnbreakableBlock(uBlock);
        model.getTanksMap().addReflectableBlocks(rBlock);
        model.update(System.nanoTime());
        assertEquals(2, model.getTanksMap().getBlocks().size());
        assertEquals(1, model.getTanksMap().getUnbreakableBlocks().size());
        assertEquals(1, model.getTanksMap().getReflectable().size());
        assertEquals(1, model.getTanksMap().getReflectable().size());
        assertEquals(uBlock, model.getTanksMap().getBlocks().get(0));
        assertEquals(uBlock, model.getTanksMap().getUnbreakableBlocks().get(0));
        assertEquals(rBlock, model.getTanksMap().getBlocks().get(1));
        assertEquals(rBlock, model.getTanksMap().getReflectable().get(0));
    }
}
