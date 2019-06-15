import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

public class UnitController extends AnchorPane {

    @FXML private @Getter @Setter JFXButton showDetailBtn;
    @FXML private @Getter @Setter Label time;
    @FXML private @Getter @Setter Label title;
    @FXML private @Getter @Setter Label details;

    private @Getter @Setter int idTimeLine;
    private @Getter @Setter TimelineUnit unit;

    @FXML
    void showDetailRequest(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("details.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);

            Stage stage = new Stage();
            stage.setTitle("Details");
            stage.setScene(scene);
            DetailsController detailsController = fxmlLoader.getController();
            detailsController.setUnit(unit);
            detailsController.renderLabels();
            stage.show();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }
    }

    public UnitController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("unit.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

}