import lombok.experimental.UtilityClass;
import org.hl7.fhir.dstu3.model.*;

import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;

@UtilityClass
public class Types {
   public static Date addDaysToDate(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    public static String getTextForValue(Type value) {
        StringBuilder sb = new StringBuilder();
        if (value instanceof Quantity) {
            Quantity quantity = (Quantity) value;
            sb.append(quantity.getValue().setScale(3, RoundingMode.HALF_UP));
            sb.append(" [");
            sb.append(quantity.getUnit());
            sb.append("]");
            sb.append("\n");
        } else if (value instanceof CodeableConcept) {
            CodeableConcept codeableConcept = (CodeableConcept) value;
            sb.append(codeableConcept.getText());
            sb.append("\n");
        } else if (value instanceof StringType) {
            sb.append(((StringType) value).getValueAsString());
            sb.append("\n");
        } else if (value instanceof BooleanType) {
            sb.append(((BooleanType) value).getValueAsString());
            sb.append("\n");
        } else if (value instanceof Period) {
            sb.append(((Period) value).getStart().toString());
            sb.append(" - ");
            sb.append(((Period) value).getEnd().toString());
            sb.append("/n");
        } else if (value instanceof Range) {
            sb.append(((Range) value).getLow().toString());
            sb.append(" - ");
            sb.append(((Range) value).getHigh().toString());
            sb.append("\n");
        } else if (value instanceof DateTimeType) {
            sb.append(((DateTimeType) value).getValueAsString());
            sb.append("\n");
        } else if (value instanceof TimeType) {
            sb.append(((TimeType) value).getValueAsString());
            sb.append("\n");
        } else if (value instanceof SampledData) {
            sb.append(((SampledData) value).getData());
            sb.append("\n");
        } else if (value instanceof Ratio) {
            sb.append(((Ratio) value).getNumerator().getValue()).append(((Ratio) value).getNumerator().getUnit());
            sb.append("/");
            sb.append(((Ratio) value).getDenominator().getValue()).append(((Ratio) value).getDenominator().getUnit());
            sb.append("\n");
        }
        return sb.toString();
    }

}
