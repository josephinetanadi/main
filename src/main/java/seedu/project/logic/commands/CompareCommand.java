package seedu.project.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.project.commons.core.Messages;
import seedu.project.commons.core.index.Index;
import seedu.project.logic.CommandHistory;
import seedu.project.logic.commands.exceptions.CommandException;
import seedu.project.model.Model;
import seedu.project.model.task.Task;

/**
 * Compares a task identified using it's displayed index from the project.
 */
public class CompareCommand extends Command {

    public static final String COMMAND_WORD = "compare";
    public static final String COMMAND_ALIAS = "c";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Compares the task identified by the index number used in the displayed task list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_COMPARE_TASK_SUCCESS = "Displaying differences:\n"
            + "Compared Task: %1$s \nCompared To: %2$s\n";
    public static final String MESSAGE_COMPARE_TASK_FAILURE = "There is no previous version of this task";


    private final Index targetIndex;

    public CompareCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Task> lastShownList = model.getFilteredTaskList();

        Task tempTask;

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        Task taskToCompare = lastShownList.get(targetIndex.getZeroBased());
        //tempTask = model.compareTask(taskToCompare);
        List<String> tempString = model.compareTask(taskToCompare);;
        model.commitProject();
        if (tempString != null) {
            return new CommandResult(String.format(MESSAGE_COMPARE_TASK_SUCCESS, tempString.get(0), tempString.get(1)));
        } else {
            return new CommandResult(MESSAGE_COMPARE_TASK_FAILURE);
        }

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof CompareCommand // instanceof handles nulls
                && targetIndex.equals(((CompareCommand) other).targetIndex)); // state check
    }
}
