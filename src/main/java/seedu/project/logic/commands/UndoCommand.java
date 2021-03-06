package seedu.project.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.project.model.Model.PREDICATE_SHOW_ALL_PROJECTS;
import static seedu.project.model.Model.PREDICATE_SHOW_ALL_TASKS;

import seedu.project.logic.CommandHistory;
import seedu.project.logic.LogicManager;
import seedu.project.logic.commands.exceptions.CommandException;
import seedu.project.model.Model;

/**
 * Reverts the {@code model}'s project to its previous state.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";
    public static final String COMMAND_ALIAS = "u";
    public static final String MESSAGE_SUCCESS = "Undo success!";
    public static final String MESSAGE_FAILURE = "No more commands to undo!";

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        if (!LogicManager.getState()) {

            if (!model.canUndoProjectList()) {
                throw new CommandException(MESSAGE_FAILURE);
            }

            model.undoProjectList();
            model.updateFilteredProjectList(PREDICATE_SHOW_ALL_PROJECTS);
            return new CommandResult(MESSAGE_SUCCESS);

        } else {

            if (!model.canUndoProject()) {
                throw new CommandException(MESSAGE_FAILURE);
            }

            model.undoProject();
            model.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
            return new CommandResult(MESSAGE_SUCCESS);
        }
    }
}
