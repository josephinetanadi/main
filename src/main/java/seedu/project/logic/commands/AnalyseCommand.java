package seedu.project.logic.commands;

import static java.util.Objects.requireNonNull;

import javafx.collections.ObservableList;
import seedu.project.commons.core.Messages;
import seedu.project.logic.CommandHistory;
import seedu.project.logic.LogicManager;
import seedu.project.logic.commands.exceptions.CommandException;
import seedu.project.model.Model;
import seedu.project.model.project.Project;
import seedu.project.model.tag.Tag;
import seedu.project.model.task.Task;

/**
 * Finds and lists all completed tasks in project.
 */
public class AnalyseCommand extends Command {

    public static final String COMMAND_WORD = "analyse";
    public static final String COMMAND_ALIAS = "an";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Finds all completed tasks of a project and displays them as a list with index numbers.\n"
            + "Example: " + COMMAND_WORD;

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        ObservableList<Project> filteredProjects = model.getFilteredProjectList();
        String toPrint = "";

        if (!LogicManager.getState()) {
            for (Project project: filteredProjects) {
                int countCompleted = 0;
                int numTasksPerProject = 0;
                float percentageCompleted;
                toPrint += project.getName().toString() + ": ";
                ObservableList<Task> filteredTasks = project.getTaskList();

                for (Task task : filteredTasks) {
                    if (task.getTags().contains(new Tag("completed"))) {
                        countCompleted += 1;
                    }
                    numTasksPerProject += 1;
                }
                toPrint += countCompleted + " tasks completed. ";

                if (numTasksPerProject == 0) {
                    percentageCompleted = 0;
                } else {
                    percentageCompleted = ((float) countCompleted / (float) numTasksPerProject) * 100;
                }

                toPrint += "(Percentage of project completed: " + String.format("%.1f", percentageCompleted) + "%)\n";
            }
            return new CommandResult(toPrint);
        } else {
            throw new CommandException(String.format(Messages.MESSAGE_RETURN_TO_PROJECT_LEVEL, COMMAND_WORD));
        }
    }
}
