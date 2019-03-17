package seedu.project.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import seedu.project.commons.core.index.Index;
import seedu.project.logic.CommandHistory;
import seedu.project.model.Model;
import seedu.project.model.task.Task;

/**
 * Lists all the commands entered by user for a chosen task.
 */
public class TaskHistoryCommand extends Command {

    public static final String COMMAND_WORD = "taskhistory";
    public static final String COMMAND_ALIAS = "th";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Compares the task identified by the index number used in the displayed task list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Entered commands for task %1$s (from most recent to earliest):\n%2$s";
    public static final String MESSAGE_NO_HISTORY = "You have not yet entered any commands.";

    //public static final String MESSAGE_COMPARE_TASK_SUCCESS = "Compared Task: %1$s \nCompared To: %2$s\n";

    private final Index targetIndex;

    public TaskHistoryCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(history);


        ArrayList<String> previousCommands = new ArrayList<>(history.getHistory());
        ArrayList<String> previousCommandsTaskId = new ArrayList<>(history.getHistoryTaskId());
        List<Task> lastShownList = model.getFilteredTaskList();


        //        if (targetIndex.getZeroBased() >= lastShownList.size()) {
        //        throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        //}

        if (previousCommands.isEmpty()) {
            return new CommandResult(MESSAGE_NO_HISTORY);
        }

        Task chosenTask = lastShownList.get(targetIndex.getZeroBased());

        String taskId = Integer.toString(chosenTask.getTaskId());
        int commandTaskIdSize = previousCommandsTaskId.size();
        ArrayList<String> commandList = new ArrayList<>();

        for (int i = 0; i < commandTaskIdSize; i++) {
            if (previousCommandsTaskId.get(i).equals(taskId)) {
                commandList.add(previousCommands.get(i));
            }
        }

        Collections.reverse(commandList);
        return new CommandResult(String.format(MESSAGE_SUCCESS, targetIndex.getOneBased(),
                String.join("\n", commandList)));
    }

}
