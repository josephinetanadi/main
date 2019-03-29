package seedu.project.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.project.model.Name;
import seedu.project.model.tag.Tag;
import seedu.project.model.task.Deadline;
import seedu.project.model.task.Description;
import seedu.project.model.task.Task;
import seedu.project.model.util.SampleDataUtil;

/**
 * A utility class to help with building Task objects.
 */
public class TaskBuilder {

    public static final String DEFAULT_NAME = "Milestone edit";
    public static final String DEFAULT_DESCRIPTION = "config labels, milestone, project board edit";
    public static final String DEFAULT_DEADLINE = "1-1-2011";

    private Name name;
    private Description description;
    private Deadline deadline;
    private Set<Tag> tags;

    public TaskBuilder() {
        name = new Name(DEFAULT_NAME);
        description = new Description(DEFAULT_DESCRIPTION);
        deadline = new Deadline(DEFAULT_DEADLINE);
        tags = new HashSet<>();
    }

    /**
     * Initializes the TaskBuilder with the data of {@code taskToCopy}.
     */
    public TaskBuilder(Task taskToCopy) {
        name = taskToCopy.getName();
        description = taskToCopy.getDescription();
        deadline = taskToCopy.getDeadline();
        tags = new HashSet<>(taskToCopy.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code Task} that we are building.
     */
    public TaskBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Task} that we are building.
     */
    public TaskBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Description} of the {@code Task} that we are building.
     */
    public TaskBuilder withDescription(String description) {
        this.description = new Description(description);
        return this;
    }

    /**
     * Sets the {@code Deadline} of the {@code Task} that we are building.
     */
    public TaskBuilder withDeadline(String deadline) {
        this.deadline = new Deadline(deadline);
        return this;
    }

    public Task build() {
        return new Task(name, description, deadline, tags);
    }

}
