package seedu.project.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.project.model.ProjectList;
import seedu.project.model.project.Project;

/**
 * A utility class to help with building ProjectList objects. Example usage:
 * <br>
 * {@code Project ab = new ProjectListBuilder().withProject("CS2101 Project").build();}
 */
public class ProjectListBuilder {

    private List<Project> projects;

    public ProjectListBuilder() {
        projects = new ArrayList<>();
    }

    public ProjectListBuilder(ProjectList projectListToCopy) {
        projects = projectListToCopy.getProjectList();
    }

    /**
     * Adds a new {@code Task} to the {@code Project} that we are building.
     */
    public ProjectListBuilder withProject(Project project) {
        this.projects = new ArrayList<>(Arrays.asList(project));
        return this;
    }

    public ProjectList build() {
        return new ProjectList(projects);
    }
}
