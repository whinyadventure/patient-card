import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

public class PatientController {

    private SinglePatient currentPatient;

    private static List<TimelineUnit> listeTimeLine;

    private DatePicker checkInDatePicker;

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

    @FXML
    private VBox medicalRequestVBox;




    public void initialization(SinglePatient chosenPatient) {

        checkInDatePicker = new DatePicker();
        this.currentPatient = chosenPatient;
        renderLabels();
        initCalendar();
        testData();
        addUnits();
    }

    private void initCalendar() {
        VBox vbox = new VBox(20);
        vbox.setStyle("-fx-padding: 10;");

        checkInDatePicker = new DatePicker();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label checkInlabel = new Label("Check-In Date:");
        gridPane.add(checkInlabel, 0, 0);

        GridPane.setHalignment(checkInlabel, HPos.LEFT);
        gridPane.add(checkInDatePicker, 0, 1);
        vbox.getChildren().add(gridPane);
        medicalRequestVBox.getChildren().add(vbox);
    }

    public void testData() {
        listeTimeLine = new LinkedList<>();
        listeTimeLine.add(
                TimelineUnit.builder().id(1).title("Install tools")
                        .details("Install JDK 1.8, Netbeans 8.2, Scene Builder")
                        .dateTime(LocalDateTime.parse("2018-02-06T13:45:00")).build()
        );
        listeTimeLine.add(
                TimelineUnit.builder().id(2).title("Create An application")
                        .details("Create new Maven JavaFx Applicaton")
                        .dateTime(LocalDateTime.parse("2018-02-06T14:10:00")).build()
        );
        listeTimeLine.add(
                TimelineUnit.builder().id(3).title("Gui design")
                        .details("Create a Simple unity of your Timeline, use your imagination ;)")
                        .dateTime(LocalDateTime.parse("2018-02-06T14:40:00")).build()
        );
        listeTimeLine.add(
                TimelineUnit.builder().id(4).title("Take a break")
                        .details("To refresh your brain, Take a break, move, take a coffé")
                        .dateTime(LocalDateTime.parse("2018-02-06T15:00:00")).build()
        );
        listeTimeLine.add(
                TimelineUnit.builder().id(5).title("Controller")
                        .details("Create a controller for your GUI")
                        .dateTime(LocalDateTime.parse("2018-02-06T15:30:00")).build()
        );
        listeTimeLine.add(
                TimelineUnit.builder().id(6).title("The END")
                        .details("Edit this class and fill this timeline")
                        .dateTime(LocalDateTime.parse("2018-02-06T16:00:00")).build()
        );
    }

    public void addUnits() {
        VBox vbox = new VBox();
        for (TimelineUnit timeUnit : listeTimeLine) {
            //For each unit create a new instance
            UnitController unitController = new UnitController();
            unitController.getTitle().setText(timeUnit.getTitle());
            unitController.getDetails().setText(timeUnit.getDetails());
            unitController.getTime().setText(
                    timeUnit.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            );
            unitController.setIdTimeLine(timeUnit.getId());
            vbox.getChildren().add(unitController);
        }

        ScrollPane scrollPane = new ScrollPane(vbox);
        scrollPane.setFitToHeight(true);
        medicalRequestVBox.getChildren().add(scrollPane);
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
    public void backToMenu(ActionEvent actionEvent) throws IOException {

        PatientCard.getInstance().primaryStage.setScene(PatientCard.getInstance().menuScene);
        PatientCard.getInstance().primaryStage.show();
    }
}
