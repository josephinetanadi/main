package seedu.project.logic.commands;

import static org.junit.Assert.assertEquals;
import static seedu.project.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.project.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.project.logic.commands.CommandTestUtil.showProjectAtIndex;
import static seedu.project.testutil.TypicalIndexes.INDEX_FIRST_TASK;
import static seedu.project.testutil.TypicalIndexes.INDEX_SECOND_TASK;
import static seedu.project.testutil.TypicalTasks.getTypicalProjectList;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.project.commons.core.Messages;
import seedu.project.commons.core.index.Index;
import seedu.project.logic.CommandHistory;
import seedu.project.logic.LogicManager;
import seedu.project.model.Model;
import seedu.project.model.ModelManager;
import seedu.project.model.ProjectList;
import seedu.project.model.ReadOnlyProjectList;
import seedu.project.model.UserPrefs;
import seedu.project.model.project.Project;
import seedu.project.storage.JsonProjectListStorage;

/**
 * Contains integration tests (interaction with the Model) for {@code ExportCommand}.
 */
public class ExportCommandTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    private Model model = new ModelManager(getTypicalProjectList(), new Project(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalProjectList(), new Project(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();
    private JsonProjectListStorage projectListStorage;

    @Before
    public void setUp() {
        projectListStorage = new JsonProjectListStorage(getTempFilePath("export"));
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.getRoot().toPath().resolve(fileName);
    }

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        Set<Index> idxToExport = new HashSet<>();
        idxToExport.add(INDEX_FIRST_TASK);
        idxToExport.add(INDEX_SECOND_TASK);
        assertExecutionSuccess(idxToExport, projectListStorage.getProjectListFilePath());
        ReadOnlyProjectList retrieved = projectListStorage.readProjectList().get();

        ProjectList expectedOutput = new ProjectList();
        expectedOutput.addProject(model.getFilteredProjectList().get(0));
        expectedOutput.addProject(model.getFilteredProjectList().get(1));
        assertEquals(expectedOutput, new ProjectList(retrieved));
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Set<Index> idxToExport = new HashSet<>();
        idxToExport.add(Index.fromOneBased(model.getFilteredProjectList().size() + 1));
        assertExecutionFailure(idxToExport, projectListStorage.getProjectListFilePath(),
                Messages.MESSAGE_INVALID_PROJECT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() throws Exception {
        showProjectAtIndex(model, INDEX_FIRST_TASK);

        Set<Index> idxToExport = new HashSet<>();
        idxToExport.add(INDEX_FIRST_TASK);
        assertExecutionSuccess(idxToExport, projectListStorage.getProjectListFilePath());

        ProjectList expectedOutput = new ProjectList();
        expectedOutput.addProject(model.getFilteredProjectList().get(0));
        ReadOnlyProjectList retrieved = projectListStorage.readProjectList().get();
        assertEquals(expectedOutput, new ProjectList(retrieved));
    }

    @Test
    public void execute_invalidIndexFilteredList_failure() {
        showProjectAtIndex(model, INDEX_FIRST_TASK);

        Set<Index> idxToExport = new HashSet<>();
        idxToExport.add(Index.fromOneBased(model.getFilteredProjectList().size() + 1));
        assertExecutionFailure(idxToExport, projectListStorage.getProjectListFilePath(),
                Messages.MESSAGE_INVALID_PROJECT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validSelectedProjectUnfilteredList_success() throws Exception {
        model.setSelectedProject(model.getFilteredProjectList().get(0));
        LogicManager.setState(true);
        assertExecutionSuccess(projectListStorage.getProjectListFilePath());

        ProjectList expectedOutput = new ProjectList();
        expectedOutput.addProject(model.getFilteredProjectList().get(0));
        ReadOnlyProjectList retrieved = projectListStorage.readProjectList().get();
        assertEquals(expectedOutput, new ProjectList(retrieved));
    }

    @Test
    public void execute_unselectedProjectUnfilteredList_failure() throws Exception {
        LogicManager.setState(false);
        model.setSelectedProject(null);
        assertExecutionFailure(projectListStorage.getProjectListFilePath(), Messages.MESSAGE_GO_TO_TASK_LEVEL);
    }

    /**
     * Executes a {@code SelectCommand} with the given {@code index},
     * and checks that the model's selected task is set to the task at {@code index} in the filtered task list.
     */
    private void assertExecutionSuccess(Path path) {
        ExportCommand exportCommand = new ExportCommand(path);
        String expectedMessage = String.format(ExportCommand.MESSAGE_SUCCESS_PROJECT, 1);
        assertCommandSuccess(exportCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    private void assertExecutionSuccess(Set<Index> idx, Path path) {
        ExportCommand exportCommand = new ExportCommand(idx, path);
        String expectedMessage = String.format(ExportCommand.MESSAGE_SUCCESS_PROJECT, idx.size());
        assertCommandSuccess(exportCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    /**
     * Executes a {@code SelectCommand} with the given {@code index}, and checks that a {@code CommandException}
     * is thrown with the {@code expectedMessage}.
     */
    private void assertExecutionFailure(Path path, String expectedMessage) {
        ExportCommand exportCommand = new ExportCommand(path);
        assertCommandFailure(exportCommand, model, commandHistory, expectedMessage);
    }

    private void assertExecutionFailure(Set<Index> idx, Path path, String expectedMessage) {
        ExportCommand exportCommand = new ExportCommand(idx, path);
        assertCommandFailure(exportCommand, model, commandHistory, expectedMessage);
    }
}
