package pp.tanks.server;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pp.tanks.message.client.ClientReadyMessage;
import pp.tanks.message.client.StartGameMessage;
import pp.tanks.message.server.ModelMessage;
import pp.tanks.model.item.ItemEnum;
import pp.tanks.model.item.PlayerEnum;
import pp.util.DoubleVec;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SingleplayerServerTest {
    private ConnectionStub conn;
    private TanksServer server;
    private ServerStub transmitter;

    private final List<Runnable> messages = List.of(
            () -> {
                transmitter.receiveMessage(new ClientReadyMessage(GameMode.SINGLEPLAYER), conn);
                transmitter.receiveMessage(new StartGameMessage(ItemEnum.LIGHT_TURRET, ItemEnum.LIGHT_ARMOR, GameMode.TUTORIAL, PlayerEnum.PLAYER1), conn);
            }
    );

    private void execute(int n) {
        messages.subList(0, n).forEach(Runnable::run);
    }

    private ModelMessage lastMessage(ConnectionStub conn, int n) {
        assertEquals(n, conn.getMessages().size());
        return (ModelMessage) conn.getMessages().get(n - 1);
    }

    @BeforeEach
    public void setUp() {
        transmitter = new ServerStub();
        server = new TanksServer(transmitter);
        transmitter.setServer(server);
        conn = new ConnectionStub("P1");
    }

    @Test
    public void testStartTutorial() {
        assertTrue(conn.getMessages().isEmpty());
        execute(1);

        final ModelMessage model = lastMessage(conn, 1);
        assertEquals(new DoubleVec(3, 6), model.tanks.get(0).getPos());
    }

    @AfterEach
    public void tearDown() { server.shutdown(); }
}
