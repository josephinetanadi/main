package seedu.project.storage;

import static org.junit.Assert.assertEquals;
import static seedu.project.storage.JsonAdaptedProject.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.project.testutil.TypicalTasks.CS2101;
import static seedu.project.testutil.TypicalTasks.CS2101_MILESTONE;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import seedu.project.commons.exceptions.IllegalValueException;
import seedu.project.model.Name;
import seedu.project.model.project.Project;
import seedu.project.model.task.Task;
import seedu.project.testutil.Assert;

public class JsonAdaptedProjectTest {

    @Test
    public void toModelType_validProjectDetails_returnsProject() throws Exception {
        JsonAdaptedProject project = new JsonAdaptedProject(CS2101);
        assertEquals(CS2101, project.toModelType());
    }

    @Test
    public void toModelType_nullTasks_returnsProject() throws Exception {
        List<JsonAdaptedTask> tasks = new ArrayList<>();
        JsonAdaptedProject project = new JsonAdaptedProject(CS2101.getName().toString(), tasks);

        Project expectedProject = new Project(CS2101.getName(), new ArrayList<Task>());
        assertEquals(expectedProject, project.toModelType());
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedTask task = new JsonAdaptedTask(CS2101_MILESTONE);
        List<JsonAdaptedTask> tasks = new ArrayList<>();
        tasks.add(task);

        JsonAdaptedProject project = new JsonAdaptedProject(null, tasks);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, project::toModelType);
    }

}
