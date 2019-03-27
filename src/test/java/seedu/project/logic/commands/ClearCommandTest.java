package seedu.project.logic.commands;

import static seedu.project.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.project.testutil.TypicalTasks.getTypicalProject;
import static seedu.project.testutil.TypicalTasks.getTypicalProjectList;

import org.junit.Test;

import seedu.project.commons.core.Messages;
import seedu.project.logic.CommandHistory;
import seedu.project.model.Model;
import seedu.project.model.ModelManager;
import seedu.project.model.ProjectList;
import seedu.project.model.UserPrefs;
import seedu.project.model.project.Project;

public class ClearCommandTest {

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_emptyProject_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();
        //expectedModel.commitProjectList();

        assertCommandSuccess(new ClearCommand(), model, commandHistory, Messages.MESSAGE_GO_TO_TASK_LEVEL, expectedModel);
    }

    @Test
    public void execute_nonEmptyProject_success() {
        Model model = new ModelManager(getTypicalProjectList(), getTypicalProject(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalProjectList(), getTypicalProject(), new UserPrefs());
        model.setProjectList(new ProjectList());
        model.commitProject();
        expectedModel.setProjectList(new ProjectList());
        expectedModel.commitProject();

        assertCommandSuccess(new ClearCommand(), model, commandHistory, Messages.MESSAGE_GO_TO_TASK_LEVEL, expectedModel);
    }

}
