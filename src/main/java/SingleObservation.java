import lombok.Data;
import org.hl7.fhir.dstu3.model.Observation;

import java.util.Date;

@Data
public class SingleObservation {

    private String category;

    private Date date;

    private Number value;

    private String unit;

    public SingleObservation(Observation observation){
        this.date = observation.getEffectiveDateTimeType().getValue();
        this.value = observation.getValueQuantity().getValue();
        this.unit = observation.getValueQuantity().getUnit();
        this.category = observation.getCode().getText();
    }
}