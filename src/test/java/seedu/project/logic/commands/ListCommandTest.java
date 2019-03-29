package seedu.project.logic.commands;

import static seedu.project.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.project.logic.commands.CommandTestUtil.showTaskAtIndex;
import static seedu.project.testutil.TypicalIndexes.INDEX_FIRST_TASK;
import static seedu.project.testutil.TypicalTasks.getTypicalProject;
import static seedu.project.testutil.TypicalTasks.getTypicalProjectList;

import org.junit.Before;
import org.junit.Test;

import seedu.project.logic.CommandHistory;
import seedu.project.model.Model;
import seedu.project.model.ModelManager;
import seedu.project.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;
    private CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalProjectList(), getTypicalProject(), new UserPrefs());
        expectedModel = new ModelManager(model.getProjectList(), model.getProject(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        assertCommandSuccess(new ListCommand(), model, commandHistory, ListCommand.MESSAGE_SUCCESS_TASK, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        showTaskAtIndex(model, INDEX_FIRST_TASK);
        assertCommandSuccess(new ListCommand(), model, commandHistory, ListCommand.MESSAGE_SUCCESS_TASK, expectedModel);
    }
}
