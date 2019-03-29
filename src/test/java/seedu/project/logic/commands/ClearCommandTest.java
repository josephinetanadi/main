package seedu.project.logic.commands;

import static seedu.project.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.project.testutil.TypicalTasks.getTypicalProjectList;

import java.util.ArrayList;

import org.junit.Test;

import seedu.project.commons.core.Messages;
import seedu.project.logic.CommandHistory;
import seedu.project.logic.LogicManager;
import seedu.project.model.Model;
import seedu.project.model.ModelManager;
import seedu.project.model.UserPrefs;
import seedu.project.model.project.Project;


public class ClearCommandTest {

    private CommandHistory commandHistory = new CommandHistory();

    /**
     * for the sake of it
     */
    //to be done
    //@Test
    public void execute_emptyProject_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();
        //expectedModel.commitProjectList();

        assertCommandSuccess(new ClearCommand(), model, commandHistory,
                Messages.MESSAGE_GO_TO_TASK_LEVEL, expectedModel);
    }

    @Test
    public void execute_nonEmptyProject_success() {
        Model model = new ModelManager(getTypicalProjectList(), new Project(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalProjectList(), new Project(), new UserPrefs());

        //model.setProjectList(new ProjectList());
        //model.commitProject();

        //expectedModel.setProjectList(new ProjectList());
        //expectedModel.commitProject();

        model.setProject(model.getFilteredProjectList().get(0));
        model.setSelectedProject(model.getFilteredProjectList().get(0));

        expectedModel.setProject(model.getFilteredProjectList().get(0));
        expectedModel.setSelectedProject(model.getFilteredProjectList().get(0));

        LogicManager.setState(true);

        Project clearedProject = new Project();
        clearedProject.setName(expectedModel.getProject().getName().toString());
        clearedProject.setTasks(new ArrayList<>());
        //expectedModel.setProject(model.getSelectedProject(), clearedProject);
        expectedModel.clearTasks();
        expectedModel.commitProject();
        expectedModel.commitProjectList();

        assertCommandSuccess(new ClearCommand(), model, commandHistory, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

}
