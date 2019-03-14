package seedu.project.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.project.commons.exceptions.IllegalValueException;
import seedu.project.model.ProjectList;
import seedu.project.model.ReadOnlyProjectList;
import seedu.project.model.project.Project;

/**
 * An Immutable ProjectList that is serializable to JSON format.
 */
@JsonRootName(value = "project list")
class JsonSerializableProjectList {

    public static final String MESSAGE_DUPLICATE_PERSON = "Project list contains duplicate project(s).";

    private final List<JsonAdaptedProject> projects = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableProjectList} with the given tasks.
     */
    @JsonCreator
    public JsonSerializableProjectList(@JsonProperty("projects") List<JsonAdaptedProject> projects) {
        this.projects.addAll(projects);
    }

    /**
     * Converts a given {@code ReadOnlyProjectList} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created
     *               {@code JsonSerializableProjectList}.
     */
    public JsonSerializableProjectList(ReadOnlyProjectList source) {
        projects.addAll(source.getProjectList().stream().map(JsonAdaptedProject::new).collect(Collectors.toList()));
    }

    /**
     * Converts this project list into the model's {@code ProjectList} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public ProjectList toModelType() throws IllegalValueException {
        ProjectList projectList = new ProjectList();
        for (JsonAdaptedProject jsonAdaptedProject : projects) {
            Project project = jsonAdaptedProject.toModelType();
            if (projectList.hasProject(project)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            projectList.addProject(project);
        }
        return projectList;
    }

}
