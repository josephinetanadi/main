package seedu.project.logic.commands;

import static seedu.project.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.project.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.project.testutil.TypicalTasks.getTypicalProjectList;

import org.junit.Before;
import org.junit.Test;

import seedu.project.logic.CommandHistory;
import seedu.project.logic.LogicManager;
import seedu.project.model.Model;
import seedu.project.model.ModelManager;
import seedu.project.model.UserPrefs;
import seedu.project.model.project.Project;
import seedu.project.model.task.Task;
import seedu.project.testutil.TaskBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;
    private CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalProjectList(), new Project(), new UserPrefs());
        model.setProject(model.getFilteredProjectList().get(0));
        model.setSelectedProject(model.getFilteredProjectList().get(0));
        LogicManager.setState(true);
    }

    @Test
    public void execute_newTask_success() {
        Task validTask = new TaskBuilder().build();

        Model expectedModel = new ModelManager(model.getProjectList(), new Project(), new UserPrefs());
        expectedModel.setProject(expectedModel.getFilteredProjectList().get(0));
        expectedModel.setSelectedProject(expectedModel.getFilteredProjectList().get(0));
        LogicManager.setState(true);

        expectedModel.addTask(validTask);
        expectedModel.setProject(expectedModel.getSelectedProject(), (Project) expectedModel.getProject());
        expectedModel.commitProject();

        assertCommandSuccess(new AddCommand(validTask), model, commandHistory,
                String.format(AddCommand.MESSAGE_SUCCESS_TASK, validTask), expectedModel);
    }

    @Test
    public void execute_duplicateTask_throwsCommandException() {
        Task taskInList = model.getProject().getTaskList().get(0);
        assertCommandFailure(new AddCommand(taskInList), model, commandHistory,
                AddCommand.MESSAGE_DUPLICATE_TASK);
    }

}
