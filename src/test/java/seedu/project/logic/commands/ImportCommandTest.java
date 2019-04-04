package seedu.project.logic.commands;

import static org.junit.Assert.assertEquals;
import static seedu.project.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.project.logic.commands.CommandTestUtil.assertCommandSuccess;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.project.commons.exceptions.DataConversionException;
import seedu.project.commons.exceptions.IllegalValueException;
import seedu.project.commons.util.JsonUtil;
import seedu.project.logic.CommandHistory;
import seedu.project.model.Model;
import seedu.project.model.ModelManager;
import seedu.project.model.ProjectList;
import seedu.project.model.UserPrefs;
import seedu.project.model.project.Project;
import seedu.project.model.task.Deadline;
import seedu.project.model.task.exceptions.DuplicateTaskException;
import seedu.project.storage.JsonSerializableProjectList;

public class ImportCommandTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableProjectListTest");
    private static final Path TYPICAL_PROJECT_FILE = TEST_DATA_FOLDER.resolve("typicalProject.json");
    private static final Path EMPTY_PROJECT_FILE = TEST_DATA_FOLDER.resolve("emptyProject.json");
    private static final Path INVALID_TASK_FILE = TEST_DATA_FOLDER.resolve("invalidTask.json");
    private static final Path INVALID_PATH = TEST_DATA_FOLDER.resolve("invalidPath.json");
    private static final Path DUPLICATE_PROJECT_FILE = TEST_DATA_FOLDER.resolve("duplicateProject.json");
    private static final Path DUPLICATE_TASK_FILE = TEST_DATA_FOLDER.resolve("duplicateTask.json");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(new ProjectList(), new Project(), new UserPrefs());
    private Model expectedModel = new ModelManager(new ProjectList(), new Project(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_typicalProjectFile_success() throws Exception {
        JsonSerializableProjectList dataFromFile = JsonUtil.readJsonFile(TYPICAL_PROJECT_FILE,
                JsonSerializableProjectList.class).get();
        ProjectList projectListFromFile = dataFromFile.toModelType();
        for (Project project : projectListFromFile.getProjectList()) {
            expectedModel.addProject(project);
        }
        expectedModel.commitProjectList();

        assertExecutionSuccess(TYPICAL_PROJECT_FILE, projectListFromFile.getProjectList().size());
        assertEquals(model, expectedModel);
    }

    @Test
    public void execute_emptyProject_success() throws Exception {
        JsonSerializableProjectList dataFromFile = JsonUtil.readJsonFile(EMPTY_PROJECT_FILE,
                JsonSerializableProjectList.class).get();
        ProjectList projectListFromFile = dataFromFile.toModelType();
        expectedModel.commitProjectList();

        assertExecutionSuccess(EMPTY_PROJECT_FILE, projectListFromFile.getProjectList().size());
        assertEquals(model, expectedModel);
    }

    @Test
    public void execute_invalidTaskFile_throwsIllegalValueException() throws Exception {
        String expectedMessage = new DataConversionException(new IllegalValueException(Deadline.MESSAGE_CONSTRAINTS))
                .getMessage();
        assertExecutionFailure(INVALID_TASK_FILE, expectedMessage);
    }

    @Test
    public void execute_invalidPath_throwsIllegalValueException() throws Exception {
        assertExecutionFailure(INVALID_PATH, ImportCommand.MESSAGE_PATH_INVALID);
    }

    @Test
    public void execute_duplicateProject_throwsIllegalValueException() throws Exception {
        String expectedMessage = new DataConversionException(new IllegalValueException(JsonSerializableProjectList
                .MESSAGE_DUPLICATE_PROJECT)).getMessage();
        assertExecutionFailure(DUPLICATE_PROJECT_FILE, expectedMessage);
    }

    @Test
    public void execute_duplicateTask_throwsIllegalValueException() throws Exception {
        String expectedMessage = new DataConversionException(new DuplicateTaskException()).getMessage();
        assertExecutionFailure(DUPLICATE_TASK_FILE, expectedMessage);
    }

    /**
     * Executes a {@code SelectCommand} with the given {@code index},
     * and checks that the model's selected task is set to the task at {@code index} in the filtered task list.
     */
    private void assertExecutionSuccess(Path path, int size) {
        ImportCommand importCommand = new ImportCommand(path);
        String expectedMessage = String.format(ImportCommand.MESSAGE_SUCCESS_PROJECT, size);
        assertCommandSuccess(importCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    /**
     * Executes a {@code SelectCommand} with the given {@code index}, and checks that a {@code CommandException}
     * is thrown with the {@code expectedMessage}.
     */
    private void assertExecutionFailure(Path path, String expectedMessage) {
        ImportCommand importCommand = new ImportCommand(path);
        assertCommandFailure(importCommand, model, commandHistory, expectedMessage);
    }

}

