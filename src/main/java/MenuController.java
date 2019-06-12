import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import javafx.fxml.FXML;

import org.hl7.fhir.dstu3.model.Patient;

import java.util.List;
import java.util.stream.Collectors;

public class MenuController {

    // menu.fxml components
    @FXML
    private TableView patientsTable;

    @FXML
    private TableColumn<SinglePatient, String> idColumn;

    @FXML
    private TableColumn<SinglePatient, String> nameColumn;

    @FXML
    private TableColumn<SinglePatient, String> surnameColumn;

    @FXML
    private TableColumn<SinglePatient, String> birthDateColumn;

    @FXML
    private TextField surnameTextField;

    // changeable list of fetched patients
    private ObservableList<SinglePatient> observablePatients = FXCollections.observableArrayList();

    private SinglePatient apply(Patient patient) {

        return new SinglePatient(patient);
    }

    // fetch filtered by surname
    @FXML
    public void searchForPatients(ActionEvent actionEvent) {
        List<SinglePatient> patients = FhirServerClient.getInstance()
                .getPatients(surnameTextField.getText())
                .stream()
                .map(this::apply)
                .collect(Collectors.toList());
        this.observablePatients.clear();
        this.observablePatients.addAll(patients);
    }

    @FXML
    public void fetchAllPatients() {
        List<SinglePatient> patients = FhirServerClient.getInstance()
                .getPatients("")
                .stream()
                .map(this::apply)
                .collect(Collectors.toList());
        System.out.println(patients.size());
        this.observablePatients.clear();
        this.observablePatients.addAll(patients);
    }

    // open card of clicked patient
    @FXML
    public void rowClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
            PatientCard.getInstance().openPatientCard((SinglePatient) patientsTable.getSelectionModel().getSelectedItem());
        }
    }

    // initialize menu table
    @FXML
    public void initialize() {
        patientsTable.setItems(observablePatients);
        idColumn.setCellValueFactory(data -> data.getValue().getId());
        nameColumn.setCellValueFactory(data -> data.getValue().getName());
        surnameColumn.setCellValueFactory(data -> data.getValue().getSurname());
        birthDateColumn.setCellValueFactory(data -> data.getValue().getBirthDate());

        fetchAllPatients();
    }
}
