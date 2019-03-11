package seedu.project.logic.commands;

import static seedu.project.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.project.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.project.logic.commands.CommandTestUtil.deleteFirstTask;
import static seedu.project.testutil.TypicalTasks.getTypicalProject;

import org.junit.Before;
import org.junit.Test;

import seedu.project.logic.CommandHistory;
import seedu.project.model.Model;
import seedu.project.model.ModelManager;
import seedu.project.model.UserPrefs;

public class UndoCommandTest {

    private final Model model = new ModelManager(getTypicalProject(), new UserPrefs());
    private final Model expectedModel = new ModelManager(getTypicalProject(), new UserPrefs());
    private final CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        // set up of models' undo/redo history
        deleteFirstTask(model);
        deleteFirstTask(model);

        deleteFirstTask(expectedModel);
        deleteFirstTask(expectedModel);
    }

    @Test
    public void execute() {
        // multiple undoable states in model
        expectedModel.undoProject();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // single undoable state in model
        expectedModel.undoProject();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // no undoable states in model
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
    }
}
