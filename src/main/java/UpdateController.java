import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;
import org.hl7.fhir.dstu3.model.Patient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateController {
    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField birthDate;

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

    @FXML
    public void confirmUpdate(ActionEvent actionEvent) {
        Patient patient = new Patient();

        // zle pola probuje przypisac, problem z ogarnieciem tej pieprzonej struktury raz jeszcze...
        if (firstName.getText() != "")
            patient.addName().addGiven(firstName.getText());
        else
            patient.addName().addGiven(oldInfo.getName().get());

        if (lastName.getText() != "")
            patient.addName().setFamily(lastName.getText());
        else
            patient.addName().setFamily(oldInfo.getSurname().get());

        // zle parsery, data sie nie formatuje
        /*if (birthDate.getText() != "") {
            try {
                Date date = new SimpleDateFormat("dd-MM-yyyy").parse(birthDate.getText());
                patient.setBirthDate(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Date date = new SimpleDateFormat("dd-MM-yyyy").parse(oldInfo.getBirthDate().get());
                patient.setBirthDate(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        //}*/

        if(phoneNbr.getText() != "")
            patient.addTelecom().setValue(phoneNbr.getText());
        else
            patient.addTelecom().setValue(oldInfo.getPhoneNbr().get());

        if(address.getText() != "")
            patient.addAddress().addLine(address.getText());
        else
            patient.addAddress().addLine(oldInfo.getAddress().get());

        if(city.getText() != "")
            patient.addAddress().setCity(city.getText());
        else
            patient.addAddress().setCity(oldInfo.getCity().get());

        if(postCode.getText() != "")
            patient.addAddress().setPostalCode(postCode.getText());
        else
            patient.addAddress().setPostalCode(oldInfo.getPostCode().get());

        if(country.getText() != "")
            patient.addAddress().setCountry(country.getText());
        else
            patient.addAddress().setCountry(oldInfo.getCountry().get());


        patient.setId(oldInfo.getId().get());

        System.out.println(oldInfo.getId());
        System.out.println(oldInfo.getName());
        System.out.println(oldInfo.getSurname());
        System.out.println(oldInfo.getAddress());

        System.out.println(patient.getId());




        //FhirServerClient.getInstance().updatePatient(patient);
        //System.out.println("update completed");

    }
}
