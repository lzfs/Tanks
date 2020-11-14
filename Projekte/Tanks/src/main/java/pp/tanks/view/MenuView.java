package pp.tanks.view;

import pp.tanks.Resources;
import pp.tanks.controller.Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MenuView extends GridPane implements View {
    Stage stage;
    Controller controller;

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

    public static MenuView makeView(Stage stage, String string, Controller controller) {
        return new MenuView(stage, string, controller);
    }

    @Override
    public void update() {}
}
