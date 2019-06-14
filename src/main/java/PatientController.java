import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.hl7.fhir.dstu3.model.MedicationRequest;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;


public class PatientController {

    private SinglePatient currentPatient;

    private List<TimelineUnit> listRequestsUnits;
    private List<MedicationRequest> listFetchedRequests;

    private DatePicker startDateRequests;

    // Patient tab
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

    // Request Medication tab

    @FXML
    private VBox medicalRequestVBox;

    @FXML
    private Pane requestDatePane;

    @FXML
    private ScrollPane requestScrollPane;

    @FXML
    private Button applyRequestDateBtn;

    @FXML
    private VBox requestUnitsVBox;

    @FXML
    public void getRequests(ActionEvent actionEvent) {
        requestUnitsVBox.getChildren().clear();
        createRequestUnits(false);
        addUnits();
    }



    public void initialization(SinglePatient chosenPatient) {

        System.out.println(chosenPatient.getId().toString());
        System.out.println(chosenPatient.getId().get());
        listFetchedRequests = FhirServerClient.getInstance().getMedicationRequests(chosenPatient.getId().get());


        System.out.println(listFetchedRequests.size());

        startDateRequests = new DatePicker();
        this.currentPatient = chosenPatient;
        renderLabels();
        initCalendar();

        createRequestUnits(true);
        addUnits();
    }

    private void initCalendar() {
        startDateRequests = new DatePicker();
        requestDatePane.getChildren().add(startDateRequests);
    }

    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public void createRequestUnits(boolean init) {
        listRequestsUnits = new LinkedList<>();

        if(this.startDateRequests.getValue() == null) {
            this.startDateRequests.setValue(LocalDate.now());
        }

        int idCounter = 1;
        for(MedicationRequest request: listFetchedRequests) {
            if(init || request.getAuthoredOn().after(asDate(this.startDateRequests.getValue()))) {
                listRequestsUnits.add(
                        TimelineUnit.builder().id(idCounter)
                                .title(request.getMedicationCodeableConcept().getText())
                                .details(request.getStatus().toString())
                                .dateTime(request.getAuthoredOnElement().getValue()).build()
                );
                idCounter++;
            }
        }
    }

    public void addUnits() {

        for (TimelineUnit timeUnit : listRequestsUnits) {
            //For each unit create a new instance
            UnitController unitController = new UnitController();
            unitController.getTitle().setText(timeUnit.getTitle());
            unitController.getDetails().setText(timeUnit.getDetails());
            unitController.getTime().setText(timeUnit.getDateTime().toString());
            unitController.setIdTimeLine(timeUnit.getId());
            requestUnitsVBox.getChildren().add(unitController);
        }
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
