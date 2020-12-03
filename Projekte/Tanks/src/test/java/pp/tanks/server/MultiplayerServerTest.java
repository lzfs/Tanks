package pp.tanks.server;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pp.network.Server;
import pp.tanks.message.client.ClientReadyMessage;
import pp.tanks.message.client.StartGameMessage;
import pp.tanks.message.client.UpdateTankConfigMessage;
import pp.tanks.message.server.*;
import pp.tanks.model.item.ItemEnum;
import pp.tanks.model.item.PlayerEnum;
import pp.util.DoubleVec;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MultiplayerServerTest {
    private ConnectionStub conn1;
    private ConnectionStub conn2;
    private TanksServer server;
    private ServerStub transmitter;

    private final List<Runnable> messages = List.of(
            () -> { //1
                transmitter.receiveMessage(new ClientReadyMessage(), conn1);
            },
            () -> { //2
                transmitter.receiveMessage(new UpdateTankConfigMessage(ItemEnum.HEAVY_TURRET, ItemEnum.LIGHT_ARMOR, PlayerEnum.PLAYER1), conn1);
                transmitter.receiveMessage(new ClientReadyMessage(), conn2);
            },
            () -> { //3
                transmitter.receiveMessage(new UpdateTankConfigMessage(ItemEnum.LIGHT_TURRET, ItemEnum.NORMAL_ARMOR, PlayerEnum.PLAYER2), conn2);
            }
    );

    private void execute(int n) {
        messages.subList(0, n).forEach(Runnable::run);
    }

    private ModelMessage lastModelMessage(ConnectionStub conn, int n) {
        assertEquals(n, conn.getMessages().size());
        return (ModelMessage) conn.getMessages().get(n - 1);
    }

    @BeforeEach
    public void setUp() {
        transmitter = new ServerStub();
        server = new TanksServer(transmitter);
        transmitter.setServer(server);
        conn1 = new ConnectionStub("P1");
        conn2 = new ConnectionStub("P2");
    }

    @Test
    public void testInitState() {
        assertEquals(server.auto.init, server.auto.getState());
        assertTrue(conn1.getMessages().isEmpty());
        assertTrue(conn2.getMessages().isEmpty());

        execute(1);
        assertEquals(SetPlayerMessage.class, conn1.getMessages().get(0).getClass());
        SetPlayerMessage tmp = (SetPlayerMessage) conn1.getMessages().get(0);
        assertEquals(PlayerEnum.PLAYER1, tmp.player);
        assertEquals(server.auto.waitingFor2Player, server.auto.getState());
    }

    @Test
    public void testWaitingForPlayer2() {
        execute(2);
        assertEquals(SetPlayerMessage.class, conn2.getMessages().get(0).getClass());
        SetPlayerMessage tmp = (SetPlayerMessage) conn2.getMessages().get(0);
        assertEquals(PlayerEnum.PLAYER2, tmp.player);
        assertEquals(server.auto.playerReady, server.auto.getState());
    }

    @Test
    public void testReadyState() {
        execute(3);
        assertEquals(ServerTankUpdateMessage.class, conn1.getMessages().get(1).getClass());
        assertEquals(ServerTankUpdateMessage.class, conn2.getMessages().get(1).getClass());
        ServerTankUpdateMessage tmp1 = (ServerTankUpdateMessage) conn2.getMessages().get(1);
        assertEquals(ItemEnum.HEAVY_TURRET, tmp1.turret);
        assertEquals(ItemEnum.LIGHT_ARMOR, tmp1.armor);

        assertEquals(ServerTankUpdateMessage.class, conn1.getMessages().get(2).getClass());
        ServerTankUpdateMessage tmp2 = (ServerTankUpdateMessage) conn1.getMessages().get(2);
        assertEquals(ItemEnum.NORMAL_ARMOR, tmp2.armor);
        assertEquals(ItemEnum.LIGHT_TURRET, tmp2.turret);

        transmitter.receiveMessage(new StartGameMessage(ItemEnum.HEAVY_TURRET, ItemEnum.LIGHT_ARMOR, GameMode.MULTIPLAYER, PlayerEnum.PLAYER1), conn1);
        assertEquals(server.auto.playerReady, server.auto.getState());
        transmitter.receiveMessage(new StartGameMessage(ItemEnum.LIGHT_TURRET, ItemEnum.NORMAL_ARMOR, GameMode.MULTIPLAYER, PlayerEnum.PLAYER2), conn2);
        assertEquals(StartingMultiplayerMessage.class, conn1.getMessages().get(3).getClass());
        StartingMultiplayerMessage tmp3 = (StartingMultiplayerMessage) conn1.getMessages().get(3);
        assertEquals(ItemEnum.LIGHT_TURRET, tmp3.enemyTurret);
        assertEquals(ItemEnum.NORMAL_ARMOR, tmp3.enemyArmor);
        assertEquals(new DoubleVec(3, 6), tmp3.playerTank.getPos());
        assertEquals(new DoubleVec(20, 6), tmp3.enemyTank.getPos());

        assertEquals(StartingMultiplayerMessage.class, conn2.getMessages().get(2).getClass());
        StartingMultiplayerMessage tmp4 = (StartingMultiplayerMessage) conn2.getMessages().get(2);
        assertEquals(ItemEnum.HEAVY_TURRET, tmp4.enemyTurret);
        assertEquals(ItemEnum.LIGHT_ARMOR, tmp4.enemyArmor);
        assertEquals(new DoubleVec(20, 6), tmp4.playerTank.getPos());
        assertEquals(new DoubleVec(3, 6), tmp4.enemyTank.getPos());

        assertEquals(server.auto.synchronize, server.auto.getState());
    }

    @AfterEach
    public void tearDown() {
        server.shutdown();
    }
}
