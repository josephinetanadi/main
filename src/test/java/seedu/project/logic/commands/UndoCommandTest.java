package seedu.project.logic.commands;

import static seedu.project.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.project.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.project.logic.commands.CommandTestUtil.deleteFirstTask;
import static seedu.project.testutil.TypicalTasks.getTypicalProject;
import static seedu.project.testutil.TypicalTasks.getTypicalProjectList;

import org.junit.Before;
import org.junit.Test;

import seedu.project.logic.CommandHistory;
import seedu.project.logic.LogicManager;
import seedu.project.model.Model;
import seedu.project.model.ModelManager;
import seedu.project.model.UserPrefs;

public class UndoCommandTest {

    private final Model model = new ModelManager(getTypicalProjectList(), getTypicalProject(), new UserPrefs());
    private final Model expectedModel = new ModelManager(getTypicalProjectList(), getTypicalProject(), new UserPrefs());
    private final CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        model.setProject(model.getFilteredProjectList().get(0));
        model.setSelectedProject(model.getFilteredProjectList().get(0));
        expectedModel.setProject(model.getFilteredProjectList().get(0));
        expectedModel.setSelectedProject(model.getFilteredProjectList().get(0));

        LogicManager.setState(true);


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
