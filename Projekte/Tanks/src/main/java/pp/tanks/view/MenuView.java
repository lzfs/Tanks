package pp.tanks.view;

import pp.tanks.Resources;
import pp.tanks.controller.Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Represents the view of the menus
 */
public class MenuView extends GridPane implements View {
    Stage stage;
    Controller controller;

    /**
     * Creates a view for the specified menu
     *
     * @param fileName the file name of the view to create
     * @param controller the according controller
     */
    private MenuView(Stage stage, String fileName, Controller controller) {
        this.stage = stage;
        this.controller = controller;
        final URL location = getClass().getResource(fileName);
        FXMLLoader fxmlLoader = new FXMLLoader(location, Resources.RESOURCE_BUNDLE);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(controller);
        try {
            fxmlLoader.load();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * static method to create a menuView
     */
    public static MenuView makeView(Stage stage, String string, Controller controller) {
        return new MenuView(stage, string, controller);
    }

    /**
     * is called to update the menuView
     */
    @Override
    public void update() {
    }
}
