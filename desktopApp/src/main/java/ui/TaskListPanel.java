package ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import me.rajobasu.shared.core.algo.TaskChunk;

/**
 * Panel containing the list of tasks.
 */
public class TaskListPanel extends UiPart<Region> {

    private static final String LABEL = "Task List";
    private static final String FXML = "TaskListPanel.fxml";

    @FXML
    private Label taskListLabel;

    @FXML
    private ListView<TaskChunk> taskListView;

    /**
     * Creates a {@code TaskListPanel} with the given {@code ObservableList}.
     */
    public TaskListPanel(ObservableList<TaskChunk> taskList) {
        super(FXML);
        taskListLabel.setText(LABEL);
        taskListView.setItems(taskList);
        taskListView.setCellFactory(listView -> new TaskListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Task} using a {@code TaskCard}.
     */
    class TaskListViewCell extends ListCell<TaskChunk> {
        @Override
        protected void updateItem(TaskChunk taskChunk, boolean empty) {
            super.updateItem(taskChunk, empty);

            if (empty || taskChunk == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new TaskCard(taskChunk, getIndex() + 1).getRoot());
            }
        }
    }
}
