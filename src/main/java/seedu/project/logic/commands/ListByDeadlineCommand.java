package seedu.project.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;

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
    public static final String MESSAGE_SUCCESS_TASK = "List sorted: %1$s";


    @Override
    public CommandResult execute(Model model, CommandHistory history) {

        requireNonNull(model);
        int size;

        String toPrint = "\n";

        //A list of all tasks in the project
        ObservableList<Task> filteredTasks = model.getFilteredTaskList();
        size = filteredTasks.size();

        ArrayList<Task> toSortList = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            toSortList.add(filteredTasks.get(i));
        }

        //Sorts list of all tasks according to toCompare specified in task class
        Collections.sort(toSortList);

        for (Task tempFilteredTask : toSortList) {
            toPrint = toPrint + tempFilteredTask.getName() + "\n" + tempFilteredTask.getDescription()
                    + "\n" + tempFilteredTask.getDeadline() + "\n\n";
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS_TASK, toPrint));
    }
}
