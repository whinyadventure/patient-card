import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import org.hl7.fhir.dstu3.model.Observation;
import java.util.Date;

import java.util.*;
import java.util.stream.Collectors;

public class ChartsController{
    private static ChartsController instance = null;  // only one instance at the moment


    private Map<String, List<SingleObservation>> observations;

    @FXML
    private ComboBox selectChart;

    @FXML
    private VBox chartsVBox;


    @FXML
    public void initialize() {

        this.observations = Charts.getInstance().getMappedObservations();

        this.selectChart.setItems(FXCollections.observableArrayList(observations.keySet()));
        this.selectChart.getSelectionModel().select(0);

        selectChart.getSelectionModel().selectedIndexProperty().addListener((options, before, now)->{
            System.out.println(selectChart.getSelectionModel().getSelectedItem());
                showSingleChart(selectChart.getSelectionModel().getSelectedItem().toString());
        });
    }

    private void showSingleChart(String category) {

        List<SingleObservation> data = this.observations.get(category);
        System.out.println(data);
        if (chartsVBox.getChildren().size() > 1)
            this.chartsVBox.getChildren().remove(1);
        this.chartsVBox.getChildren().add(1, createChartForObservations(data));
    }

    private LineChart<Date, Number> createChartForObservations(List <SingleObservation> data) {
        ObservableList<XYChart.Series<Date, Number>> series = FXCollections.observableArrayList();
        ObservableList<XYChart.Data<Date, Number>> series1Data = FXCollections.observableArrayList();
        System.out.println(data);
        data.stream()
                .map(o -> new XYChart.Data<>(o.getDate(), o.getValue()))
                .forEach(series1Data::add);

        series.add(new XYChart.Series<>(data.get(0).getCategory(), series1Data));
        NumberAxis numberAxis = new NumberAxis();
        numberAxis.setLabel(data.get(0).getUnit());
        DateAxis dateAxis = new DateAxis(Types.addDaysToDate(data.get(0).getDate(), -10),
                Types.addDaysToDate(data.get(data.size() - 1).getDate(), 10));
        LineChart<Date, Number> chart = new LineChart<>(dateAxis, numberAxis, series);


        setUpTooltip(chart);

        return chart;
    }

    private void setUpTooltip(LineChart<Date, Number> chart) {
        for (final XYChart.Series<Date, Number> s : chart.getData()) {
            for (final XYChart.Data<Date, Number> data : s.getData()) {
                Tooltip tooltip = new Tooltip();
                tooltip.setText("Value: " + data.getYValue().toString() + "\nDate: " + data.getXValue().toString());
                Tooltip.install(data.getNode(), tooltip);
            }
        }
    }
}
