package seedu.project.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.project.logic.CommandHistory;
import seedu.project.logic.LogicManager;
import seedu.project.model.Model;
import seedu.project.model.project.Project;

/**
 * Lists all tasks in the project to the user.
 */
public class ListProjectCommand extends Command {

    public static final String COMMAND_WORD = "listproject";
    public static final String COMMAND_ALIAS = "lp";

    public static final String MESSAGE_SUCCESS_PROJECT = "Listed all projects";

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        if (LogicManager.getState()) {
            LogicManager.setState(false);
            //sync versionedProject with versionedProjectList
            model.setProject(model.getSelectedProject(), (Project) model.getProject());
            model.setSelectedProject(model.getFilteredProjectList().get(0));
            model.setSelectedTask(null);
        }
        return new CommandResult(MESSAGE_SUCCESS_PROJECT);
    }
}
