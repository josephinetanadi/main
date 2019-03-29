package seedu.project.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.project.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.project.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.project.logic.commands.CommandTestUtil.showTaskAtIndex;
import static seedu.project.testutil.TypicalIndexes.INDEX_FIRST_TASK;
import static seedu.project.testutil.TypicalIndexes.INDEX_SECOND_TASK;
import static seedu.project.testutil.TypicalTasks.getTypicalProjectList;

import org.junit.Test;

import seedu.project.commons.core.Messages;
import seedu.project.commons.core.index.Index;
import seedu.project.logic.CommandHistory;
import seedu.project.logic.LogicManager;
import seedu.project.model.Model;
import seedu.project.model.ModelManager;
import seedu.project.model.UserPrefs;
import seedu.project.model.project.Project;
import seedu.project.model.task.Task;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(getTypicalProjectList(), new Project(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_validIndexUnfilteredList_success() {
        //Project projectToDelete = model.getFilteredProjectList().get(INDEX_FIRST_TASK.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_TASK);

        //String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_TASK_SUCCESS, projectToDelete);

        Model expectedModel = new ModelManager(model.getProjectList(), model.getProject(), new UserPrefs());

        model.setProject(model.getFilteredProjectList().get(0));
        model.setSelectedProject(model.getFilteredProjectList().get(0));

        expectedModel.setProject(model.getFilteredProjectList().get(0));
        expectedModel.setSelectedProject(model.getFilteredProjectList().get(0));

        Task taskToDelete = model.getFilteredTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_TASK_SUCCESS, taskToDelete);

        LogicManager.setState(true);

        //expectedModel.deleteProject(projectToDelete);
        expectedModel.deleteTask(taskToDelete);
        //expectedModel.commitProjectList();
        expectedModel.commitProject();

        assertCommandSuccess(deleteCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredTaskList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        LogicManager.setState(true);

        assertCommandFailure(deleteCommand, model, commandHistory, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredProjectList_success() {
        model.setProject(model.getFilteredProjectList().get(0));
        model.setSelectedProject(model.getFilteredProjectList().get(0));

        showTaskAtIndex(model, INDEX_FIRST_TASK);

        Task taskToDelete = model.getFilteredTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_TASK);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_TASK_SUCCESS, taskToDelete);

        Model expectedModel = new ModelManager(model.getProjectList(), new Project(), new UserPrefs());

        expectedModel.setProject(model.getFilteredProjectList().get(0));
        expectedModel.setSelectedProject(model.getFilteredProjectList().get(0));

        //Project projectToDelete = model.getFilteredProjectList().get(INDEX_FIRST_TASK.getZeroBased());
        //expectedModel.deleteProject(projectToDelete);
        //String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PROJECT_SUCCESS, projectToDelete);
        LogicManager.setState(true);
        expectedModel.deleteTask(taskToDelete);
        expectedModel.commitProject();
        showNoTask(expectedModel);

        assertCommandSuccess(deleteCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        model.setProject(model.getFilteredProjectList().get(0));
        model.setSelectedProject(model.getFilteredProjectList().get(0));

        showTaskAtIndex(model, INDEX_FIRST_TASK);

        Index outOfBoundIndex = INDEX_SECOND_TASK;
        // ensures that outOfBoundIndex is still in bounds of project list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getProject().getTaskList().size());
        LogicManager.setState(true);

        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, commandHistory, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {

        model.setProject(model.getFilteredProjectList().get(0));
        model.setSelectedProject(model.getFilteredProjectList().get(0));

        Model expectedModel = new ModelManager(model.getProjectList(), new Project(), new UserPrefs());

        expectedModel.setProject(model.getFilteredProjectList().get(0));
        expectedModel.setSelectedProject(model.getFilteredProjectList().get(0));

        Task taskToDelete = model.getFilteredTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_TASK);
        LogicManager.setState(true);

        expectedModel.deleteTask(taskToDelete);
        expectedModel.commitProject();

        // delete -> first task deleted
        deleteCommand.execute(model, commandHistory);

        // undo -> reverts project back to previous state and filtered task list to show all tasks
        expectedModel.undoProject();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first task deleted again
        expectedModel.redoProject();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        model.setProject(model.getFilteredProjectList().get(0));
        model.setSelectedProject(model.getFilteredProjectList().get(0));

        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredTaskList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        LogicManager.setState(true);

        // execution failed -> project state not added into model
        assertCommandFailure(deleteCommand, model, commandHistory, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);

        // single project state in model -> undoCommand and redoCommand fail
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Deletes a {@code Task} from a filtered list.
     * 2. Undo the deletion.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously deleted task in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the deletion. This ensures {@code RedoCommand} deletes the task object regardless of indexing.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_sameTaskDeleted() throws Exception {

        model.setProject(model.getFilteredProjectList().get(0));
        model.setSelectedProject(model.getFilteredProjectList().get(0));

        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_TASK);
        Model expectedModel = new ModelManager(model.getProjectList(), model.getProject(), new UserPrefs());

        expectedModel.setProject(model.getFilteredProjectList().get(0));
        expectedModel.setSelectedProject(model.getFilteredProjectList().get(0));

        LogicManager.setState(true);

        showTaskAtIndex(model, INDEX_SECOND_TASK);
        Task taskToDelete = model.getFilteredTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        expectedModel.deleteTask(taskToDelete);
        expectedModel.commitProject();

        // delete -> deletes second task in unfiltered task list / first task in filtered task list
        deleteCommand.execute(model, commandHistory);

        // undo -> reverts project back to previous state and filtered task list to show all tasks
        expectedModel.undoProject();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        assertNotEquals(taskToDelete, model.getFilteredTaskList().get(INDEX_FIRST_TASK.getZeroBased()));
        // redo -> deletes same second task in unfiltered task list
        expectedModel.redoProject();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() {

        //LogicManager.setState(true);

        DeleteCommand deleteFirstCommand = new DeleteCommand(INDEX_FIRST_TASK);
        DeleteCommand deleteSecondCommand = new DeleteCommand(INDEX_SECOND_TASK);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(INDEX_FIRST_TASK);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different task -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoTask(Model model) {
        model.updateFilteredTaskList(p -> false);

        //LogicManager.setState(true);

        assertTrue(model.getFilteredTaskList().isEmpty());
    }
    /**
     * Updates {@code model}'s project filtered list to show no one.
     */
    private void showNoProject(Model model) {
        model.updateFilteredProjectList(p -> false);

        assertTrue(model.getFilteredProjectList().isEmpty());
    }
}
