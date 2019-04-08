package seedu.project.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.project.model.Model.PREDICATE_SHOW_ALL_TASKS;

import java.util.List;

import seedu.project.commons.core.Messages;
import seedu.project.commons.core.index.Index;
import seedu.project.logic.CommandHistory;
import seedu.project.logic.LogicManager;
import seedu.project.logic.commands.exceptions.CommandException;
import seedu.project.model.Model;
import seedu.project.model.project.Project;
import seedu.project.model.project.VersionedProject;
import seedu.project.model.tag.Tag;
import seedu.project.model.task.Task;

/**
 * Marks an existing task as completed and removes it from UI display
 */
public class CompletedCommand extends Command {
    public static final String COMMAND_WORD = "completed";
    public static final String COMMAND_ALIAS = "cpt";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Completes a task and deletes it from view. "
            + "Parameters: INDEX (must be a positive integer) "
            + "Example: " + COMMAND_WORD + " 1";
    public static final String MESSAGE_TASK_ALREADY_COMPLETED = "Task is already completed.";
    public static final String MESSAGE_COMPLETED_SUCCESS = " Task completed.";

    private final Index index;
    /**
     * @param index of the task in the filtered task list to be completed
     */
    public CompletedCommand(Index index) {
        requireNonNull(index);
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Task> lastShownList = model.getFilteredTaskList();
        int taskId;

        if (!LogicManager.getState()) {
            throw new CommandException(String.format(Messages.MESSAGE_GO_TO_TASK_LEVEL, COMMAND_WORD));
        } else {
            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
            }

            Task taskToComplete = new Task(lastShownList.get(index.getZeroBased()).getName(),
                    lastShownList.get(index.getZeroBased()).getDescription(),
                    lastShownList.get(index.getZeroBased()).getDeadline(),
                    lastShownList.get(index.getZeroBased()).getTags());

            Task targetTask = lastShownList.get(index.getZeroBased());

            taskId = targetTask.getTaskId();
            taskToComplete.updateTaskId(taskId);
            history.addHistoryTaskId(Integer.toString(taskId));

            if (taskToComplete.getTags().contains(new Tag("completed"))) {
                throw new CommandException(MESSAGE_TASK_ALREADY_COMPLETED);
            } else {
                taskToComplete.addTag(new Tag("completed"));

                model.setTask(targetTask, taskToComplete);

                model.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);

                model.commitProject();
                if (model.getProject().getClass().equals(VersionedProject.class)) {
                    model.setProject(model.getSelectedProject(), (VersionedProject) model.getProject());
                } else {
                    model.setProject(model.getSelectedProject(), (Project) model.getProject());
                }
                model.commitProjectList();

                return new CommandResult(String.format(MESSAGE_COMPLETED_SUCCESS, taskToComplete));
            }
        }
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CompletedCommand)) {
            return false;
        }

        // state check
        CompletedCommand e = (CompletedCommand) other;
        return index.equals(e.index);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("index: ").append(index);
        return builder.toString();
    }
}
