package seedu.project.testutil;

import seedu.project.model.project.Project;
import seedu.project.model.task.Task;

/**
 * A utility class to help with building Addressbook objects. Example usage:
 * <br>
 * {@code Project ab = new ProjectBuilder().withTask("John", "Doe").build();}
 */
public class ProjectBuilder {

    private Project project;

    public ProjectBuilder() {
        project = new Project();
    }

    public ProjectBuilder(Project project) {
        this.project = project;
    }

    /**
     * Adds a new {@code Task} to the {@code Project} that we are building.
     */
    public ProjectBuilder withTask(Task task) {
        project.addTask(task);
        return this;
    }

    public Project build() {
        return project;
    }
}
