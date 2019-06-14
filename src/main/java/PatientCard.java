import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PatientCard {

    private static PatientCard instance = null;  // only one instance at the moment
    public Stage primaryStage; // handle to main frame
    public Scene menuScene;

    public PatientCard(Stage primary, Scene menuScene) {
        instance = this;
        this.primaryStage = primary;
        this.menuScene = menuScene;
    }

    public static PatientCard getInstance() {
        return instance;
    }

    public void openPatientCard(SinglePatient chosenPatient) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("patient_card.fxml"));
            Parent root = loader.load();
            PatientController patientController = loader.getController();

            patientController.initialization(chosenPatient);

            Scene currentScene = new Scene(root);
            this.primaryStage.setScene(currentScene);
            this.primaryStage.show();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
