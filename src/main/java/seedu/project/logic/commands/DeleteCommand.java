package seedu.project.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.project.commons.core.Messages;
import seedu.project.commons.core.index.Index;
import seedu.project.logic.CommandHistory;
import seedu.project.logic.LogicManager;
import seedu.project.logic.commands.exceptions.CommandException;
import seedu.project.model.Model;
import seedu.project.model.project.Project;
import seedu.project.model.task.Task;

/**
 * Deletes a task identified using it's displayed index from the project.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";
    public static final String COMMAND_ALIAS = "d";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the task identified by the index number used in the displayed project / task list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_TASK_SUCCESS = "Deleted Task: %1$s";
    public static final String MESSAGE_DELETE_PROJECT_SUCCESS = "Deleted Project: %1$s";

    private final Index targetIndex;

    public DeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        if (!LogicManager.getState()) {
            List<Project> lastShownList = model.getFilteredProjectList();

            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PROJECT_DISPLAYED_INDEX);
            }

            Project projectToDelete = lastShownList.get(targetIndex.getZeroBased());
            model.deleteProject(projectToDelete);
            model.commitProjectList();
            return new CommandResult(String.format(MESSAGE_DELETE_PROJECT_SUCCESS, projectToDelete));
        } else {
            List<Task> lastShownList = model.getFilteredTaskList();

            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
            }

            Task taskToDelete = lastShownList.get(targetIndex.getZeroBased());

            int taskId = taskToDelete.getTaskId();
            history.addHistoryTaskId(Integer.toString(taskId));

            model.deleteTask(taskToDelete);
            model.commitProject();
            return new CommandResult(String.format(MESSAGE_DELETE_TASK_SUCCESS, taskToDelete));
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteCommand // instanceof handles nulls
                && targetIndex.equals(((DeleteCommand) other).targetIndex)); // state check
    }
}
