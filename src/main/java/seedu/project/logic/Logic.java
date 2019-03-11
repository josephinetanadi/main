package seedu.project.logic;

import java.nio.file.Path;

import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableList;
import seedu.project.commons.core.GuiSettings;
import seedu.project.logic.commands.CommandResult;
import seedu.project.logic.commands.exceptions.CommandException;
import seedu.project.logic.parser.exceptions.ParseException;
import seedu.project.model.project.ReadOnlyProject;
import seedu.project.model.task.Task;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    /**
     * Returns the Project.
     *
     * @see seedu.project.model.Model#getProject()
     */
    ReadOnlyProject getProject();

    /** Returns an unmodifiable view of the filtered list of tasks */
    ObservableList<Task> getFilteredTaskList();

    /**
     * Returns an unmodifiable view of the list of commands entered by the user.
     * The list is ordered from the least recent command to the most recent command.
     */
    ObservableList<String> getHistory();

    /**
     * Returns the user prefs' project file path.
     */
    Path getProjectFilePath();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Set the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Selected task in the filtered task list.
     * null if no task is selected.
     *
     * @see seedu.project.model.Model#selectedTaskProperty()
     */
    ReadOnlyProperty<Task> selectedTaskProperty();

    /**
     * Sets the selected task in the filtered task list.
     *
     * @see seedu.project.model.Model#setSelectedTask(Task)
     */
    void setSelectedTask(Task task);
}
