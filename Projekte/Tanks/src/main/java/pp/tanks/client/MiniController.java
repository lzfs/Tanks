package pp.tanks.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import pp.tanks.message.client.MoveMessage;
import pp.tanks.message.client.ShootMessage;
import pp.tanks.message.data.DataTimeItem;
import pp.tanks.message.data.ProjectileData;
import pp.tanks.message.data.TankData;
import pp.util.DoubleVec;

import java.io.IOException;
import java.net.URL;

/**
 * Controller for visual tests of the server team
 */

public class MiniController extends GridPane {
    private static final String MENU_CONTROL_FXML = "MiniController.fxml"; //NON-NLS
    private final TanksApp app;

    @FXML
    private Button move;

    @FXML
    private Button shootBtn;

    MiniController(TanksApp app) {
        this.app = app;
        final URL location = getClass().getResource(MENU_CONTROL_FXML);
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void shoot() {
        ProjectileData d = new ProjectileData(new DoubleVec(2, 3), 0, 0);
        app.getConnection().send(new ShootMessage(new DataTimeItem(d, System.nanoTime() + app.getOffset())));
    }

    @FXML
    private void moveTank() {
        TankData d = new TankData(new DoubleVec(1, 1), 0);
        app.getConnection().send(new MoveMessage(new DataTimeItem(d, System.nanoTime() + app.getOffset())));
    }
}
