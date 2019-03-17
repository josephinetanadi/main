package seedu.project.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.project.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.project.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.project.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.project.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.project.logic.CommandHistory;
import seedu.project.logic.commands.exceptions.CommandException;
import seedu.project.model.Model;
import seedu.project.model.task.Task;

/**
 * Adds a task to the project.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";
    public static final String COMMAND_ALIAS = "a";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a task to the project. " + "Parameters: "
            + PREFIX_NAME + "NAME " + PREFIX_DESCRIPTION + "DESCRIPTION " + PREFIX_DEADLINE + "DEADLINE "
            + "[" + PREFIX_TAG + "TAG]...\n" + "Example: " + COMMAND_WORD + " " + PREFIX_NAME + "Report submission "
            + PREFIX_DESCRIPTION + " " + PREFIX_DEADLINE + "1-1-2011 "
            + PREFIX_TAG + "cs2101 " + PREFIX_TAG + "submission";

    public static final String MESSAGE_SUCCESS = "New task added: %1$s";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the project";

    private final Task toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Task}
     */
    public AddCommand(Task task) {
        requireNonNull(task);
        toAdd = task;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        if (model.hasTask(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_TASK);
        }

        model.addTask(toAdd);

        int taskId = toAdd.getTaskId();
        history.addHistoryTaskId(Integer.toString(taskId));

        model.commitProject();
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddCommand // instanceof handles nulls
                        && toAdd.equals(((AddCommand) other).toAdd));
    }
}
