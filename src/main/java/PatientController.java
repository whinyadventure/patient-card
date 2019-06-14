import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class PatientController {

    private SinglePatient currentPatient;

    // patient_card.fxml components
    @FXML
    private Label serverID;

    @FXML
    private Label firstName;

    @FXML
    private Label lastName;

    @FXML
    private Label birthDate;

    @FXML
    private Label gender;

    @FXML
    private Label phoneNbr;

    @FXML
    private Label address;

    @FXML
    private Label city;

    @FXML
    private Label postCode;

    @FXML
    private Label country;




    public void initialization(SinglePatient chosenPatient) {
        this.currentPatient = chosenPatient;
        renderLabels();
    }

    public void renderLabels() {

        serverID.textProperty().bind(currentPatient.getId());
        firstName.textProperty().bind(currentPatient.getName());
        lastName.textProperty().bind(currentPatient.getSurname());
        birthDate.textProperty().bind(currentPatient.getBirthDate());
        gender.textProperty().bind(currentPatient.getGender());
        phoneNbr.textProperty().bind(currentPatient.getPhoneNbr());
        address.textProperty().bind(currentPatient.getAddress());
        city.textProperty().bind(currentPatient.getCity());
        postCode.textProperty().bind(currentPatient.getPostCode());
        country.textProperty().bind(currentPatient.getCountry());
    }

    @FXML
    public void backToMenu(ActionEvent actionEvent) throws IOException { // czy na pewno trzeba na nowo renderowac cala scene? Nie wiem, a wydajnosc jest słaba teraz :d

        Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
        Scene current_scene = new Scene(root);

        PatientCard.getInstance().primaryStage.setScene(current_scene);
        PatientCard.getInstance().primaryStage.show();
    }
}
