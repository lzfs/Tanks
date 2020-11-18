package pp.tanks.client;

import pp.tanks.message.client.UpdateTankConfigMessage;
import pp.tanks.message.server.ServerTankUpdateMessage;
import pp.tanks.model.item.ItemEnum;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Controller for visual tests of the server team
 *
 * !!WIRD AUCH NICHT KOMMENTIERT!!
 */

public class MiniController extends GridPane {
    private static final String MENU_CONTROL_FXML = "MiniController.fxml"; //NON-NLS
    private final TanksApp app;
    private List<ItemEnum> turretList = new ArrayList(Arrays.asList(ItemEnum.LIGHT_TURRET, ItemEnum.NORMAL_TURRET, ItemEnum.HEAVY_TURRET));
    private List<ItemEnum> armorList = new ArrayList(Arrays.asList(ItemEnum.LIGHT_ARMOR, ItemEnum.NORMAL_ARMOR, ItemEnum.HEAVY_ARMOR));
    private int armorCount = 0;
    private int turretCount = 0;
    private ItemEnum currentTurret = ItemEnum.LIGHT_TURRET;
    private ItemEnum currentArmor = ItemEnum.LIGHT_ARMOR;
    private boolean playerConnected = false;

    @FXML
    private Button changeTurret;

    @FXML
    private Button changeArmor;

    @FXML
    private Button ready;

    @FXML
    private Text waitingfor;

    @FXML
    private Text ownTurret;

    @FXML
    private Text ownArmor;

    @FXML
    private Text enemyTurret;

    @FXML
    private Text enemyArmor;

    public MiniController(TanksApp app) {
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

        ready.setDisable(true);
    }

    @FXML
    private void changeTurret() {
        turretCount++;
        if (turretCount > 2) {
            turretCount = 0;
        }

        ownTurret.setText(String.valueOf(turretList.get(turretCount)));
        currentTurret = turretList.get(turretCount);
        app.getConnection().send(new UpdateTankConfigMessage(currentTurret, currentArmor, app.getPlayer()));
    }

    @FXML
    private void changeArmor() {
        armorCount++;
        if (armorCount > 2) {
            armorCount = 0;
        }
        ownArmor.setText(String.valueOf(armorList.get(armorCount)));
        currentArmor = armorList.get(armorCount);
        app.getConnection().send(new UpdateTankConfigMessage(currentTurret, currentArmor, app.getPlayer()));
    }

    @FXML
    private void ready() {
        System.out.println("3");
    }

    public void playerConnected() {
        if (playerConnected) return;
        waitingfor.setText("");
        playerConnected = true;
        ready.setDisable(false);
    }

    public void serverUpdate(ServerTankUpdateMessage msg) {
        if (msg.turret == null) return;
        Platform.runLater(() -> {
            enemyTurret.setText(String.valueOf(msg.turret));
            enemyArmor.setText(String.valueOf(msg.armor));
        });
    }
}
