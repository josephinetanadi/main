package seedu.project.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.project.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.project.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.project.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.project.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.project.logic.CommandHistory;
import seedu.project.logic.commands.exceptions.CommandException;
import seedu.project.model.Model;
import seedu.project.model.project.Project;
import seedu.project.model.task.Task;

/**
 * Adds a task to the project.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";
    public static final String COMMAND_ALIAS = "a";

    public static final String PROJECT_MESSAGE_USAGE = COMMAND_WORD + ": Adds a project to the project list. "
            + "Parameters: " + PREFIX_NAME + "NAME " + "Example: " + COMMAND_WORD + " " + PREFIX_NAME
            + "CS2113T Project ";
    public static final String TASK_MESSAGE_USAGE = COMMAND_WORD + ": Adds a task to the project. " + "Parameters: "
            + PREFIX_NAME + "NAME " + PREFIX_DESCRIPTION + "DESCRIPTION " + PREFIX_DEADLINE + "DEADLINE "
            + "[" + PREFIX_TAG + "TAG]...\n" + "Example: " + COMMAND_WORD + " " + PREFIX_NAME + "Sample Task 1 "
            + PREFIX_DESCRIPTION + "This is a sample description " + PREFIX_DEADLINE + "01-01-2019 "
            + PREFIX_TAG + "SAMPLE " + PREFIX_TAG + "submission";

    public static final String MESSAGE_SUCCESS_PROJECT = "New project added: %1$s";
    public static final String MESSAGE_SUCCESS_TASK = "New task added: %1$s";
    public static final String MESSAGE_DUPLICATE_PROJECT = "This project already exists in the project list";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the project";

    private final Object toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Project}
     */
    public AddCommand(Object object) {
        requireNonNull(object);
        toAdd = object;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        if (toAdd instanceof Project) {
            if (model.hasProject((Project) toAdd)) {
                throw new CommandException(MESSAGE_DUPLICATE_PROJECT);
            }

            model.addProject((Project) toAdd);
            model.commitProjectList();
            return new CommandResult(String.format(MESSAGE_SUCCESS_PROJECT, toAdd));
        } else {
            if (model.hasTask((Task) toAdd)) {
                throw new CommandException(MESSAGE_DUPLICATE_TASK);
            }

            model.addTask((Task) toAdd);

            int taskId = ((Task) toAdd).getTaskId();
            history.addHistoryTaskId(Integer.toString(taskId));

            model.setProject(model.getSelectedProject(), (Project) model.getProject());
            model.commitProject();
            return new CommandResult(String.format(MESSAGE_SUCCESS_TASK, toAdd));
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddCommand // instanceof handles nulls
                        && (toAdd.equals(((AddCommand) other).toAdd)));
    }
}
