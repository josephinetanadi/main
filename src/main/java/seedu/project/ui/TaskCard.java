package seedu.project.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.project.model.task.Task;

/**
 * An UI component that displays information of a {@code Task}.
 */
public class TaskCard extends UiPart<Region> {

    private static final String FXML = "TaskListCard.fxml";
    private static final String[] TAG_COLORS = { "teal", "red", "yellow", "blue",
        "orange", "brown", "green", "pink", "black", "grey" };

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved
     * keywords in JavaFX. As a consequence, UI elements' variable names cannot be
     * set to such keywords or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The
     *      issue on Project level 4</a>
     */

    public final Task task;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label description;
    @FXML
    private Label deadline;
    @FXML
    private FlowPane tags;

    public TaskCard(Task task, int displayedIndex) {
        super(FXML);
        this.task = task;
        id.setText(displayedIndex + ". ");
        name.setText(task.getName().fullName);
        description.setText(task.getDescription().value);
        deadline.setText(task.getDeadline().value);
        setTagColor(task);
    }

    // Create tag labels for task
    public void setTagColor(Task task) {
        task.getTags().forEach(tag -> {
            // Todo: Allow defining of tag colors through CLI instead of randomization
            // Throw out color name from TAG_COLOR array based on randomized value
            String tagColor = TAG_COLORS[Math.abs(tag.tagName.hashCode()) % TAG_COLORS.length];
            Label tagLabel = new Label(tag.tagName);
            tagLabel.getStyleClass().add(tagColor);
            tags.getChildren().add(tagLabel);
        });
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TaskCard)) {
            return false;
        }

        // state check
        TaskCard card = (TaskCard) other;
        return id.getText().equals(card.id.getText()) && task.equals(card.task);
    }
}
