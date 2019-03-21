package seedu.project.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.project.model.Model.PREDICATE_SHOW_ALL_TASKS;

import java.nio.file.Paths;
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
 * Selects a task identified using it's displayed index from the project.
 */
public class SelectCommand extends Command {

    public static final String COMMAND_WORD = "select";
    public static final String COMMAND_ALIAS = "s";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Selects the project / task identified by the index number used in the displayed project / task list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SELECT_PROJECT_SUCCESS = "Selected Project: %1$s";
    public static final String MESSAGE_SELECT_TASK_SUCCESS = "Selected Task: %1$s";

    private final Index targetIndex;

    public SelectCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        if(!LogicManager.getState()) {
            List<Project> filteredProjectList = model.getFilteredProjectList();

            if (targetIndex.getZeroBased() >= filteredProjectList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PROJECT_DISPLAYED_INDEX);
            }

            model.setProject(filteredProjectList.get(targetIndex.getZeroBased()));
            model.setSelectedProject(filteredProjectList.get(targetIndex.getZeroBased()));
            LogicManager.setState(true);
            //model.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
            //model.setSelectedTask(model.getFilteredTaskList().get(0));
            return new CommandResult(String.format(MESSAGE_SELECT_PROJECT_SUCCESS, targetIndex.getOneBased()));
        } else {
            List<Task> filteredTaskList = model.getFilteredTaskList();

            if (targetIndex.getZeroBased() >= filteredTaskList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
            }

            model.setSelectedTask(filteredTaskList.get(targetIndex.getZeroBased()));
            return new CommandResult(String.format(MESSAGE_SELECT_TASK_SUCCESS, targetIndex.getOneBased()));
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SelectCommand // instanceof handles nulls
                && targetIndex.equals(((SelectCommand) other).targetIndex)); // state check
    }
}
