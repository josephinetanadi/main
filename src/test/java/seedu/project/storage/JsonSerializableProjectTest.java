package seedu.project.storage;

import static org.junit.Assert.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.project.commons.exceptions.IllegalValueException;
import seedu.project.commons.util.JsonUtil;
import seedu.project.model.project.Project;
import seedu.project.testutil.TypicalTasks;

public class JsonSerializableProjectTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableProjectTest");
    private static final Path TYPICAL_TASKS_FILE = TEST_DATA_FOLDER.resolve("typicalTasksProject.json");
    private static final Path INVALID_TASK_FILE = TEST_DATA_FOLDER.resolve("invalidTaskProject.json");
    private static final Path DUPLICATE_TASK_FILE = TEST_DATA_FOLDER.resolve("duplicateTaskProject.json");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void toModelType_typicalTasksFile_success() throws Exception {
        JsonSerializableProject dataFromFile = JsonUtil.readJsonFile(TYPICAL_TASKS_FILE, JsonSerializableProject.class)
                .get();
        Project projectFromFile = dataFromFile.toModelType();
        Project typicalTasksProject = TypicalTasks.getTypicalProject();
        assertEquals(projectFromFile, typicalTasksProject);
    }

    @Test
    public void toModelType_invalidTaskFile_throwsIllegalValueException() throws Exception {
        JsonSerializableProject dataFromFile = JsonUtil.readJsonFile(INVALID_TASK_FILE, JsonSerializableProject.class)
                .get();
        thrown.expect(IllegalValueException.class);
        dataFromFile.toModelType();
    }

    @Test
    public void toModelType_duplicateTasks_throwsIllegalValueException() throws Exception {
        JsonSerializableProject dataFromFile = JsonUtil
                .readJsonFile(DUPLICATE_TASK_FILE, JsonSerializableProject.class).get();
        thrown.expect(IllegalValueException.class);
        thrown.expectMessage(JsonSerializableProject.MESSAGE_DUPLICATE_TASK);
        dataFromFile.toModelType();
    }

}
