package pp.tanks.server;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pp.tanks.message.client.ClientReadyMessage;
import pp.tanks.message.client.CreateNewLobbyMessage;
import pp.tanks.message.client.JoinLobbyXMessage;
import pp.tanks.message.client.ReadyMessage;
import pp.tanks.message.client.TankSelectedMessage;
import pp.tanks.message.client.UpdateTankConfigMessage;
import pp.tanks.message.server.ModelMessage;
import pp.tanks.model.item.ItemEnum;
import pp.tanks.model.item.PlayerEnum;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TanksServerTest {
    private ConnectionStub conn1;
    private ConnectionStub conn2;
    private TanksServer server;
    private ServerStub transmitter;

    private final List<Runnable> messages = List.of(
            () -> {
                transmitter.receiveMessage(new ClientReadyMessage(), conn1);//TODO @Georg da stand mal gamemode Singleplayer drin
            },
            () -> {
                transmitter.receiveMessage(new CreateNewLobbyMessage(), conn1);
                transmitter.receiveMessage(new JoinLobbyXMessage(), conn1);
                transmitter.receiveMessage(new UpdateTankConfigMessage(ItemEnum.HEAVY_TURRET, ItemEnum.HEAVY_ARMOR, PlayerEnum.PLAYER1), conn1);
                transmitter.receiveMessage(new UpdateTankConfigMessage(ItemEnum.LIGHT_TURRET, ItemEnum.HEAVY_ARMOR, PlayerEnum.PLAYER2), conn2);
                transmitter.receiveMessage(new TankSelectedMessage(), conn1);
                transmitter.receiveMessage(new TankSelectedMessage(), conn2);
                transmitter.receiveMessage(new ReadyMessage(), conn1);
                transmitter.receiveMessage(new ReadyMessage(), conn2);
            },
            /*
            () -> {
                transmitter.receiveMessage(new PingResponse(), conn1);  //TODO: add correct System.nanoTime()
                transmitter.receiveMessage(new PingResponse(), conn2);  //TODO: add correct System.nanoTime()
            },
            */
            () -> {

            }
    );

    /**
     * Execute the specified number of runnable in {@linkplain #messages}
     *
     * @param n the number of runnable to be executed
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
        transmitter = new ServerStub();
        server = new TanksServer(transmitter);
        transmitter.setServer(server);
        conn1 = new ConnectionStub("P1");
        conn2 = new ConnectionStub("P2");
    }

    @Test
    public void testStart() {
        //execute(1);
        //assertTrue(conn1.getMessages().isEmpty());
        //assertTrue(conn2.getMessages().isEmpty());

        //final ModelMessage model1 = lastMessage(conn1, 1);
        //final ModelMessage model2 = lastMessage(conn2, 1);
    }

    @AfterEach
    public void tearDown() { server.shutdown(); }
}
