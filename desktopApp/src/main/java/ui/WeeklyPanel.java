package ui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import me.rajobasu.shared.core.model.Schedule;

/**
 * Panel containing the list of daily panels for a particular week.
 */
public class WeeklyPanel extends UiPart<Region> {

    private static final String FXML = "WeeklyPanel.fxml";
    private static final String WEEK_LABEL = "Week %d (%s)";

    @FXML
    private Label weekLabel;

    @FXML
    private StackPane progressPanelPlaceholder;

    @FXML
    private HBox dailyHBox;

    /**
     * Creates a {@code WeeklyPanel} with the given {@code ObservableList}.
     */
    public WeeklyPanel(Schedule weeklyTasks) {
        super(FXML);
//        SimpleIntegerProperty observableWeekNumber = weeklyTasks.getObservableWeekNumber();
//        LocalDate firstDayOfWeek = weeklyTasks.getFirstDayOfWeek();
//        weekLabel.setText(String.format(
//                WEEK_LABEL, observableWeekNumber.getValue(), weeklyTasks.getFirstDayOfWeek().toString()));
        for (int i = 0; i < 7; i++) {
            DailyPanel dailyPanel =
                    new DailyPanel(FXCollections.observableList(weeklyTasks.getTaskChunkListForDate(i)));
            dailyHBox.getChildren().add(dailyPanel.getRoot());
            // This can probably be replaced with something more efficient
            dailyHBox.widthProperty()
                    .addListener(e -> dailyPanel.getRoot().setPrefWidth(dailyHBox.getWidth() / 7));
        }

//        ProgressPanel progressPanel =
//                new ProgressPanel(
//                        weeklyTasks.getWeeklyProgress(),
//                        weeklyTasks.getTotalWeeklyTasks(),
//                        weeklyTasks.getTotalDoneTasks());
//        progressPanelPlaceholder.getChildren().add(progressPanel.getRoot());

//        observableWeekNumber.addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                weekLabel.setText(String.format(
//                        WEEK_LABEL, newValue.intValue(), weeklyTasks.getFirstDayOfWeek().toString()));
//            }
//        });
    }

}
