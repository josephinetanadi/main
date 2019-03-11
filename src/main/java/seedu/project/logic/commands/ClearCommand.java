package seedu.project.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.project.logic.CommandHistory;
import seedu.project.model.Model;
import seedu.project.model.project.Project;

/**
 * Clears the project.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Project has been cleared!";


    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        model.setProject(new Project());
        model.commitProject();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
