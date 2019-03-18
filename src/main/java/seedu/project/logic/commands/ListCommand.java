package seedu.project.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.project.model.Model.PREDICATE_SHOW_ALL_TASKS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.collections.ObservableList;
import seedu.project.model.tag.Tag;
import seedu.project.model.task.Task;
import seedu.project.logic.CommandHistory;
import seedu.project.model.Model;

/**
 * Lists all tasks in the project to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all tasks";

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);

        // TODO: Seperate list tag into its own command
        // TODO: To display in UI
        // TODO: Make code into OOP style
        ObservableList<Task> filteredTasks = model.getFilteredTaskList();

        System.out.println(filteredTasks);

        // get a list of unqiue tags
        List<Tag> allTags = new ArrayList<>();
        filteredTasks.forEach(entry -> {
            entry.getTags().forEach(tag -> allTags.add(tag));
        });
        Set<Tag> uniqueTags = new HashSet<Tag>(allTags);
        ArrayList<String> hello = new ArrayList<>();

        filteredTasks.forEach(entry -> {
            Set<Tag> copyUniqueTag = new HashSet<>(uniqueTags);

            // !Collections.disjoint(uniqueTags, entry.getTags())
            if (copyUniqueTag.retainAll(entry.getTags())) {
                if (!hello.contains(copyUniqueTag.toString())) {
                    hello.add(copyUniqueTag.toString());
                    System.out.print("\n" + copyUniqueTag);
                }
                System.out.print(" " + entry.getName() + ", ");
            }
        });
        System.out.println();

        model.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
