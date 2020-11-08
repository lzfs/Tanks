package pp.tanks;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Main class of the sample app
 */
public class SampleApp extends Application {

    /**
     * Main Method
     *
     * @param args input args
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Hello!");

        // Lade ein Bild aus dem internen Ressourcen Ordner auf Ebene der Klassen
        // Dieses Bild befindet sich also in der .jar Datei
        Image image = new Image(SampleApp.class.getResourceAsStream("/images/unibwm.png"));
        ImageView iv = new ImageView(image);
        Text internalText = new Text("(internes Bild aus der .jar)");
        internalText.setFill(Color.GRAY);

        // Lade externes Bild aus einem externen Ordner neben erzeugter .jar Datei
        // Dieses Bild muss sich also nicht in der .jar befinden
        // In Eclipse wird der Pfad auf der obersten Projektebene gesucht
        // Fuer den Export muessen die angegebenen Ressourcen bzw. der Ordner auf die Ebene der .jar gelegt werden
        final URL url = new File("extBilder/spaceship.png").toURI().toURL();
        System.out.println(url);
        Image image1 = new Image(url.toString());
        ImageView iv1 = new ImageView(image1);

        Text externalText = new Text("(externes Bild aus einem Ordner auf Ebene der .jar)");
        externalText.setFill(Color.GRAY);

        Button btn = new Button("Say 'Hello'");
        btn.setOnAction(e -> System.out.println("Hello!"));

        VBox box = new VBox();

        box.getChildren().addAll(iv, internalText, iv1, externalText, btn);
        box.setSpacing(5);
        box.setAlignment(Pos.CENTER);
        primaryStage.setScene(new Scene(box, 320, 260));
        primaryStage.show();
    }
}