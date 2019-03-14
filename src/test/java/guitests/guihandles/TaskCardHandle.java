package guitests.guihandles;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMultiset;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.project.model.task.Task;

/**
 * Provides a handle to a task card in the task list panel.
 */
public class TaskCardHandle extends NodeHandle<Node> {
    private static final String ID_FIELD_ID = "#id";
    private static final String NAME_FIELD_ID = "#name";
    private static final String DESCRIPTION_FIELD_ID = "#description";
    private static final String DEADLINE_FIELD_ID = "#deadline";
    private static final String TAGS_FIELD_ID = "#tags";

    private final Label idLabel;
    private final Label nameLabel;
    private final Label descriptionLabel;
    private final Label deadlineLabel;
    private final List<Label> tagLabels;

    public TaskCardHandle(Node cardNode) {
        super(cardNode);

        idLabel = getChildNode(ID_FIELD_ID);
        nameLabel = getChildNode(NAME_FIELD_ID);
        descriptionLabel = getChildNode(DESCRIPTION_FIELD_ID);
        deadlineLabel = getChildNode(DEADLINE_FIELD_ID);

        Region tagsContainer = getChildNode(TAGS_FIELD_ID);
        tagLabels = tagsContainer
                .getChildrenUnmodifiable()
                .stream()
                .map(Label.class::cast)
                .collect(Collectors.toList());
    }

    public String getId() {
        return idLabel.getText();
    }

    public String getName() {
        return nameLabel.getText();
    }

    public String getDescription() {
        return descriptionLabel.getText();
    }

    public String getDeadline() {
        return deadlineLabel.getText();
    }

    public List<String> getTags() {
        return tagLabels
                .stream()
                .map(Label::getText)
                .collect(Collectors.toList());
    }

    /**
     * Returns true if this handle contains {@code task}.
     */
    public boolean equals(Task task) {
        return getName().equals(task.getName().fullName)
                && getDescription().equals(task.getDescription().value)
                && getDeadline().equals(task.getDeadline().value)
                && ImmutableMultiset.copyOf(getTags()).equals(ImmutableMultiset.copyOf(task.getTags().stream()
                        .map(tag -> tag.tagName)
                        .collect(Collectors.toList())));
    }
}
