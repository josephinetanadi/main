package seedu.project.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.project.commons.core.Messages;
import seedu.project.logic.CommandHistory;
import seedu.project.logic.LogicManager;
import seedu.project.model.Model;

/**
 * Clears the project.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String COMMAND_ALIAS = "cl";
    public static final String MESSAGE_SUCCESS = "Project has been cleared!";


    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        if (LogicManager.getState()) {
            /*Project clearedProject = new Project();
            clearedProject.setName(model.getProject().getName().toString());
            clearedProject.setTasks(new ArrayList<>());
            model.setProject(model.getSelectedProject(), clearedProject);*/
            model.clearTasks();
            model.commitProject();
            model.commitProjectList();
            return new CommandResult(MESSAGE_SUCCESS);
        } else {
            return new CommandResult(String.format(Messages.MESSAGE_GO_TO_TASK_LEVEL, COMMAND_WORD));
        }
    }
}
