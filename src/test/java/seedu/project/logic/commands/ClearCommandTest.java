package seedu.project.logic.commands;

import static seedu.project.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.project.testutil.TypicalTasks.getTypicalProject;

import org.junit.Test;

import seedu.project.logic.CommandHistory;
import seedu.project.model.Project;
import seedu.project.model.Model;
import seedu.project.model.ModelManager;
import seedu.project.model.UserPrefs;

public class ClearCommandTest {

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_emptyProject_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();
        expectedModel.commitProject();

        assertCommandSuccess(new ClearCommand(), model, commandHistory, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyProject_success() {
        Model model = new ModelManager(getTypicalProject(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalProject(), new UserPrefs());
        expectedModel.setProject(new Project());
        expectedModel.commitProject();

        assertCommandSuccess(new ClearCommand(), model, commandHistory, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

}
