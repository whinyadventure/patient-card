// javaFX
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.event.ActionEvent;

// plugins
import javafx.fxml.FXML;
import lombok.Data;

// hapi-fhrir stuff
import org.hl7.fhir.dstu3.model.Patient;

// basic java
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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

    // changeable list of patients
    private ObservableList<SinglePatient> observablePatients = FXCollections.observableArrayList();

    private SinglePatient apply(Patient patient) {

        return new SinglePatient(patient);
    }

    @Data
    private class SinglePatient {
        private StringProperty id;
        private StringProperty name;
        private StringProperty surname;
        private StringProperty birthDate;

        public SinglePatient(Patient patient) {
            this.id = new SimpleStringProperty(patient.getIdElement().getIdPart());
            this.name = new SimpleStringProperty(patient.getName().get(0).getGivenAsSingleString());
            this.surname = new SimpleStringProperty(patient.getName().get(0).getFamily());
            this.birthDate = new SimpleStringProperty(this.dateParser(patient.getBirthDate()));
        }

        private String dateParser(Date date) {

            return new SimpleDateFormat("dd-MM-yyyy").format(date);
        }

        @Override
        public String toString() {
            return "SinglePatient{" +
                    "firstName=" + name.getValue() +
                    ", lastName=" + surname.getValue() +
                    ", dateOfBitrh=" + birthDate.getValue() +
                    '}';
        }
    }

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
