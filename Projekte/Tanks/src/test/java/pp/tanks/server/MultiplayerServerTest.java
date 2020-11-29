package pp.tanks.server;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pp.tanks.message.client.ClientReadyMessage;
import pp.tanks.message.client.StartGameMessage;
import pp.tanks.message.client.UpdateTankConfigMessage;
import pp.tanks.message.server.ModelMessage;
import pp.tanks.message.server.ServerTankUpdateMessage;
import pp.tanks.message.server.StartingMultiplayerMessage;
import pp.tanks.message.server.SynchronizeMessage;
import pp.tanks.model.item.ItemEnum;
import pp.tanks.model.item.PlayerEnum;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MultiplayerServerTest {
    private ConnectionStub conn1;
    private ConnectionStub conn2;
    private TanksServer server;
    private ServerStub transmitter;

    private final List<Runnable> messages = List.of(
            () -> {
                transmitter.receiveMessage(new ClientReadyMessage(), conn1);
                transmitter.receiveMessage(new ClientReadyMessage(), conn2);
                transmitter.receiveMessage(new UpdateTankConfigMessage(ItemEnum.NORMAL_TURRET, ItemEnum.LIGHT_ARMOR, PlayerEnum.PLAYER1), conn1);
                transmitter.receiveMessage(new UpdateTankConfigMessage(ItemEnum.HEAVY_TURRET, ItemEnum.LIGHT_ARMOR, PlayerEnum.PLAYER1), conn1);
                transmitter.receiveMessage(new StartGameMessage(ItemEnum.HEAVY_TURRET, ItemEnum.LIGHT_ARMOR, GameMode.MULTIPLAYER, PlayerEnum.PLAYER1), conn1);
                transmitter.receiveMessage(new StartGameMessage(ItemEnum.LIGHT_TURRET, ItemEnum.LIGHT_ARMOR, GameMode.MULTIPLAYER, PlayerEnum.PLAYER2), conn2);
            },
            () -> {

            }
    );

    private void execute(int n) {
        messages.subList(0, n).forEach(Runnable::run);
    }

    private ModelMessage lastModelMessage(ConnectionStub conn, int n) {
        assertEquals(n, conn.getMessages().size());
        return (ModelMessage) conn.getMessages().get(n - 1);
    }

    private ServerTankUpdateMessage lastServerTankUpdateMsg(ConnectionStub conn, int n) {
        assertEquals(n, conn.getMessages().size());
        return (ServerTankUpdateMessage) conn.getMessages().get(n - 1);
    }

    private StartingMultiplayerMessage lastStartingMultiplayerMsg(ConnectionStub conn, int n) {
        assertEquals(n, conn.getMessages().size());
        return (StartingMultiplayerMessage) conn.getMessages().get(n - 1);
    }

    private SynchronizeMessage lastSychronizeMsg(ConnectionStub conn, int n) {
        assertEquals(n, conn.getMessages().size());
        return (SynchronizeMessage) conn.getMessages().get(n - 1);
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
    public void testStart() {
        assertTrue(conn1.getMessages().isEmpty());
        assertTrue(conn2.getMessages().isEmpty());
        execute(1);

    }

    @AfterEach
    public void tearDown() { server.shutdown(); }
}
