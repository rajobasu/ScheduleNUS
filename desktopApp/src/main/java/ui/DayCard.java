package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import me.rajobasu.shared.core.algo.TaskChunk;


/**
 * An UI component that displays information of a {@code Task} in the specified day.
 */
public class DayCard extends UiPart<Region> {

    private static final String FXML = "DayCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final TaskChunk task;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label date;
    @FXML
    private Label id;

    /**
     * Creates a {@code TaskCode} with the given {@code Task} and index to display.
     */
    public DayCard(TaskChunk task, int size) {
        super(FXML);
        this.task = task;
        name.setText(task.getParentTask().getDescription());
        date.setText(task.getTimeChunk().getStartTime().toString());
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DayCard)) {
            return false;
        }

        // state check
        DayCard card = (DayCard) other;
        return id.getText().equals(card.id.getText())
                && task.equals(card.task);
    }
}
