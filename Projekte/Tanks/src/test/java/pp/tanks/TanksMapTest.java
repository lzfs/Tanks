package pp.tanks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pp.tanks.message.data.*;
import pp.tanks.model.ICollisionObserver;
import pp.tanks.model.Model;
import pp.tanks.model.TanksMap;
import pp.tanks.model.item.*;
import pp.util.DoubleVec;

import java.sql.Ref;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

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
        Tank player = new PlayersTank(model, new HeavyArmor(), new HeavyTurret(), new TankData(new DoubleVec(3, 6), 0, 100, MoveDirection.STAY, 0.0, new DoubleVec(1, 1), false));
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
        //model.getTanksMap().getCOMTanks().get(0).processDamage(100); //TODO: I want to destroy the tank by command
        //assertEquals(0, model.getTanksMap().getCOMTanks().size());
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
        UnbreakableBlock uBlock = new UnbreakableBlock(model, new Data(new DoubleVec(1, 1), 5000, false));
        ReflectableBlock rBlock = new ReflectableBlock(model, new Data(new DoubleVec(1, 2), 5001, false));
        model.getTanksMap().addBreakableBlock(bBlock);
        model.getTanksMap().addUnbreakableBlock(uBlock);
        model.getTanksMap().addReflectableBlocks(rBlock);
        assertEquals(3, model.getTanksMap().getBlocks().size());

        assertEquals(1, model.getTanksMap().getBreakableBlocks().size());
        assertEquals(1, model.getTanksMap().getUnbreakableBlocks().size());
        assertEquals(1, model.getTanksMap().getReflectable().size());

        assertEquals(bBlock, model.getTanksMap().getBreakableBlocks().get(0));
        assertEquals(uBlock, model.getTanksMap().getUnbreakableBlocks().get(0));
        assertEquals(rBlock, model.getTanksMap().getReflectable().get(0));
    }

    @Test
    public void loadMap() {
        model.loadMap("map0.xml");
        assertEquals(71, model.getTanksMap().getBlocks().size());
        model.loadMap("map1.xml");
        assertEquals(87, model.getTanksMap().getBlocks().size());
        model.loadMap("map2.xml");
        assertEquals(96, model.getTanksMap().getBlocks().size());
    }

    @Test
    public void gameWonTest() {
        Tank player = new PlayersTank(model, new HeavyArmor(), new HeavyTurret(),
                new TankData(new DoubleVec(3, 6), 0, 100, MoveDirection.STAY,
                        0.0, new DoubleVec(0, 1), false));
        model.getTanksMap().addPlayerTank(player);
        assertTrue(model.gameWon());
    }

    @Test
    public void damageBlockTest() {
        BreakableBlock bBlock = new BreakableBlock(model, new BBData(new DoubleVec(1, 3), 5003, 100, false));
        model.getTanksMap().addBreakableBlock(bBlock);
        assertEquals(1, model.getTanksMap().getBreakableBlocks().size());
        assertEquals(bBlock, model.getTanksMap().getBreakableBlocks().get(0));
        bBlock.processDamage(100);
        assertTrue(bBlock.isDestroyed());
    }

    @Test
    public void bulletRefelct() {
        ReflectableBlock rBlock = new ReflectableBlock(model, new Data(new DoubleVec(10, 6), 5001, false));
        model.getTanksMap().addReflectableBlocks(rBlock);
        LightProjectile lightProjectile = new LightProjectile(model, new ProjectileData(new DoubleVec(9.5, 6), 1001, 2,
                new DoubleVec(10, 6), new DoubleVec(10, 6), ItemEnum.LIGHT_PROJECTILE, false));
        model.getTanksMap().addProjectile(lightProjectile);
        lightProjectile.interpolateData(new DataTimeItem<>(lightProjectile.getData().mkCopy(), System.nanoTime()));
        lightProjectile.setFlag(0);
        lightProjectile.processHits();
        assertEquals(1, lightProjectile.getLatestOp().data.getBounce());
        assertEquals(new DoubleVec(-10, 6), lightProjectile.getDir());
        lightProjectile.setPos(new DoubleVec(9.5, 6));
        lightProjectile.setFlag(0);
        lightProjectile.processHits();
        assertEquals(0, lightProjectile.getLatestOp().data.getBounce());
        assertEquals(new DoubleVec(10, 6), lightProjectile.getDir());

        NormalProjectile normalProjectile = new NormalProjectile(model, new ProjectileData(new DoubleVec(9.5, 6), 1001, 1,
                new DoubleVec(10, 6), new DoubleVec(10, 6), ItemEnum.NORMAL_PROJECTILE, false));
        model.getTanksMap().addProjectile(normalProjectile);
        normalProjectile.interpolateData(new DataTimeItem<>(normalProjectile.getData().mkCopy(), System.nanoTime()));
        normalProjectile.setFlag(0);
        normalProjectile.processHits();
        assertEquals(0, normalProjectile.getLatestOp().data.getBounce());
        assertEquals(new DoubleVec(-10, 6), normalProjectile.getDir());
    }

    @Test
    public void destroyBullet() {
        UnbreakableBlock uBlock = new UnbreakableBlock(model, new Data(new DoubleVec(10, 6), 50, false));
        model.getTanksMap().addUnbreakableBlock(uBlock);

        LightProjectile lightProjectile = new LightProjectile(model, new ProjectileData(new DoubleVec(9.5, 6), 1001, 2,
                new DoubleVec(10, 6), new DoubleVec(10, 6), ItemEnum.LIGHT_PROJECTILE, false));
        model.getTanksMap().addProjectile(lightProjectile);
        lightProjectile.interpolateData(new DataTimeItem<>(lightProjectile.getData().mkCopy(), System.nanoTime()));
        lightProjectile.setFlag(0);
        lightProjectile.processHits();
        assertTrue(lightProjectile.getData().isDestroyed());

        NormalProjectile normalProjectile = new NormalProjectile(model, new ProjectileData(new DoubleVec(9.5, 6), 1001, 1,
                new DoubleVec(10, 6), new DoubleVec(10, 6), ItemEnum.NORMAL_PROJECTILE, false));
        model.getTanksMap().addProjectile(normalProjectile);
        normalProjectile.interpolateData(new DataTimeItem<>(normalProjectile.getData().mkCopy(), System.nanoTime()));
        normalProjectile.setFlag(0);
        normalProjectile.processHits();
        assertTrue(normalProjectile.getData().isDestroyed());

        HeavyProjectile heavyProjectile = new HeavyProjectile(model, new ProjectileData(new DoubleVec(10, 6), 1001, 0,
                new DoubleVec(10, 6), new DoubleVec(10, 6), ItemEnum.HEAVY_PROJECTILE, false));
        heavyProjectile.collision();
        assertTrue(heavyProjectile.getData().isDestroyed());
    }

    @Test
    public void projectileCollision() {
        CollisionObserver observer = new CollisionObserver();
        model.getTanksMap().addObserver(observer);

        NormalProjectile firstProjectile = new NormalProjectile(model, new ProjectileData(new DoubleVec(9.5, 6), 1001, 1,
                new DoubleVec(10, 6), new DoubleVec(10, 6), ItemEnum.NORMAL_PROJECTILE, false));
        NormalProjectile secondProjectile = new NormalProjectile(model, new ProjectileData(new DoubleVec(9.6, 6), 1002, 1,
                new DoubleVec(10, 6), new DoubleVec(10, 6), ItemEnum.NORMAL_PROJECTILE, false));
        NormalProjectile thirdProjectile = new NormalProjectile(model, new ProjectileData(new DoubleVec(10, 8), 1003, 1,
                new DoubleVec(10, 6), new DoubleVec(10, 6), ItemEnum.NORMAL_PROJECTILE, false));
        HeavyProjectile heavyProjectile = new HeavyProjectile(model, new ProjectileData(new DoubleVec(10, 8), 1004, 0,
                new DoubleVec(10, 6), new DoubleVec(10, 6), ItemEnum.HEAVY_PROJECTILE, false));
        model.getTanksMap().getHashProjectiles().put(1001, firstProjectile);
        model.getTanksMap().getHashProjectiles().put(1002, secondProjectile);
        model.getTanksMap().getHashProjectiles().put(1003, thirdProjectile);
        model.getTanksMap().getHashProjectiles().put(1004, heavyProjectile);
        model.update(System.nanoTime());

        firstProjectile.interpolateData(new DataTimeItem<>(firstProjectile.getData().mkCopy(), System.nanoTime()));
        secondProjectile.interpolateData(new DataTimeItem<>(secondProjectile.getData().mkCopy(), System.nanoTime()));
        firstProjectile.setFlag(0);
        firstProjectile.processHits();
        assertTrue(firstProjectile.getData().isDestroyed());
        assertTrue(secondProjectile.getData().isDestroyed());

        thirdProjectile.interpolateData(new DataTimeItem<>(thirdProjectile.getData().mkCopy(), System.nanoTime()));
        heavyProjectile.interpolateData(new DataTimeItem<>(heavyProjectile.getData().mkCopy(), System.nanoTime()));
        thirdProjectile.setFlag(0);
        thirdProjectile.processHits();
        assertFalse(thirdProjectile.getData().isDestroyed());
        assertFalse(heavyProjectile.getData().isDestroyed());
    }

    static class CollisionObserver implements ICollisionObserver {
        @Override
        public void notifyProjTank(Projectile proj, Tank tank, int damage, boolean dest) {
            if (dest) {
                tank.destroy();
            } else {
                tank.processDamage(damage);
            }
            proj.destroy();
        }

        @Override
        public void notifyProjBBlock(Projectile proj, BreakableBlock block, int damage, boolean dest) {
            if (dest) {
                block.destroy();
            } else {
                block.processDamage(damage);
            }
            proj.destroy();
        }

        @Override
        public void notifyProjProj(Projectile proj1, Projectile proj2) {
            proj1.destroy();
            proj2.destroy();
        }
    }

    @Test
    public void testBulletDamage(){

    }
}
