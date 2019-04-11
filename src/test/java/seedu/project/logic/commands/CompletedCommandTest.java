package seedu.project.logic.commands;

import static seedu.project.logic.commands.CommandTestUtil.VALID_NAME_CP2106;
import static seedu.project.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.project.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.project.model.Model.PREDICATE_SHOW_ALL_TASKS;
import static seedu.project.testutil.TypicalIndexes.INDEX_FIRST_TASK;
import static seedu.project.testutil.TypicalTasks.getTypicalProjectList;

import org.junit.Test;

import seedu.project.commons.core.Messages;
import seedu.project.commons.core.index.Index;
import seedu.project.logic.CommandHistory;
import seedu.project.logic.LogicManager;
import seedu.project.model.Model;
import seedu.project.model.ModelManager;
import seedu.project.model.ProjectList;
import seedu.project.model.UserPrefs;
import seedu.project.model.project.Project;
import seedu.project.model.task.Task;
import seedu.project.testutil.TaskBuilder;

public class CompletedCommandTest {
    private Model model = new ModelManager(getTypicalProjectList(), new Project(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_invalidIndex_failure() {
        model.setProject(model.getFilteredProjectList().get(0));
        model.setSelectedProject(model.getFilteredProjectList().get(0));

        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredTaskList().size() + 1);
        CompletedCommand completedCommand = new CompletedCommand(outOfBoundIndex);

        assertCommandFailure(completedCommand, model, commandHistory, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndex_success() {
        model.setProject(model.getFilteredProjectList().get(0));
        model.setSelectedProject(model.getFilteredProjectList().get(0));

        LogicManager.setState(true);

        Index indexLastTask = Index.fromOneBased(model.getFilteredTaskList().size());
        CompletedCommand completedCommand = new CompletedCommand(indexLastTask);

        Model expectedModel = new ModelManager(
                new ProjectList(model.getProjectList()), new Project(model.getProject()), new UserPrefs());
        Task lastTask = expectedModel.getFilteredTaskList().get(indexLastTask.getZeroBased());
        Task taskToComplete = new TaskBuilder(lastTask).withTags("TUTORIAL", "completed").build();
        String expectedMessage = String.format(completedCommand.MESSAGE_COMPLETED_SUCCESS, taskToComplete.getName());

        int taskId = lastTask.getTaskId();
        taskToComplete.updateTaskId(taskId);
        commandHistory.addHistoryTaskId(Integer.toString(taskId));

        expectedModel.setProject(model.getFilteredProjectList().get(0));
        expectedModel.setSelectedProject(model.getFilteredProjectList().get(0));

        expectedModel.setTask(lastTask, taskToComplete);
        expectedModel.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
        expectedModel.commitProject();
        expectedModel.setProject(expectedModel.getSelectedProject(), (Project) expectedModel.getProject());
        expectedModel.commitProjectList();

        assertCommandSuccess(completedCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexAlreadyCompleted_failure() {
        model.setProject(model.getFilteredProjectList().get(0));
        model.setSelectedProject(model.getFilteredProjectList().get(0));

        LogicManager.setState(true);

        Task firstTask = model.getFilteredTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        TaskBuilder taskInList = new TaskBuilder(firstTask);
        Task taskAlreadyCompleted = taskInList.withName(VALID_NAME_CP2106).withTags("completed").build();
        model.addTask(taskAlreadyCompleted);
        Index lastIndex = Index.fromOneBased(model.getFilteredTaskList().size());
        CompletedCommand completedCommand = new CompletedCommand(lastIndex);

        assertCommandFailure(completedCommand, model, commandHistory, CompletedCommand.MESSAGE_TASK_ALREADY_COMPLETED);
    }

    @Test
    public void execute_notAtTaskLevel_failure() {
        LogicManager.setState(false);
        model.setSelectedTask(null);

        CompletedCommand completedCommand = new CompletedCommand(INDEX_FIRST_TASK);
        String expectedMessage = String.format(Messages.MESSAGE_GO_TO_TASK_LEVEL, CompletedCommand.COMMAND_WORD);

        assertCommandFailure(completedCommand, model, commandHistory, expectedMessage);
    }
}
