package pp.battleship.server;

import pp.battleship.Resources;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Optional;

public class ServerApp extends Application {

    private Stage stage;
    private ServerMenuControl serverMenuControl;
    private BattleshipServer bs;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * start method for Java FX, sets up the GUI for the Server and is automatically called
     * @param stage provided by Java JX
     */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.serverMenuControl = new ServerMenuControl(this);
        stage.setResizable(false);
        stage.setTitle(Resources.getString("battleships"));
        stage.setOnCloseRequest(this::shutdown);
        stage.setScene(new Scene(serverMenuControl));
        stage.sizeToScene();
        stage.show();
    }

    /**
     * creates the Server with the given Port from the GUI
     */
    void startSever() {
        String[] s = {serverMenuControl.getPort()};
        bs = BattleshipServer.mkBsServer(s);
        serverMenuControl.setCreated();
    }

    /**
     * shuts down the Server after it gets the confirmation from its produced alert
     * @param e WindowEvent from pressing the close button in the GUI
     */
    private void shutdown(WindowEvent e) {
        if (bs == null)
            System.exit(0);
        final Alert confirmation = new Alert(AlertType.CONFIRMATION);
        confirmation.setContentText(Resources.getString("would.you.really.like.to.leave.the.game"));
        confirmation.initOwner(stage);
        final Optional<ButtonType> result = confirmation.showAndWait();
        if (result.orElse(ButtonType.NO) == ButtonType.OK) {
            bs.shutdown();
            System.exit(0);
        }
        // prevent closing
        e.consume();
    }
}
