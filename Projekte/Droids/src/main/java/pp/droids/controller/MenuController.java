package pp.droids.controller;

import pp.droids.model.DroidsGameModel;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The controller realizing the menu state.
 */
class MenuController extends Controller {
    private static final Logger LOGGER = Logger.getLogger(MenuController.class.getName());

    private final TextField mapName = new TextField("");
    private final CheckBox debugCb = new CheckBox("Debug-Info anzeigen");
    private final CheckBox mutedCb = new CheckBox("Sound abgeschaltet");
    private final Button playBtn = new Button("Zurück zum Spiel");
    private Scene scene;

    /**
     * Creates the controller.
     *
     * @param engine the game engine this controller belongs to
     */
    public MenuController(GameEngine engine) {
        super(engine);
    }

    /**
     * Creates the scene representing the menu of the Droids game..
     */
    private Scene makeScene() {
        debugCb.setOnAction(e -> engine.getModel().setDebugMode(debugCb.isSelected()));
        mutedCb.setOnAction(e -> engine.getModel().setMuted(mutedCb.isSelected()));
        debugCb.setMaxWidth(Double.MAX_VALUE);
        mutedCb.setMaxWidth(Double.MAX_VALUE);

        mapName.setEditable(false);
        mapName.minWidth(200);
        mapName.setMaxWidth(Double.MAX_VALUE);

        Button loadBtn = new Button("Map von Datei laden...");
        loadBtn.setMaxWidth(Double.MAX_VALUE);
        loadBtn.setOnMouseClicked(e -> loadFromFile());

        Button randomBtn = new Button("Zufällige Map erstellen");
        randomBtn.setMaxWidth(Double.MAX_VALUE);
        randomBtn.setOnMouseClicked(e -> loadRandomMap());

        Button saveBtn = new Button("Map in Datei speichern...");
        saveBtn.setMaxWidth(Double.MAX_VALUE);
        saveBtn.setOnMouseClicked(event -> saveToFile());

        playBtn.setMaxWidth(Double.MAX_VALUE);
        playBtn.setOnAction(e -> engine.activatePlayGameController());

        Button exitBtn = new Button("Spiel beenden");
        exitBtn.setMaxWidth(Double.MAX_VALUE);
        exitBtn.setOnMouseClicked(e -> quitGame());

        Label mapLabel = new Label("Map (Dateiname): ");

        VBox box = new VBox(10);
        box.setPadding(new Insets(10, 10, 10, 10));
        box.getChildren().addAll(mapLabel, mapName, playBtn, randomBtn, loadBtn, saveBtn, debugCb, mutedCb, exitBtn);

        return new Scene(box);
    }

    /**
     * Quits the game after user confirmation.
     */
    private void quitGame() {
        final Alert confirmation = new Alert(AlertType.CONFIRMATION);
        confirmation.setContentText("Wollen Sie das Spiel wirklich beenden?");
        confirmation.initOwner(scene.getWindow());
        final Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK)
            System.exit(0);
    }

    /**
     * Generates and loads a random game map
     */
    private void loadRandomMap() {
        engine.getModel().loadRandomMap();
        mapName.setText("");
        engine.activatePlayGameController();
    }

    /**
     * Opens a file chooser dialog and loads the game map from the file selected there.
     */
    private void loadFromFile() {
        File selectedFile = chooseFile("lade Map").showOpenDialog(scene.getWindow());
        if (selectedFile != null) {
            loadFromFile(selectedFile);
            engine.activatePlayGameController();
        }
    }

    /**
     * Loads a game map from the specified file in the file system.
     *
     * @param selectedFile the file, from which the game map should be loaded
     */
    private void loadFromFile(File selectedFile) {
        try {
            engine.getModel().loadMap(selectedFile);
            mapName.setText(selectedFile.getName());
        }
        catch (XMLStreamException | IOException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
            Alert error = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
            error.initOwner(scene.getWindow());
            error.showAndWait();
        }
    }

    /**
     * Opens a file chooser dialog and saves the current game map to the file selected there.
     */
    private void saveToFile() {
        File selectedFile = chooseFile("speichere Map").showSaveDialog(scene.getWindow());
        if (selectedFile != null) {
            saveToFile(selectedFile);
            engine.activatePlayGameController();
        }
    }

    /**
     * Saves the current game map to the specified file in the file system.
     *
     * @param selectedFile the file, to which the Map should be saved to
     */
    private void saveToFile(File selectedFile) {
        try {
            engine.getModel().saveMap(selectedFile);
            mapName.setText(selectedFile.getName());
        }
        catch (FileNotFoundException | XMLStreamException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
            Alert error = new Alert(AlertType.ERROR,
                                    "Fehler beim Speichern in " + selectedFile,
                                    ButtonType.OK);
            error.initOwner(scene.getWindow());
            error.showAndWait();
        }
    }

    /**
     * Creates a file chooser dialog for xml files, but does not yet open it.
     *
     * @param title the title of the file chooser dialog
     * @return the file chooser completely initialized with a title and for selecting xml files.
     */
    private FileChooser chooseFile(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(
                new ExtensionFilter("XML-Dateien", "*.xml"));
        return fileChooser;
    }

    /**
     * This method is called whenever this controller is activated, i.e., when the menu state is entered.
     */
    @Override
    void entry() {
        final DroidsGameModel model = engine.getModel();
        debugCb.setSelected(model.isDebugMode());
        mutedCb.setSelected(model.isMuted());
        playBtn.setDisable(model.gameLost() || model.gameWon());
        if (scene == null)
            scene = makeScene();
        engine.setScene(scene);
    }
}