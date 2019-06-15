import ca.uhn.fhir.rest.api.MethodOutcome;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.hl7.fhir.dstu3.model.Patient;

import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateController {
    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField phoneNbr;

    @FXML
    private TextField address;

    @FXML
    private TextField city;

    @FXML
    private TextField postCode;

    @FXML
    private TextField country;

    @Setter @Getter
    private SinglePatient oldInfo;

    @Setter @Getter
    private Patient patient;

    @FXML
    public void confirmUpdate(ActionEvent actionEvent) throws ParseException {
        patient = new Patient();
        patient.addIdentifier().setSystem("urn:system").setValue("12345");

        if (!firstName.getText().equals("") && !lastName.getText().equals(""))
            patient.addName().setFamily(lastName.getText()).addGiven(firstName.getText());
        else if(firstName.getText().equals("") && !lastName.getText().equals(""))
            patient.addName().setFamily(lastName.getText()).addGiven(oldInfo.getName().get());
        else if(lastName.getText().equals("") && !firstName.getText().equals(""))
            patient.addName().setFamily(oldInfo.getSurname().get()).addGiven(firstName.getText());
        else
            patient.addName().setFamily(oldInfo.getSurname().get()).addGiven(oldInfo.getName().get());

        Date date = new SimpleDateFormat("dd-MM-yyyy").parse(oldInfo.getBirthDate().get());
        patient.setBirthDate(date);

        if(oldInfo.getGender().getValue() == "FEMALE"){
            System.out.println("Kobieta");
            patient.setGender(AdministrativeGender.FEMALE);
        }else{
            patient.setGender(AdministrativeGender.MALE);
        }


        if(!phoneNbr.getText().equals(""))
            patient.addTelecom().setValue(phoneNbr.getText());
        else
            patient.addTelecom().setValue(oldInfo.getPhoneNbr().get());

        String addressText;
        if(!address.getText().equals(""))
            addressText = address.getText();
        else
            addressText = oldInfo.getAddress().get();

        String cityText;
        if(!city.getText().equals(""))
            cityText = city.getText();
        else
            cityText = oldInfo.getCity().get();
        String postCodeText;
        if(!postCode.getText().equals(""))
            postCodeText = postCode.getText();
        else
            postCodeText = oldInfo.getPostCode().get();

        String countryText;
        if(!country.getText().equals(""))
            countryText = country.getText();
        else
            countryText = oldInfo.getCountry().get();

        patient.addAddress().addLine(addressText).setCity(cityText).setPostalCode(postCodeText).setCountry(countryText);

        patient.setId(oldInfo.getId().get());


        FhirServerClient.getInstance().updatePatient(patient);
        System.out.println("update completed");


    }
}
