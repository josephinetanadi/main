package seedu.project.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.project.model.Model.PREDICATE_SHOW_ALL_PROJECTS;
import static seedu.project.model.Model.PREDICATE_SHOW_ALL_TASKS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.collections.ObservableList;
import seedu.project.model.tag.Tag;
import seedu.project.model.task.Task;
import seedu.project.logic.CommandHistory;
import seedu.project.logic.LogicManager;
import seedu.project.model.Model;

/**
 * Lists all tasks in the project to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";
    public static final String COMMAND_ALIAS = "l";

    public static final String MESSAGE_SUCCESS_PROJECT = "Listed all projects";
    public static final String MESSAGE_SUCCESS_TASK = "Listed all tasks";

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        if (!LogicManager.getState()) {
            model.updateFilteredProjectList(PREDICATE_SHOW_ALL_PROJECTS);
            return new CommandResult(MESSAGE_SUCCESS_PROJECT);
        } else {
            model.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
            return new CommandResult(MESSAGE_SUCCESS_TASK);
        }
    }
}
