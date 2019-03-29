package seedu.project.logic.commands;

import static seedu.project.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.project.logic.commands.CommandTestUtil.showTaskAtIndex;
import static seedu.project.testutil.TypicalIndexes.INDEX_FIRST_TASK;
import static seedu.project.testutil.TypicalTasks.getTypicalProject;
import static seedu.project.testutil.TypicalTasks.getTypicalProjectList;

import org.junit.Before;
import org.junit.Test;

import seedu.project.logic.CommandHistory;
import seedu.project.logic.LogicManager;
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
        model.setProject(model.getFilteredProjectList().get(0));
        model.setSelectedProject(model.getFilteredProjectList().get(0));
        expectedModel.setProject(model.getFilteredProjectList().get(0));
        expectedModel.setSelectedProject(model.getFilteredProjectList().get(0));

        LogicManager.setState(true);

        assertCommandSuccess(new ListCommand(), model, commandHistory, ListCommand.MESSAGE_SUCCESS_TASK, expectedModel);
    }
    //filteredlist problem
    @Test
    public void execute_listIsFiltered_showsEverything() {
        model.setProject(model.getFilteredProjectList().get(0));
        model.setSelectedProject(model.getFilteredProjectList().get(0));
        expectedModel.setProject(model.getFilteredProjectList().get(0));
        expectedModel.setSelectedProject(model.getFilteredProjectList().get(0));

        LogicManager.setState(true);

        showTaskAtIndex(model,
                INDEX_FIRST_TASK);
        assertCommandSuccess(new ListCommand(), model, commandHistory, ListCommand.MESSAGE_SUCCESS_TASK, expectedModel);
    }
}
