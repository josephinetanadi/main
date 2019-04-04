package seedu.project.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.collections.ObservableList;
import seedu.project.commons.core.Messages;
import seedu.project.logic.CommandHistory;
import seedu.project.logic.LogicManager;
import seedu.project.logic.commands.exceptions.CommandException;
import seedu.project.model.Model;
import seedu.project.model.tag.Tag;
import seedu.project.model.task.Task;

/**
 * Lists all unique tags and their tasks to the user.
 */
public class ListTagCommand extends Command {
    public static final String COMMAND_WORD = "listtag";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Shows a list of all available tags prefix and its related tasks. " + "Example: " + COMMAND_WORD;

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        ObservableList<Task> filteredTasks = model.getFilteredTaskList();

        if (!LogicManager.getState()) {
            throw new CommandException(String.format(Messages.MESSAGE_GO_TO_TASK_LEVEL, COMMAND_WORD));
        }

        // get set of unique tags
        List<Tag> allTags = new ArrayList<>();
        filteredTasks.forEach(entry -> {
            entry.getTags().forEach(tag -> allTags.add(tag));
        });
        Set<Tag> uniqueTagSet = new HashSet<>(allTags);

        // convert uniqueTag set to list
        List<Tag> uniqueTagList = new ArrayList<>();
        uniqueTagList.addAll(uniqueTagSet);

        // arrange print string
        List<String> stringToPrint = new ArrayList<>();
        uniqueTagList.forEach(entry -> {
            stringToPrint.add(entry.toStringWithoutBrackets());
            filteredTasks.forEach(task -> {
                if (task.getTags().contains(entry)) {
                    stringToPrint.add(task.getName().toString());
                }
            });
        });

        return new CommandResult(stringToPrint.toString());
    }

}
