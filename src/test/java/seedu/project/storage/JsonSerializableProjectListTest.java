package seedu.project.storage;

import static org.junit.Assert.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.project.commons.exceptions.IllegalValueException;
import seedu.project.commons.util.JsonUtil;
import seedu.project.model.ProjectList;
import seedu.project.model.task.exceptions.DuplicateTaskException;
import seedu.project.testutil.TypicalTasks;

public class JsonSerializableProjectListTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableProjectListTest");
    private static final Path TYPICAL_PROJECT_FILE = TEST_DATA_FOLDER.resolve("typicalProject.json");
    private static final Path EMPTY_PROJECT_FILE = TEST_DATA_FOLDER.resolve("emptyProject.json");
    private static final Path INVALID_TASK_FILE = TEST_DATA_FOLDER.resolve("invalidTask.json");
    private static final Path DUPLICATE_PROJECT_FILE = TEST_DATA_FOLDER.resolve("duplicateProject.json");
    private static final Path DUPLICATE_TASK_FILE = TEST_DATA_FOLDER.resolve("duplicateTask.json");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void toModelType_typicalProjectFile_success() throws Exception {
        JsonSerializableProjectList dataFromFile = JsonUtil.readJsonFile(TYPICAL_PROJECT_FILE,
                JsonSerializableProjectList.class).get();
        ProjectList projectFromFile = dataFromFile.toModelType();
        ProjectList typicalProject = TypicalTasks.getTypicalProjectList();
        assertEquals(projectFromFile, typicalProject);
    }

    @Test
    public void toModelType_emptyProjectFile_success() throws Exception {
        JsonSerializableProjectList dataFromFile = JsonUtil.readJsonFile(EMPTY_PROJECT_FILE,
                JsonSerializableProjectList.class).get();
        ProjectList projectFromFile = dataFromFile.toModelType();
        ProjectList typicalProject = new ProjectList();
        assertEquals(projectFromFile.getProjectList(), typicalProject.getProjectList());
    }

    @Test
    public void toModelType_invalidTaskFile_throwsIllegalValueException() throws Exception {
        JsonSerializableProjectList dataFromFile = JsonUtil.readJsonFile(INVALID_TASK_FILE,
                JsonSerializableProjectList.class).get();
        thrown.expect(IllegalValueException.class);
        dataFromFile.toModelType();
    }

    @Test
    public void toModelType_duplicateProject_throwsIllegalValueException() throws Exception {
        JsonSerializableProjectList dataFromFile = JsonUtil
                .readJsonFile(DUPLICATE_PROJECT_FILE, JsonSerializableProjectList.class).get();
        thrown.expect(IllegalValueException.class);
        thrown.expectMessage(JsonSerializableProjectList.MESSAGE_DUPLICATE_PROJECT);
        dataFromFile.toModelType();
    }

    @Test
    public void toModelType_duplicateTasks_throwsIllegalValueException() throws Exception {
        JsonSerializableProjectList dataFromFile = JsonUtil
                .readJsonFile(DUPLICATE_TASK_FILE, JsonSerializableProjectList.class).get();
        thrown.expect(DuplicateTaskException.class);
        thrown.expectMessage(new DuplicateTaskException().getMessage());
        dataFromFile.toModelType();
    }

}
