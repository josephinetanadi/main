package seedu.project.testutil;

import seedu.project.model.ProjectList;
import seedu.project.model.project.Project;

/**
 * A utility class to help with building ProjectList objects. Example usage:
 * <br>
 * {@code Project ab = new ProjectListBuilder().withProject("CS2101 Project").build();}
 */
public class ProjectListBuilder {

    private ProjectList projectList;

    public ProjectListBuilder() {
        projectList = new ProjectList();
    }

    public ProjectListBuilder(ProjectList projectList) {
        this.projectList = projectList;
    }

    /**
     * Adds a new {@code Task} to the {@code Project} that we are building.
     */
    public ProjectListBuilder withProject(Project project) {
        projectList.addProject(project);
        return this;
    }

    public ProjectList build() {
        return projectList;
    }
}
