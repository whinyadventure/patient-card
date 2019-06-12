import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Data;
import org.hl7.fhir.dstu3.model.Patient;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
class SinglePatient {
    private StringProperty id;
    private StringProperty name;
    private StringProperty surname;
    private StringProperty birthDate;
    private StringProperty gender;
    private StringProperty phoneNbr;
    private StringProperty address;
    private StringProperty city;
    private StringProperty postCode;
    private StringProperty country;


    public SinglePatient(Patient patient) {

        try {
            this.id = new SimpleStringProperty(patient.getIdElement().getIdPart());
        } catch (Exception e) {
            this.id = new SimpleStringProperty("");
        }

        try {
            this.name = new SimpleStringProperty(patient.getName().get(0).getGivenAsSingleString());
        } catch (Exception e) {
            this.name = new SimpleStringProperty("");
        }

        try {
            this.surname = new SimpleStringProperty(patient.getName().get(0).getFamily());
        } catch (Exception e) {
            this.surname = new SimpleStringProperty("");
        }

        try {
            this.birthDate = new SimpleStringProperty(this.dateParser(patient.getBirthDate()));
        } catch (Exception e) {
            this.birthDate = new SimpleStringProperty("");
        }

        try {
            this.gender = new SimpleStringProperty(patient.getGender().toString());
        } catch (Exception e) {
            this.gender = new SimpleStringProperty("");
        }

        try {
            this.phoneNbr = new SimpleStringProperty(patient.getTelecom().get(0).getValue());
        } catch (Exception e) {
            this.phoneNbr = new SimpleStringProperty("");
        }

        try {
            this.address = new SimpleStringProperty(patient.getAddressFirstRep().toString());
        } catch (Exception e) {
            this.address = new SimpleStringProperty("");
        }

        try {
            this.city = new SimpleStringProperty(patient.getAddressFirstRep().getCity());
        } catch (Exception e) {
            this.city = new SimpleStringProperty("");
        }

        try {
            this.postCode = new SimpleStringProperty(patient.getAddressFirstRep().getPostalCode());
        } catch (Exception e) {
            this.postCode = new SimpleStringProperty("");
        }

        try {
            this.country = new SimpleStringProperty(patient.getAddressFirstRep().getCountry());
        } catch (Exception e) {
            this.country = new SimpleStringProperty("");
        }

    }

    private String dateParser(Date date) {

        return new SimpleDateFormat("dd-MM-yyyy").format(date);
    }
}