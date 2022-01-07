package ui;

import javafx.application.Application;
import javafx.stage.Stage;
import me.rajobasu.shared.core.algo.ScheduleManager;

/**
 * Runs the application.
 */
public class MainApp extends Application {


    protected Ui ui;

    @Override
    public void init() throws Exception {
        super.init();
        ui = new UiManager(new ScheduleManager());
    }

    /**
     * Returns a {@code ModelManager} with the data from {@code storage}'s address book and {@code userPrefs}. <br>
     * The data from the sample address book will be used instead if {@code storage}'s address book is not found,
     * or an empty address book will be used instead if errors occur when reading {@code storage}'s address book.
     */

    @Override
    public void start(Stage primaryStage) {
        ui.start(primaryStage);
    }

    @Override
    public void stop() {

    }
}
