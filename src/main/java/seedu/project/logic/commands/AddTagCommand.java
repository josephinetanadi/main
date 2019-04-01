package seedu.project.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.project.logic.parser.CliSyntax.PREFIX_GROUPTAG;
import static seedu.project.model.Model.PREDICATE_SHOW_ALL_TASKS;

import java.util.List;

import seedu.project.commons.core.Messages;
import seedu.project.commons.core.index.Index;
import seedu.project.logic.CommandHistory;
import seedu.project.logic.commands.exceptions.CommandException;
import seedu.project.model.Model;
import seedu.project.model.project.Project;
import seedu.project.model.tag.GroupTag;
import seedu.project.model.tag.Tag;
import seedu.project.model.task.Task;

/**
 * Marks an existing task as completed and removes it from UI display
 */
public class AddTagCommand extends Command {
    public static final String COMMAND_ALIAS = "at";
    public static final String COMMAND_WORD = "addtag";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a tag to the task. "
            + "Parameters: INDEX (must be a positive integer) " + PREFIX_GROUPTAG + "GROUPTAG"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_GROUPTAG + "sample";
    public static final String MESSAGE_COMPLETED_SUCCESS = " Task completed.";

    private final Index index;
    private final String groupTag;
    /**
     * @param index of the task in the filtered task list to be completed
     */
    public AddTagCommand(Index index, String groupTag) {
        requireNonNull(index);
        this.index = index;
        this.groupTag = groupTag;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Task> lastShownList = model.getFilteredTaskList();

        int taskId;

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        Task targetTask = lastShownList.get(index.getZeroBased());

        Task taskToComplete = new Task(targetTask.getName(), targetTask.getDescription(),
                targetTask.getDeadline(), targetTask.getTags());
        taskId = targetTask.getTaskId();
        targetTask.updateTaskId(taskId);

        for (GroupTag groupTag : model.getProjectList().getGroupTagList()) {
            if (groupTag.getName().toString().equals(this.groupTag)) {
                for (Tag t : groupTag.getTags()) {
                    taskToComplete.addTag(t);
                }
            }
        }

        model.setTask(targetTask, taskToComplete);
        model.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);

        //history.addHistoryTaskId(Integer.toString(taskId));

        model.commitProject();

        //this will not work if user clicks on a different project while on task level??? lock UI at prev panel
        model.setProject(model.getSelectedProject(), (Project) model.getProject()); //sync project list
        model.commitProjectList();

        return new CommandResult(String.format(MESSAGE_COMPLETED_SUCCESS, taskToComplete));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddTagCommand)) {
            return false;
        }

        // state check
        AddTagCommand e = (AddTagCommand) other;
        return index.equals(e.index);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("index: ").append(index);
        return builder.toString();
    }
}
