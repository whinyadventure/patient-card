import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Platform;

public class Main extends Application {

    public void start(Stage primaryStage) throws Exception {

        new PatientCard(primaryStage); // save main frame

        Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
        Scene current_scene = new Scene(root);
        primaryStage.setTitle("Patient Health Card System");
        primaryStage.setScene(current_scene);
        primaryStage.onCloseRequestProperty().setValue(e -> Platform.exit());
        primaryStage.show();
    }

    public static void main(String... args){
        launch(args);
    }

}