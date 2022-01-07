package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import me.rajobasu.shared.core.algo.ScheduleManager;
import me.rajobasu.shared.core.algo.TaskChunk;
import me.rajobasu.shared.core.commands.CommandFactoryKt;

import java.util.ArrayList;


/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    public static final ObservableList<TaskChunk> taskList = FXCollections.observableList(new ArrayList<>());
    public static final WeeklyTasks weeklyTasks = new WeeklyTasks(taskList);
    private static final String FXML = "MainWindow.fxml";
    private Stage primaryStage;
    private ScheduleManager scheduleManager;

    // Independent Ui parts residing in this Ui container
    private TaskListPanel taskListPanel;
    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;
    private WeeklyPanel weeklyPanel;
    private CommandBox commandBox;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane taskListPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    @FXML
    private StackPane weeklyPanelPlaceholder;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and {@code Logic}.
     */
    public MainWindow(Stage primaryStage, ScheduleManager scheduleManager) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;

        this.scheduleManager = scheduleManager;


        setAccelerators();

        helpWindow = new HelpWindow();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     *
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        taskListPanel = new TaskListPanel(null);
        taskListPanelPlaceholder.getChildren().add(taskListPanel.getRoot());

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter("haha");
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());

        // pass in date into weeklyPanel constructor instead of
        // a taskList. Currently placeholder
        taskList.addAll(scheduleManager.currentSchedule.getTaskList());
        weeklyTasks.updateWeeklyList(scheduleManager.currentSchedule.getFirstDate());
        weeklyPanel = new WeeklyPanel(weeklyTasks);
        weeklyPanelPlaceholder.getChildren().add(weeklyPanel.getRoot());
    }


    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        helpWindow.hide();
        primaryStage.hide();
    }

    public TaskListPanel gettaskListPanel() {
        return taskListPanel;
    }

    /**
     * Executes the command and returns the result.
     */
    private void executeCommand(String commandText) {
        CommandFactoryKt.parseAndGetCommand(commandText).execute(scheduleManager);
        taskList.clear();
        taskList.addAll(scheduleManager.currentSchedule.getTaskList());
        weeklyTasks.updateWeeklyList(scheduleManager.currentSchedule.getFirstDate());
        resultDisplay.setFeedbackToUser("Good Job");
        this.commandBox.setCommandTextField("");
    }
}
