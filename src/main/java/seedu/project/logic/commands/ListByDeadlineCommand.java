package seedu.project.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.*;

import javafx.collections.ObservableList;
import seedu.project.logic.CommandHistory;
import seedu.project.model.Model;
import seedu.project.model.task.Task;

/**
 * Lists all tasks sorted ascending according to deadline.
 */
public class ListByDeadlineCommand extends Command {
    public static final String COMMAND_WORD = "listDeadline";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Shows a list of all available tasks sorted by the deadline" + "Example: " + COMMAND_WORD;
    public static final String MESSAGE_SUCCESS_TASK = "List sorted and displayed";

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        //A list of all tasks in the project
        ObservableList<Task> filteredTasks = model.getFilteredTaskList();

        //Sorts list of all tasks according to toCompare specified in task class
        Collections.sort(filteredTasks);
        System.out.println(filteredTasks);

        return new CommandResult(MESSAGE_SUCCESS_TASK);
    }
}
