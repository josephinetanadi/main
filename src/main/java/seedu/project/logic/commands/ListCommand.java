package seedu.project.logic.commands;

import static java.util.Objects.requireNonNull;
import seedu.project.logic.LogicManager;
import static seedu.project.model.Model.PREDICATE_SHOW_ALL_PROJECTS;
import static seedu.project.model.Model.PREDICATE_SHOW_ALL_TASKS;

import seedu.project.logic.CommandHistory;
import seedu.project.model.Model;

/**
 * Lists all tasks in the project to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS_PROJECT = "Listed all projects";
    public static final String MESSAGE_SUCCESS_TASK = "Listed all tasks";

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        if(!LogicManager.getState()) {
            model.updateFilteredProjectList(PREDICATE_SHOW_ALL_PROJECTS);
            return new CommandResult(MESSAGE_SUCCESS_PROJECT);
        } else {
            model.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
            return new CommandResult(MESSAGE_SUCCESS_TASK);
        }
    }
}
