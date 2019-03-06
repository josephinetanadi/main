package seedu.project.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.project.logic.CommandHistory;
import seedu.project.model.Project;
import seedu.project.model.Model;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Address book has been cleared!";


    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        model.setProject(new Project());
        model.commitProject();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
