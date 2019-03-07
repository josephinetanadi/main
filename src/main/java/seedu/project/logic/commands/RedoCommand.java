package seedu.project.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.project.model.Model.PREDICATE_SHOW_ALL_TASKS;

import seedu.project.logic.CommandHistory;
import seedu.project.logic.commands.exceptions.CommandException;
import seedu.project.model.Model;

/**
 * Reverts the {@code model}'s project to its previously undone state.
 */
public class RedoCommand extends Command {

    public static final String COMMAND_WORD = "redo";
    public static final String MESSAGE_SUCCESS = "Redo success!";
    public static final String MESSAGE_FAILURE = "No more commands to redo!";

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        if (!model.canRedoProject()) {
            throw new CommandException(MESSAGE_FAILURE);
        }

        model.redoProject();
        model.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
