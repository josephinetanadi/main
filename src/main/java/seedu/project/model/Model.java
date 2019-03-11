package seedu.project.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableList;
import seedu.project.commons.core.GuiSettings;
import seedu.project.model.task.Task;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Task> PREDICATE_SHOW_ALL_TASKS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' project file path.
     */
    Path getProjectFilePath();

    /**
     * Sets the user prefs' project file path.
     */
    void setProjectFilePath(Path projectFilePath);

    /**
     * Replaces project data with the data in {@code project}.
     */
    void setProject(ReadOnlyProject project);

    /** Returns the Project */
    ReadOnlyProject getProject();

    /**
     * Returns true if a task with the same identity as {@code task} exists in the
     * project.
     */
    boolean hasTask(Task task);

    /**
     * Deletes the given task. The task must exist in the project.
     */
    void deleteTask(Task target);

    /**
     * Compares the given task with the last edited version. The task must exist in the project.
     */
    Task compareTask(Task target);

    /**
     * Adds the given task. {@code task} must not already exist in the project.
     */
    void addTask(Task task);

    /**
     * Replaces the given task {@code target} with {@code editedTask}.
     * {@code target} must exist in the project. The task identity of
     * {@code editedTask} must not be the same as another existing task in the
     * project.
     */
    void setTask(Task target, Task editedTask);

    /** Returns an unmodifiable view of the filtered task list */
    ObservableList<Task> getFilteredTaskList();

    /**
     * Updates the filter of the filtered task list to filter by the given
     * {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredTaskList(Predicate<Task> predicate);

    /**
     * Returns true if the model has previous project states to restore.
     */
    boolean canUndoProject();

    /**
     * Returns true if the model has undone project states to restore.
     */
    boolean canRedoProject();

    /**
     * Restores the model's project to its previous state.
     */
    void undoProject();

    /**
     * Restores the model's project to its previously undone state.
     */
    void redoProject();

    /**
     * Saves the current project state for undo/redo.
     */
    void commitProject();

    /**
     * Selected task in the filtered task list. null if no task is selected.
     */
    ReadOnlyProperty<Task> selectedTaskProperty();

    /**
     * Returns the selected task in the filtered task list. null if no task is
     * selected.
     */
    Task getSelectedTask();

    /**
     * Sets the selected task in the filtered task list.
     */
    void setSelectedTask(Task task);
}
