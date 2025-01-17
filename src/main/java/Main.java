import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.util.Locale;

public class Main extends Application {

    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
        primaryStage.setTitle("Patient Health Card System");
        primaryStage.setWidth(1280);
        primaryStage.setHeight(800);
        primaryStage.setResizable(false);
        Scene menuScene = new Scene(root);
        primaryStage.setScene(menuScene);
        primaryStage.onCloseRequestProperty().setValue(e -> Platform.exit());
        primaryStage.show();

        new PatientCard(primaryStage, menuScene); // save main frame
    }

    public static void main(String... args){

        Locale.setDefault(Locale.UK);
        launch(args);
    }

}