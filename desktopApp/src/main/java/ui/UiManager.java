package ui;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import me.rajobasu.shared.core.algo.ScheduleManager;

/**
 * The manager of the UI component.
 */
public class UiManager implements Ui {

    public static final String ALERT_DIALOG_PANE_FIELD_ID = "alertDialogPane";
    
    private MainWindow mainWindow;
    private ScheduleManager scheduleManager;

    /**
     * Creates a {@code UiManager} with the given {@code Logic}.
     */
    public UiManager(ScheduleManager scheduleManager) {
        super();
        this.scheduleManager = scheduleManager;
    }

    /**
     * Shows an alert dialog on {@code owner} with the given parameters.
     * This method only returns after the user has closed the alert dialog.
     */
    private static void showAlertDialogAndWait(
            Stage owner, AlertType type, String title, String headerText,
            String contentText) {
        final Alert alert = new Alert(type);
        alert.getDialogPane().getStylesheets().add("view/DarkTheme.css");
        alert.initOwner(owner);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.getDialogPane().setId(ALERT_DIALOG_PANE_FIELD_ID);
        alert.showAndWait();
    }

    @Override
    public void start(Stage primaryStage) {
        //Set the application icon.

        try {
            mainWindow = new MainWindow(primaryStage, scheduleManager);
            mainWindow.show(); //This should be called before creating other UI parts
            mainWindow.fillInnerParts();

        } catch (Throwable e) {
            showFatalErrorDialogAndShutdown("Fatal error during initializing", e);
        }
    }

    void showAlertDialogAndWait(Alert.AlertType type, String title, String headerText, String contentText) {
        showAlertDialogAndWait(mainWindow.getPrimaryStage(), type, title, headerText, contentText);
    }

    /**
     * Shows an error alert dialog with {@code title} and error message, {@code e},
     * and exits the application after the user has closed the alert dialog.
     */
    private void showFatalErrorDialogAndShutdown(String title, Throwable e) {
        showAlertDialogAndWait(Alert.AlertType.ERROR, title, e.getMessage(), e.toString());
        Platform.exit();
        System.exit(1);
    }

}
