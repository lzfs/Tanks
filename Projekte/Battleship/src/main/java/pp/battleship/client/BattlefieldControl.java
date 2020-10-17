package pp.battleship.client;

import pp.battleship.Resources;
import pp.battleship.message.client.ClickHarborMessage;
import pp.battleship.message.client.ClickOpponentMapMessage;
import pp.battleship.message.client.ClickOwnMapMessage;
import pp.battleship.message.client.ClientMessage;
import pp.battleship.message.client.ConfirmMessage;
import pp.battleship.message.client.ReadyMessage;
import pp.battleship.message.client.RemoveMessage;
import pp.battleship.message.client.RotateMessage;
import pp.battleship.message.server.ModelMessage;
import pp.battleship.model.ClientState;
import pp.util.IntVec;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

/**
 * A class that contains the full functionality for the client.
 */
class BattlefieldControl extends GridPane {
    private static final Logger LOGGER = Logger.getLogger(BattlefieldControl.class.getName());
    private static final String BATTLEFIELD_CONTROL_FXML = "BattlefieldControl.fxml"; //NON-NLS
    private static final Set<ClientState> SETUP_STATES = Set.of(ClientState.SELECT_SHIP,
                                                                ClientState.PLACE_SHIP,
                                                                ClientState.ALL_PLACED);

    private final BattleshipApp app;
    private final MapView ownView;
    private final MapView opponentView;
    private final MapView harborView;

    private ModelMessage model;

    @FXML
    private Canvas ownMap;
    @FXML
    private Canvas opponentMap;
    @FXML
    private Canvas harbor;
    @FXML
    private Button rotateButton;
    @FXML
    private Button readyButton;
    @FXML
    private Button removeButton;
    @FXML
    private Label infoText;

    /**
     * Creates a new BattlefieldControl.
     *
     * @param app   the App in which this class is used
     * @param model the model to be displayed
     */
    BattlefieldControl(BattleshipApp app, ModelMessage model) {
        this.app = app;
        final URL location = getClass().getResource(BATTLEFIELD_CONTROL_FXML);
        LOGGER.info(BATTLEFIELD_CONTROL_FXML + " -> " + location); //NON-NLS
        FXMLLoader fxmlLoader = new FXMLLoader(location, Resources.RESOURCE_BUNDLE);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        // initialize view
        ownView = new MapView(ownMap, model.ownMap.getWidth(), model.ownMap.getHeight());
        opponentView = new MapView(opponentMap, model.opponentMap.getWidth(), model.opponentMap.getHeight());
        harborView = new MapView(harbor, model.harbor.getWidth(), model.harbor.getHeight());
        ownView.setShowGrid(true);
        opponentView.setShowGrid(true);
        harborView.setShowGrid(false);

        // register mouse handlers
        ownMap.addEventHandler(MouseEvent.MOUSE_CLICKED, this::ownMapClicked);
        ownMap.addEventHandler(MouseEvent.MOUSE_MOVED, this::ownMapMouseMoved);
        opponentMap.addEventHandler(MouseEvent.MOUSE_CLICKED, this::opponentMapClicked);
        harbor.addEventHandler(MouseEvent.MOUSE_CLICKED, this::harborClicked);

        setModel(model);
    }

    /**
     * Sends out a RemoveMessage.
     */
    @FXML
    private void remove() {
        final ClientMessage message = new RemoveMessage();
        app.send(message);
        LOGGER.info("RemoveMessage sent"); //NON-NLS
    }

    /**
     * Sends out a RotateMessage.
     */
    @FXML
    private void rotate() {
        app.send(new RotateMessage());
        LOGGER.info("RotateMessage sent"); //NON-NLS
    }

    /**
     * Sends out a ReadyMessage.
     */
    @FXML
    private void ready() {
        app.send(new ReadyMessage());
        LOGGER.info("ReadyMessage sent"); //NON-NLS
    }

    /**
     * Updates all views.
     */
    private void updateView() {
        ownView.update();
        opponentView.update();
        harborView.update();
    }

    /**
     * Sets an infotext.
     *
     * @param msg the text to b set
     */
    void setInfoText(String msg) {
        infoText.setText(msg);
    }

    /**
     * Sets a new model and displays it.
     *
     * @param model the model to be set.
     */
    void setModel(ModelMessage model) {
        this.model = model;
        setInfoText(model.infoText);
        readyButton.setVisible(model.state == ClientState.ALL_PLACED);
        rotateButton.setVisible(model.state == ClientState.PLACE_SHIP);
        removeButton.setVisible(model.state == ClientState.PLACE_SHIP);
        ownView.setMap(model.ownMap);
        opponentView.setMap(model.opponentMap);
        harborView.setMap(model.harbor);
        updateView();
        if (model.state == ClientState.INVALID_PLACEMENT) {
            final Alert confirmation = new Alert(AlertType.INFORMATION);
            confirmation.setContentText(Resources.getString("invalid.ship.placement"));
            confirmation.initOwner(getScene().getWindow());
            confirmation.showAndWait();
            app.send(new ConfirmMessage());
        }
        if (model.state == ClientState.LOST || model.state == ClientState.WON) {
            final Alert confirmation = new Alert(AlertType.CONFIRMATION);
            confirmation.setContentText("Möchtest du ein Revenge");
            confirmation.initOwner(app.getStage());
            final Optional<ButtonType> result = confirmation.showAndWait();
            if (result.orElse(ButtonType.NO) == ButtonType.OK) {
                app.newGame();
            }
            else app.toLobby();
        }
    }

    /**
     * Handles a click in the harbor.
     *
     * @param e the MouseEvent to be handled
     */
    private void harborClicked(MouseEvent e) {
        final IntVec c = harborView.viewToModel(e.getX(), e.getY());
        LOGGER.finer(() -> String.format("harbor clicked: (%f,%f) = %s", e.getX(), e.getY(), c)); //NON-NLS
        if (SETUP_STATES.contains(model.state)) {
            app.send(new ClickHarborMessage(c));
            LOGGER.info(() -> "Ship in harbor selected at " + c); //NON-NLS
        }
    }

    /**
     * Handles a click in the opponent map.
     *
     * @param e the MouseEvent to be handled
     */
    private void opponentMapClicked(MouseEvent e) {
        final IntVec c = opponentView.viewToModel(e.getX(), e.getY());
        LOGGER.finer(() -> String.format("opponentMap clicked: (%f,%f) = %s", e.getX(), e.getY(), c)); //NON-NLS
        if (model.state == ClientState.SHOOT) {
            app.send(new ClickOpponentMapMessage(c));
            LOGGER.info(() -> "shot at " + c); //NON-NLS
        }
    }

    /**
     * Handles a click in the own map.
     *
     * @param e the MouseEvent to be handled
     */
    private void ownMapClicked(MouseEvent e) {
        final IntVec c = ownView.viewToModel(e.getX(), e.getY());
        LOGGER.finer(() -> String.format("ownMap clicked: (%f,%f) = %s", e.getX(), e.getY(), c)); //NON-NLS
        if (SETUP_STATES.contains(model.state)) {
            app.send(new ClickOwnMapMessage(c));
            LOGGER.info(() -> "Ship placed at " + c); //NON-NLS
        }
    }

    /**
     * Handles mouse movement over the own map.
     *
     * @param e the MouseEvent to be handled
     */
    private void ownMapMouseMoved(MouseEvent e) {
        final IntVec c = ownView.viewToModel(e.getX(), e.getY());
        LOGGER.finer(() -> String.format("mouse moved in ownMap: (%f,%f) = %s", e.getX(), e.getY(), c)); //NON-NLS
        if (SETUP_STATES.contains(model.state) && model.ownMap.getPreview() != null) {
            model.ownMap.getPreview().setPos(c);
            LOGGER.fine(() -> "preview moved to " + c); //NON-NLS
            updateView();
        }
    }

    public ModelMessage getModel() {
        return model;
    }
}
