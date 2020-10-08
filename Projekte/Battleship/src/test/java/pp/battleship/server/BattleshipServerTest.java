package pp.battleship.server;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pp.battleship.message.client.ClickHarborMessage;
import pp.battleship.message.client.ClickOpponentMapMessage;
import pp.battleship.message.client.ClickOwnMapMessage;
import pp.battleship.message.client.ClientReadyMessage;
import pp.battleship.message.client.ConfirmMessage;
import pp.battleship.message.client.ReadyMessage;
import pp.battleship.message.client.RemoveMessage;
import pp.battleship.message.client.RotateMessage;
import pp.battleship.message.server.ModelMessage;
import pp.battleship.model.ClientState;
import pp.util.IntVec;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BattleshipServerTest {
    private ConnectionStub conn1;
    private ConnectionStub conn2;
    private BattleshipServer server;
    private ServerStub transmitter;

    private final List<Runnable> messages = List.of(
            () -> {
                transmitter.receiveMessage(new ClientReadyMessage(), conn1);
                transmitter.receiveMessage(new ClientReadyMessage(), conn2);
            },
            () -> {
                transmitter.receiveMessage(new ClickHarborMessage(new IntVec(0, 0)), conn1);
                transmitter.receiveMessage(new ClickOwnMapMessage(new IntVec(0, 0)), conn1);
                transmitter.receiveMessage(new ClickHarborMessage(new IntVec(0, 0)), conn2);
                transmitter.receiveMessage(new ClickOwnMapMessage(new IntVec(9, 9)), conn2);
            },
            () -> {
                transmitter.receiveMessage(new ClickOwnMapMessage(new IntVec(0, 0)), conn1);
                transmitter.receiveMessage(new RemoveMessage(), conn1);
            },
            () -> {
                transmitter.receiveMessage(new ClickHarborMessage(new IntVec(0, 1)), conn1);
                transmitter.receiveMessage(new ClickOwnMapMessage(new IntVec(0, 0)), conn1);
                transmitter.receiveMessage(new ClickHarborMessage(new IntVec(0, 0)), conn1);
                transmitter.receiveMessage(new ClickOwnMapMessage(new IntVec(0, 1)), conn1);
            },
            () -> {
                transmitter.receiveMessage(new ConfirmMessage(), conn1);
                transmitter.receiveMessage(new ClickHarborMessage(new IntVec(0, 0)), conn1);
                transmitter.receiveMessage(new RotateMessage(), conn1);
                transmitter.receiveMessage(new ClickOwnMapMessage(new IntVec(5, 5)), conn1);
            },
            () -> {
                transmitter.receiveMessage(new ClickHarborMessage(new IntVec(0, 0)), conn2);
                transmitter.receiveMessage(new ClickOwnMapMessage(new IntVec(0, 0)), conn2);
                transmitter.receiveMessage(new ReadyMessage(), conn2);
                transmitter.receiveMessage(new ClickOwnMapMessage(null), conn2);
                transmitter.receiveMessage(new ClickOpponentMapMessage(null), conn2);
                transmitter.receiveMessage(new ClickHarborMessage(null), conn2);
                transmitter.receiveMessage(new RotateMessage(), conn2);
                transmitter.receiveMessage(new ReadyMessage(), conn2);
            },
            () -> {
                transmitter.receiveMessage(new ReadyMessage(), conn1);
                transmitter.receiveMessage(new ClickOpponentMapMessage(new IntVec(5, 5)), conn1);
            },
            () -> transmitter.receiveMessage(new ClickOpponentMapMessage(new IntVec(5, 5)), conn2),
            () -> transmitter.receiveMessage(new ClickOpponentMapMessage(new IntVec(5, 6)), conn2),
            () -> transmitter.receiveMessage(new ClickOpponentMapMessage(new IntVec(0, 0)), conn2)
    );

    /**
     * Execute the specified number of runnables in {@linkplain #messages}
     *
     * @param n the number of runnables to be executed
     */
    private void execute(int n) {
        messages.subList(0, n).forEach(Runnable::run);
    }

    /**
     * Checks whether the specified connection received the specified number of messages and returns the last one.
     *
     * @param conn the connection
     * @param n    the required number of messages
     */
    private ModelMessage lastMessage(ConnectionStub conn, int n) {
        assertEquals(n, conn.getMessages().size());
        return (ModelMessage) conn.getMessages().get(n - 1);
    }

    @BeforeEach
    public void setUp() {
        Config config = new Config(10, 10, 6, 10, new int[]{1, 1});
        transmitter = new ServerStub();
        server = new BattleshipServer(config, transmitter);
        transmitter.setServer(server);
        conn1 = new ConnectionStub("P1");
        conn2 = new ConnectionStub("P2");
    }

    @Test
    public void testStart() {
        assertTrue(conn1.getMessages().isEmpty());
        assertTrue(conn2.getMessages().isEmpty());
        execute(1);

        final ModelMessage model1 = lastMessage(conn1, 1);
        assertEquals(ClientState.SELECT_SHIP, model1.state);
        assertTrue(model1.ownMap.getShips().isEmpty());
        assertTrue(model1.opponentMap.getShips().isEmpty());
        assertEquals(2, model1.harbor.getShips().size());
        assertNull(model1.ownMap.getPreview());

        final ModelMessage model2 = lastMessage(conn2, 1);
        assertEquals(ClientState.SELECT_SHIP, model2.state);
        assertTrue(model2.ownMap.getShips().isEmpty());
        assertTrue(model2.opponentMap.getShips().isEmpty());
        assertEquals(2, model2.harbor.getShips().size());
        assertNull(model2.ownMap.getPreview());
    }

    @Test
    public void testShipPlacedCorrectly() {
        assertTrue(conn1.getMessages().isEmpty());
        assertTrue(conn2.getMessages().isEmpty());
        execute(2);

        final ModelMessage model1 = lastMessage(conn1, 3);
        assertEquals(1, model1.ownMap.getShips().size());
        assertTrue(model1.opponentMap.getShips().isEmpty());
        assertEquals(1, model1.harbor.getShips().size());
        assertNull(model1.ownMap.getPreview());
        assertEquals(model1.ownMap.findShipAt(new IntVec(0, 0)), model1.ownMap.getShips().get(0));
        assertNull(model1.ownMap.findShipAt(new IntVec(1, 0)));
        assertNull(model1.ownMap.findShipAt(new IntVec(0, 1)));

        final ModelMessage model2 = lastMessage(conn2, 3);
        assertEquals(1, model2.ownMap.getShips().size());
        assertTrue(model2.opponentMap.getShips().isEmpty());
        assertEquals(1, model2.harbor.getShips().size());
        assertNull(model2.ownMap.getPreview());
        assertEquals(model2.ownMap.findShipAt(new IntVec(9, 9)), model2.ownMap.getShips().get(0));
        assertNull(model2.ownMap.findShipAt(new IntVec(10, 9)));
        assertNull(model2.ownMap.findShipAt(new IntVec(9, 10)));
    }

    @Test
    public void testRemove() {
        assertTrue(conn1.getMessages().isEmpty());
        assertTrue(conn2.getMessages().isEmpty());
        execute(3);

        final ModelMessage model1 = lastMessage(conn1, 5);
        assertEquals(0, model1.ownMap.getShips().size());
        assertTrue(model1.opponentMap.getShips().isEmpty());
        assertEquals(2, model1.harbor.getShips().size());
        assertNull(model1.ownMap.getPreview());
    }

    @Test
    public void testTooClose() {
        assertTrue(conn1.getMessages().isEmpty());
        assertTrue(conn2.getMessages().isEmpty());
        execute(4);

        final ModelMessage model1 = lastMessage(conn1, 9);
        assertEquals(1, model1.ownMap.getShips().size());
        assertTrue(model1.opponentMap.getShips().isEmpty());
        assertEquals(1, model1.harbor.getShips().size());
        assertNotNull(model1.ownMap.getPreview());
    }

    @Test
    public void testRotate() {
        assertTrue(conn1.getMessages().isEmpty());
        assertTrue(conn2.getMessages().isEmpty());
        execute(5);

        final ModelMessage model1 = lastMessage(conn1, 13);
        assertEquals(2, model1.ownMap.getShips().size());
        assertTrue(model1.opponentMap.getShips().isEmpty());
        assertEquals(0, model1.harbor.getShips().size());
        assertNull(model1.ownMap.getPreview());
        assertEquals(model1.ownMap.findShipAt(new IntVec(5, 5)), model1.ownMap.getShips().get(1));
        assertEquals(model1.ownMap.findShipAt(new IntVec(5, 6)), model1.ownMap.getShips().get(1));
    }

    @Test
    public void testReady() {
        assertTrue(conn1.getMessages().isEmpty());
        assertTrue(conn2.getMessages().isEmpty());
        execute(6);

        final ModelMessage model2 = lastMessage(conn2, 6);
        assertEquals(2, model2.ownMap.getShips().size());
        assertTrue(model2.opponentMap.getShips().isEmpty());
        assertEquals(0, model2.harbor.getShips().size());
        assertNull(model2.ownMap.getPreview());
        assertEquals(model2.ownMap.findShipAt(new IntVec(9, 9)), model2.ownMap.getShips().get(0));
        assertEquals(model2.ownMap.findShipAt(new IntVec(0, 0)), model2.ownMap.getShips().get(1));
        assertEquals(model2.ownMap.findShipAt(new IntVec(1, 0)), model2.ownMap.getShips().get(1));
        assertEquals(ClientState.WAIT, model2.state);
    }

    @Test
    public void testPlayer1Miss() {
        assertTrue(conn1.getMessages().isEmpty());
        assertTrue(conn2.getMessages().isEmpty());
        execute(7);

        final ModelMessage model1 = lastMessage(conn1, 16);
        assertEquals(ClientState.WAIT, model1.state);
        assertEquals(5, model1.opponentMap.getShots().get(0).x);
        assertEquals(5, model1.opponentMap.getShots().get(0).y);
        assertFalse(model1.opponentMap.getShots().get(0).hit);
        assertTrue(model1.opponentMap.getShips().isEmpty());

        final ModelMessage model2 = lastMessage(conn2, 8);
        assertEquals(ClientState.SHOOT, model2.state);
        assertEquals(5, model2.ownMap.getShots().get(0).x);
        assertEquals(5, model2.ownMap.getShots().get(0).y);
        assertFalse(model2.ownMap.getShots().get(0).hit);
        assertEquals(2, model2.ownMap.getShips().size());
    }

    @Test
    public void testPlayer2Hit() {
        assertTrue(conn1.getMessages().isEmpty());
        assertTrue(conn2.getMessages().isEmpty());
        execute(8);

        final ModelMessage model1 = lastMessage(conn1, 17);
        assertEquals(ClientState.WAIT, model1.state);
        assertEquals(5, model1.ownMap.getShots().get(0).x);
        assertEquals(5, model1.ownMap.getShots().get(0).y);
        assertTrue(model1.ownMap.getShots().get(0).hit);

        final ModelMessage model2 = lastMessage(conn2, 9);
        assertEquals(ClientState.SHOOT, model2.state);
        assertEquals(5, model2.opponentMap.getShots().get(0).x);
        assertEquals(5, model2.opponentMap.getShots().get(0).y);
        assertTrue(model2.opponentMap.getShots().get(0).hit);
        assertEquals(0, model2.opponentMap.getShips().size());
    }

    @Test
    public void testPlayer2Destroys() {
        assertTrue(conn1.getMessages().isEmpty());
        assertTrue(conn2.getMessages().isEmpty());
        execute(9);

        final ModelMessage model1 = lastMessage(conn1, 18);
        assertEquals(ClientState.WAIT, model1.state);
        assertEquals(5, model1.ownMap.getShots().get(1).x);
        assertEquals(6, model1.ownMap.getShots().get(1).y);
        assertTrue(model1.ownMap.getShots().get(1).hit);
        assertTrue(model1.ownMap.getShips().get(1).isDestroyed());

        final ModelMessage model2 = lastMessage(conn2, 10);
        assertEquals(ClientState.SHOOT, model2.state);
        assertEquals(5, model2.opponentMap.getShots().get(1).x);
        assertEquals(6, model2.opponentMap.getShots().get(1).y);
        assertTrue(model2.opponentMap.getShots().get(1).hit);
        assertEquals(1, model2.opponentMap.getShips().size());
    }

    @Test
    public void testPlayer2Wins() {
        assertTrue(conn1.getMessages().isEmpty());
        assertTrue(conn2.getMessages().isEmpty());
        execute(10);

        final ModelMessage model1 = lastMessage(conn1, 19);
        assertEquals(ClientState.LOST, model1.state);
        assertEquals(0, model1.ownMap.getShots().get(2).x);
        assertEquals(0, model1.ownMap.getShots().get(2).y);
        assertTrue(model1.ownMap.getShots().get(2).hit);
        assertEquals(2, model1.opponentMap.getShips().size());
        assertTrue(model1.ownMap.getShips().get(0).isDestroyed());
        assertTrue(model1.ownMap.getShips().get(1).isDestroyed());

        final ModelMessage model2 = lastMessage(conn2, 11);
        assertEquals(ClientState.WON, model2.state);
        assertEquals(0, model2.opponentMap.getShots().get(2).x);
        assertEquals(0, model2.opponentMap.getShots().get(2).y);
        assertTrue(model2.opponentMap.getShots().get(2).hit);
        assertEquals(2, model2.opponentMap.getShips().size());
    }

    @AfterEach
    public void tearDown() {
        server.shutdown();
    }
}
