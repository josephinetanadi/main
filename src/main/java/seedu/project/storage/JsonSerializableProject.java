package seedu.project.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.project.commons.exceptions.IllegalValueException;
import seedu.project.model.Project;
import seedu.project.model.ReadOnlyProject;
import seedu.project.model.task.Task;

/**
 * An Immutable Project that is serializable to JSON format.
 */
@JsonRootName(value = "project")
class JsonSerializableProject {

    public static final String MESSAGE_DUPLICATE_PERSON = "Tasks list contains duplicate task(s).";

    private final List<JsonAdaptedTask> tasks = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableProject} with the given tasks.
     */
    @JsonCreator
    public JsonSerializableProject(@JsonProperty("tasks") List<JsonAdaptedTask> tasks) {
        this.tasks.addAll(tasks);
    }

    /**
     * Converts a given {@code ReadOnlyProject} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created
     *               {@code JsonSerializableProject}.
     */
    public JsonSerializableProject(ReadOnlyProject source) {
        tasks.addAll(source.getTaskList().stream().map(JsonAdaptedTask::new).collect(Collectors.toList()));
    }

    /**
     * Converts this address book into the model's {@code Project} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public Project toModelType() throws IllegalValueException {
        Project project = new Project();
        for (JsonAdaptedTask jsonAdaptedTask : tasks) {
            Task task = jsonAdaptedTask.toModelType();
            if (project.hasTask(task)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            project.addTask(task);
        }
        return project;
    }

}
