import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.hl7.fhir.dstu3.model.MedicationRequest;
import org.hl7.fhir.dstu3.model.Observation;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.sql.Timestamp;
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

    @FXML
    private VBox observationVBox;

    @FXML
    private Pane observationDatePane;

    @FXML
    private Pane observationUnitsVBox;

    //@FXML
    //private VBox medicalRequestVBox;

    @FXML
    private Pane requestDatePane;

    //@FXML
    //private ScrollPane requestScrollPane;

    @FXML
    private Button applyRequestDateBtn;

    @FXML
    private Pane requestUnitsVBox;

    @FXML
    public void getRequests(ActionEvent actionEvent) {

        requestUnitsVBox.getChildren().clear();
        createRequestUnits(false);
        addUnits(requestUnitsVBox, listRequestsUnits);
    }

    @FXML
    public void getObservations(ActionEvent actionEvent) {

    }



    public void initialization(SinglePatient chosenPatient) {

        System.out.println(chosenPatient.getId().toString());
        System.out.println(chosenPatient.getId().get());
        listFetchedRequests = FhirServerClient.getInstance().getMedicationRequests(chosenPatient.getId().get());


        System.out.println(listFetchedRequests.size());

        startDateRequests = new DatePicker();
        this.currentPatient = chosenPatient;
        renderLabels();
        initCalendar(startDateRequests, requestDatePane);

        createRequestUnits(true);
        addUnits(requestUnitsVBox, listRequestsUnits);

        initObservationsView();
    }
    private void initObservationsView(){

        initCalendar(startDateObservation, observationDatePane);
        this.observations = FhirServerClient.getInstance().getObservations(this.currentPatient.getId().getValue());
        System.out.println("Wczytane: " + observations.size() + " " + currentPatient.getId().getValue());
        showObservation();
        addUnits(observationUnitsVBox, observationsToShow);
    }

    private void showObservation(){
        observationsToShow = new LinkedList<>();
        int id = 0;
        this.observations.stream()
        .forEach(e ->
                {
                    //String date = e.getEffectiveDateTimeType().getValueAsString().substring(0,date.length()-6);
                    observationsToShow.add(
                            TimelineUnit.builder().id(0).title(e.getCode().getText())
                                    .details(extractDetails(e))
                                    .dateTime(e.getEffectiveDateTimeType().getValue()).build()
                    );
               });


    }
    private String extractDetails(Observation observation) {
        StringBuilder sb = new StringBuilder();
        sb.append(Types.getTextForValue(observation.getValue()));

        return sb.toString();
    }

    private void initCalendar(DatePicker actPicker, Pane actPane) {
        actPicker = new DatePicker();
        actPane.getChildren().add(actPicker);
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
                System.out.println("sialalala");
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

    public void addUnits(Pane actPane, List<TimelineUnit> toShow) {

        for (TimelineUnit timeUnit : toShow) {
            //For each unit create a new instance
            UnitController unitController = new UnitController();
            unitController.getTitle().setText(timeUnit.getTitle());
            unitController.getDetails().setText(timeUnit.getDetails());
            unitController.getTime().setText(timeUnit.getDateTime().toString());
            unitController.setIdTimeLine(timeUnit.getId());
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
