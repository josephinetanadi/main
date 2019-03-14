package seedu.project.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.project.model.project.Name;
import seedu.project.model.project.Project;
import seedu.project.model.task.Task;

/**
 * A utility class to help with building Task objects.
 */
public class ProjectBuilder {

    public static final String DEFAULT_NAME = "TYPICAL";

    private Name name;
    private List<Task> tasks;

    public ProjectBuilder() {
        name = new Name(DEFAULT_NAME);
        tasks = new ArrayList<>();
    }

    /**
     * Initializes the ProjectBuilder with the data of {@code projectToCopy}.
     */
    public ProjectBuilder(Project projectToCopy) {
        name = projectToCopy.getName();
        tasks = projectToCopy.getTaskList();
    }

    /**
     * Sets the {@code Name} of the {@code Project} that we are building.
     */
    public ProjectBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code task} into a {@code List<Task>} and set it to the {@code Project} that we are building.
     */
    public ProjectBuilder withTask(Task ... task) {
        this.tasks = new ArrayList<>(Arrays.asList(task));
        return this;
    }

    public Project build() {
        return new Project(name, tasks);
    }

}
