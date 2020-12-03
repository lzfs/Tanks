package pp.tanks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
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

    @Test
    public void movementTest() {
        DoubleVec startPos = new DoubleVec(6, 6);
        player = new PlayersTank(model, new LightArmor(), new LightTurret(),
                                 new TankData(startPos, 0, 3, MoveDirection.STAY, 0.0, new DoubleVec(0, 1), false)); //TODO: multiply
        model.getTanksMap().addPlayerTank(player);
        assertEquals(startPos, player.getPos());

        player.setMoveDirection(MoveDirection.RIGHT);
        assertEquals(MoveDirection.RIGHT, player.getMoveDir());
        player.setMove(true);
        player.updateMove(2);
        player.setMove(false);
        assertEquals(new DoubleVec(11, 6), player.getPos());

        player.setMoveDirection(MoveDirection.LEFT);
        player.setMove(true);
        player.updateMove(2);
        player.setMove(false);
        assertEquals(startPos, player.getPos());

        player.setRotation(90);
        player.setMoveDirection(MoveDirection.DOWN);
        player.setMove(true);
        player.updateMove(2);
        player.setMove(false);
        assertEquals(new DoubleVec(6, 11), player.getPos());

        player.setMoveDirection(MoveDirection.UP);
        player.setMove(true);
        player.updateMove(2);
        player.setMove(false);
        assertEquals(startPos, player.getPos());

        player.setRotation(45);
        player.setMoveDirection(MoveDirection.RIGHT_DOWN);
        player.setMove(true);
        player.updateMove(2);
        player.setMove(false);
        assertEquals(new DoubleVec(11, 11), player.getPos());

        player.setMoveDirection(MoveDirection.LEFT_UP);
        player.setMove(true);
        player.updateMove(2);
        player.setMove(false);
        assertEquals(startPos, player.getPos());

        player.setRotation(135);
        player.setMoveDirection(MoveDirection.LEFT_DOWN);
        player.setMove(true);
        player.updateMove(2);
        player.setMove(false);
        assertEquals(new DoubleVec(1, 11), player.getPos());

        player.setMoveDirection(MoveDirection.RIGHT_UP);
        player.setMove(true);
        player.updateMove(2);
        player.setMove(false);
        assertEquals(startPos, player.getPos());
    }

    @Test
    public void lightTurretTest() {
        long time;
        player = new PlayersTank(model, new HeavyArmor(), new LightTurret(),
                                 new TankData(new DoubleVec(3, 6), 0, 100, MoveDirection.STAY, 0.0, new DoubleVec(0, 1), false)); //TODO: multiply
        model.getTanksMap().addPlayerTank(player);
        assertEquals(5, model.getTanksMap().getAllTanks().get(0).getTurret().getMagSize());
        assertTrue(model.getTanksMap().getAllTanks().get(0).getTurret().canShoot());
        assertEquals(0, model.getTanksMap().getProjectiles().size());

        player.shoot(new DoubleVec(10, 6));
        assertFalse(model.getTanksMap().getAddedProjectiles().isEmpty());

        time = System.nanoTime();
        Projectile firstShot = model.getTanksMap().getAddedProjectiles().get(1001);
        model.getTanksMap().getHashProjectiles().put(firstShot.getData().id, firstShot);
        DataTimeItem<ProjectileData> tmp = new DataTimeItem<>(firstShot.getData().mkCopy(), time);
        assertEquals(1, model.getTanksMap().getProjectiles().size());

        firstShot.interpolateData(tmp);
        firstShot.interpolateTime(time + 1_000_000_000);
        assertEquals(new DoubleVec(10.01, 6), model.getTanksMap().getProjectiles().get(0).getPos());

        assertEquals(ItemEnum.LIGHT_PROJECTILE, model.getTanksMap().getProjectiles().get(0).getProjectileData().type);
    }

    @Test
    public void normalTurretTest() {
        long time;
        assertEquals(0, model.getTanksMap().getProjectiles().size());
        player = new PlayersTank(model, new HeavyArmor(), new NormalTurret(),
                                 new TankData(new DoubleVec(3, 6), 0, 100, MoveDirection.STAY, 0.0, new DoubleVec(0, 1), false));
        model.getTanksMap().addPlayerTank(player);
        assertEquals(3, model.getTanksMap().getAllTanks().get(0).getTurret().getMagSize());
        assertTrue(model.getTanksMap().getAllTanks().get(0).getTurret().canShoot());
        assertEquals(0, model.getTanksMap().getProjectiles().size());

        player.shoot(new DoubleVec(10, 6));
        assertFalse(model.getTanksMap().getAddedProjectiles().isEmpty());

        time = System.nanoTime();
        Projectile firstShot = model.getTanksMap().getAddedProjectiles().get(1001);
        model.getTanksMap().getHashProjectiles().put(firstShot.getData().id, firstShot);
        DataTimeItem<ProjectileData> tmp = new DataTimeItem<>(firstShot.getData().mkCopy(), time);
        assertEquals(1, model.getTanksMap().getProjectiles().size());

        firstShot.interpolateData(tmp);
        firstShot.interpolateTime(time + 1_000_000_000);
        assertEquals(new DoubleVec(10.01, 6), model.getTanksMap().getProjectiles().get(0).getPos());

        assertEquals(ItemEnum.NORMAL_PROJECTILE, model.getTanksMap().getProjectiles().get(0).getProjectileData().type);
    }

    @Test
    public void heavyTurretTest() {
        long time;
        assertEquals(0, model.getTanksMap().getProjectiles().size());
        player = new PlayersTank(model, new HeavyArmor(), new HeavyTurret(),
                                 new TankData(new DoubleVec(3, 6), 0, 100, MoveDirection.STAY, 0.0, new DoubleVec(0, 1), false));
        model.getTanksMap().addPlayerTank(player);
        assertEquals(1, model.getTanksMap().getAllTanks().get(0).getTurret().getMagSize());
        assertTrue(model.getTanksMap().getAllTanks().get(0).getTurret().canShoot());
        assertEquals(0, model.getTanksMap().getProjectiles().size());

        player.shoot(new DoubleVec(10, 6));
        assertFalse(model.getTanksMap().getAddedProjectiles().isEmpty());

        time = System.nanoTime();
        Projectile firstShot = model.getTanksMap().getAddedProjectiles().get(1001);
        model.getTanksMap().getHashProjectiles().put(firstShot.getData().id, firstShot);
        DataTimeItem<ProjectileData> tmp = new DataTimeItem<>(firstShot.getData().mkCopy(), time);
        assertEquals(1, model.getTanksMap().getProjectiles().size());

        firstShot.interpolateData(tmp);
        firstShot.interpolateTime(time + 1_000_000_000);
        assertEquals(new DoubleVec(8.01, 6), model.getTanksMap().getProjectiles().get(0).getPos());

        assertEquals(ItemEnum.HEAVY_PROJECTILE, model.getTanksMap().getProjectiles().get(0).getProjectileData().type);
    }

    @Test
    public void collisionBlockTest() {
        player = new PlayersTank(model, new HeavyArmor(), new HeavyTurret(),
                                 new TankData(new DoubleVec(4.5, 5), 0, 100, MoveDirection.STAY, 0.0, new DoubleVec(0, 1), false));
        model.getTanksMap().addPlayerTank(player);
        assertFalse(player.collide(player.getPos()));
        UnbreakableBlock uBlock = new UnbreakableBlock(model, new Data(new DoubleVec(5, 5), 10, false));
        model.getTanksMap().addUnbreakableBlock(uBlock);
        assertTrue(player.collide(player.getPos()));
    }

    @Test
    public void collisionEnemyTest() {
        player = new PlayersTank(model, new HeavyArmor(), new HeavyTurret(),
                                 new TankData(new DoubleVec(4.5, 5), 0, 100, MoveDirection.STAY, 0.0, new DoubleVec(0, 1), false));
        model.getTanksMap().addPlayerTank(player);
        assertFalse(player.collide(player.getPos()));
        COMEnemy enemy = new ArmoredPersonnelCarrier(model,
                                                     new TankData(new DoubleVec(5, 5), 1, 100, MoveDirection.STAY, 0.0, new DoubleVec(0, 1), false));
        model.getTanksMap().addCOMTank(enemy);
        assertTrue(player.collide(player.getPos()));
    }

    @Test
    public void tankDestroyed() {
        player = new PlayersTank(model, new HeavyArmor(), new HeavyTurret(),
                                 new TankData(new DoubleVec(4.5, 5), 0, 100, MoveDirection.STAY, 0.0, new DoubleVec(0, 1), false));
        model.getTanksMap().addPlayerTank(player);
        assertEquals(player, model.getTanksMap().getAllTanks().get(0));
        player.processDamage(100);
        assertTrue(player.getData().isDestroyed());
    }

    @Test
    public void enemyDestroyed() {
        COMEnemy enemy = new ArmoredPersonnelCarrier(model,
                                                     new TankData(new DoubleVec(5, 5), 1, 100, MoveDirection.STAY, 0.0, new DoubleVec(0, 1), false));
        model.getTanksMap().addCOMTank(enemy);
        assertEquals(enemy, model.getTanksMap().getAllTanks().get(0));
        enemy.processDamage(100);
        assertTrue(enemy.getData().isDestroyed());
    }

    @Test
    public void testArmorWeight() {
        PlayersTank heavy = new PlayersTank(model, new HeavyArmor(), new LightTurret(),
                                            new TankData(new DoubleVec(3, 4), 0, 100, MoveDirection.STAY, 0.0, new DoubleVec(0, 1), false));
        PlayersTank normal = new PlayersTank(model, new NormalArmor(), new LightTurret(),
                                             new TankData(new DoubleVec(3, 6), 0, 100, MoveDirection.STAY, 0.0, new DoubleVec(0, 1), false));
        PlayersTank light = new PlayersTank(model, new LightArmor(), new LightTurret(),
                                            new TankData(new DoubleVec(3, 8), 0, 100, MoveDirection.STAY, 0.0, new DoubleVec(0, 1), false));
        model.getTanksMap().addPlayerTank(heavy);
        model.getTanksMap().addPlayerTank(normal);
        model.getTanksMap().addPlayerTank(light);

        heavy.setMoveDirection(MoveDirection.RIGHT);
        heavy.setMove(true);
        heavy.updateMove(2);
        heavy.setMove(false);
        assertEquals(new DoubleVec(5,4), heavy.getPos());

        normal.setMoveDirection(MoveDirection.RIGHT);
        normal.setMove(true);
        normal.updateMove(6.5);
        normal.setMove(false);
        assertEquals(new DoubleVec(15.5,6), normal.getPos());

        light.setMoveDirection(MoveDirection.RIGHT);
        light.setMove(true);
        light.updateMove(2);
        light.setMove(false);
        assertEquals(new DoubleVec(8,8), light.getPos());
    }

    @Test
    public void testTurretWeight(){
        PlayersTank heavy = new PlayersTank(model, new LightArmor(), new HeavyTurret(),
                                            new TankData(new DoubleVec(3, 4), 0, 100, MoveDirection.STAY, 0.0, new DoubleVec(0, 1), false));
        PlayersTank normal = new PlayersTank(model, new LightArmor(), new NormalTurret(),
                                             new TankData(new DoubleVec(3, 6), 0, 100, MoveDirection.STAY, 0.0, new DoubleVec(0, 1), false));
        PlayersTank light = new PlayersTank(model, new LightArmor(), new LightTurret(),
                                            new TankData(new DoubleVec(3, 8), 0, 100, MoveDirection.STAY, 0.0, new DoubleVec(0, 1), false));
        model.getTanksMap().addPlayerTank(heavy);
        model.getTanksMap().addPlayerTank(normal);
        model.getTanksMap().addPlayerTank(light);

        heavy.setMoveDirection(MoveDirection.RIGHT);
        heavy.setMove(true);
        heavy.updateMove(2);
        heavy.setMove(false);
        assertEquals(new DoubleVec(5.5,4), heavy.getPos());

        normal.setMoveDirection(MoveDirection.RIGHT);
        normal.setMove(true);
        normal.updateMove(9);
        normal.setMove(false);
        assertEquals(new DoubleVec(18,6), normal.getPos());

        light.setMoveDirection(MoveDirection.RIGHT);
        light.setMove(true);
        light.updateMove(2);
        light.setMove(false);
        assertEquals(new DoubleVec(8,8), light.getPos());
    }
}
