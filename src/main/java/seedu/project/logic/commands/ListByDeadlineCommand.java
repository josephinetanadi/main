package seedu.project.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;
import java.lang.String;

import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import seedu.project.logic.CommandHistory;
import seedu.project.logic.LogicManager;
import seedu.project.logic.commands.exceptions.CommandException;
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
    public static final String MESSAGE_WRONG_LAYER = "Please use this command in a specific project";

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);

        if (!LogicManager.getState()) {
            return new CommandResult(MESSAGE_WRONG_LAYER);
        }
        else {
            //ObservableList of all filteredTasks
            ObservableList<Task> filteredTasks = model.getFilteredTaskList();

            Comparator<Task> taskComparator = new Comparator<Task>() {
                @Override
                public int compare(Task o1, Task o2) {
                    return (o1.getDeadline().toString()).compareTo(o2.getDeadline().toString());
                }
            };

            int size = filteredTasks.size();

            //filteredTasks.sorted(taskComparator);

            SortedList<Task> sortedList = filteredTasks.sorted(taskComparator);

            //System.out.println(size);

            System.out.println("SortedList:");

            for(int i = 0; i < size; i++) {
                System.out.println(((sortedList.get(i)).getName()).toString());
            }

            //System.out.println("FilteredTasks:");

            //for(int i = 0; i < size; i++) {
            //    System.out.println(((filteredTasks.get(i)).getName()).toString());
            //}

            return new CommandResult(MESSAGE_SUCCESS_TASK);
        }
    }
}
