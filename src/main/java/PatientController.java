import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.hl7.fhir.dstu3.model.MedicationRequest;
import org.hl7.fhir.dstu3.model.Observation;

import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;


public class PatientController {

    private SinglePatient currentPatient;

    private List<Observation> observations;
    private List<TimelineUnit> observationsToShow;
    private List<TimelineUnit> listRequestsUnits;
    private List<MedicationRequest> listFetchedRequests;

    private DatePicker startDateRequests, startDateObservation;

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

    // observation tab

    @FXML
    private Pane observationDatePane;

    @FXML
    private Pane observationUnitsVBox;

    // request tab

    @FXML
    private Pane requestDatePane;

    @FXML
    private Pane requestUnitsVBox;

    @FXML
    public void getRequests(ActionEvent actionEvent) {

        requestUnitsVBox.getChildren().clear();
        System.out.println(this.startDateRequests.getValue());
        createRequestUnits(false);
        addUnits(requestUnitsVBox, listRequestsUnits);
    }

    @FXML
    public void getObservations(ActionEvent actionEvent) {
        observationUnitsVBox.getChildren().clear();
        filterObservation();
        addUnits(observationUnitsVBox, observationsToShow);

    }

    @FXML
    private void showCharts(){
        try {

            Charts.getInstance().setObservations(observations);
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("charts.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Stage stage = new Stage();
            stage.setTitle("Charts Window");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }
    }

    public void initialization(SinglePatient chosenPatient) {

        this.currentPatient = chosenPatient;
        renderLabels();

        this.listFetchedRequests = FhirServerClient.getInstance().getMedicationRequests(chosenPatient.getId().get());
        this.observations = FhirServerClient.getInstance().getObservations(this.currentPatient.getId().getValue());

        this.startDateRequests = new DatePicker();
        requestDatePane.getChildren().add(this.startDateRequests);
        createRequestUnits(true);
        addUnits(requestUnitsVBox, listRequestsUnits);

        this.startDateObservation = new DatePicker();
        observationDatePane.getChildren().add(this.startDateObservation);
        showObservation();
        addUnits(observationUnitsVBox, observationsToShow);
    }

    private void showObservation(){
        observationsToShow = new LinkedList<>();
        int id = 1;
        this.startDateObservation.setValue(LocalDate.now());
        for(Observation obs: this.observations){
            observationsToShow.add(
                    TimelineUnit.builder().id(id++).title(obs.getCode().getText())
                            .details(extractDetails(obs))
                            .dateTime(obs.getEffectiveDateTimeType().getValue())
                            .status(obs.getStatus().toString()).build()
            );
        }
    }

    private void filterObservation(){
        observationsToShow = new LinkedList<>();
        int id = 1;
        System.out.println(asDate(this.startDateRequests.getValue()));
        for(Observation obs: this.observations){

            if(obs.getEffectiveDateTimeType().getValue().after(asDate(this.startDateObservation.getValue()))){
                observationsToShow.add(
                        TimelineUnit.builder().id(id++).title(obs.getCode().getText())
                                .details(extractDetails(obs))
                                .dateTime(obs.getEffectiveDateTimeType().getValue())
                                .status(obs.getStatus().toString()).build()
                );
            }
        }
    }
    private String extractDetails(Observation observation) {
        StringBuilder sb = new StringBuilder();
        sb.append(Types.getTextForValue(observation.getValue()));

        return sb.toString();
    }

    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public void createRequestUnits(boolean init) {
        listRequestsUnits = new LinkedList<>();

        if(this.startDateRequests.getValue() == null) {

            this.startDateRequests.setValue(LocalDate.now());
        }

        int id = 0;
        for(MedicationRequest request: listFetchedRequests) {
            if(init || request.getAuthoredOn().after(asDate(this.startDateRequests.getValue()))) {

                listRequestsUnits.add(
                        TimelineUnit.builder().id(id)
                                .title(request.getMedicationCodeableConcept().getText())
                                .details(request.getStatus().toString())
                                .dateTime(request.getAuthoredOnElement().getValue()).build()
                );
            }
        }
    }

    public void addUnits(Pane actPane, List<TimelineUnit> toShow) {

        for (TimelineUnit timeUnit : toShow) {
            //For each unit create a new instance

            UnitController unitController = new UnitController();
            unitController.getTitle().setText(timeUnit.getTitle());
            unitController.getDetails().setText(timeUnit.getDetails());
            unitController.getTime().setText(timeUnit.getDateTime().toString());
            unitController.setIdTimeLine(timeUnit.getId());
            unitController.setUnit(timeUnit);

            if(timeUnit.getId() == 0)
                unitController.getShowDetailBtn().setVisible(false);
            actPane.getChildren().add(unitController);
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
