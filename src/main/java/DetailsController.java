import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.Getter;
import lombok.Setter;

public class DetailsController {
    @FXML
    private Label obTitle;

    @FXML
    private Label obDate;

    @FXML
    private Label obValUni;

    @FXML
    private Label obStatus;

    @Getter @Setter
    private TimelineUnit unit;

    public void renderLabels() {
        obTitle.setText(unit.getTitle());
        obDate.setText(unit.getDateTime().toString());
        obValUni.setText(unit.getDetails());
        obStatus.setText(unit.getStatus());
    }
}
