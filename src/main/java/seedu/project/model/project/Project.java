package seedu.project.model.project;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.collections.ObservableList;
import seedu.project.commons.util.InvalidationListenerManager;
import seedu.project.model.task.Task;
import seedu.project.model.task.UniqueTaskList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSameTask comparison)
 */
public class Project implements ReadOnlyProject {

    // Identity fields
    private Name name;

    private final UniqueTaskList tasks;
    private final InvalidationListenerManager invalidationListenerManager = new InvalidationListenerManager();

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        name = new Name("");
        tasks = new UniqueTaskList();
    }

    public Project() {}

    public Project(Name name) {
        this();
        this.name = name;
    }

    public Project(ReadOnlyProject toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    /**
     * Creates an Project using the Tasks in the {@code toBeCopied}
     */
    public Project(ReadOnlyProject toBeCopied, Name name) {
        this();
        this.name = name;
        resetData(toBeCopied);
    }

    public Name getName() {
        return name;
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the task list with {@code tasks}.
     * {@code tasks} must not contain duplicate tasks.
     */
    public void setTasks(List<Task> tasks) {
        this.tasks.setTasks(tasks);
        indicateModified();
    }

    /**
     * Resets the existing data of this {@code Project} with {@code newData}.
     */
    public void resetData(ReadOnlyProject newData) {
        requireNonNull(newData);
        setTasks(newData.getTaskList());
    }

    //// task-level operations

    /**
     * Returns true if a task with the same identity as {@code task} exists in the project.
     */
    public boolean hasTask(Task task) {
        requireNonNull(task);
        return tasks.contains(task);
    }

    /**
     * Adds a task to the project.
     * The task must not already exist in the project.
     */
    public void addTask(Task p) {
        tasks.add(p);
        indicateModified();
    }

    /**
     * Replaces the given task {@code target} in the list with {@code editedTask}.
     * {@code target} must exist in the project.
     * The task identity of {@code editedTask} must not be the same as another existing task in the project.
     */
    public void setTask(Task target, Task editedTask) {
        requireNonNull(editedTask);

        tasks.setTask(target, editedTask);
        indicateModified();
    }

    /**
     * Removes {@code key} from this {@code Project}.
     * {@code key} must exist in the project.
     */
    public void removeTask(Task key) {
        tasks.remove(key);
        indicateModified();
    }

    /**
     * Returns true if both tasks of the same name have at least one other identity field that is the same.
     * This defines a weaker notion of equality between two tasks.
     */
    public boolean isSameProject(Project otherProject) {
        if (otherProject == this) {
            return true;
        }

        return otherProject != null
                && otherProject.getName().equals(getName());
    }

    @Override
    public void addListener(InvalidationListener listener) {
        invalidationListenerManager.addListener(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        invalidationListenerManager.removeListener(listener);
    }

    /**
     * Notifies listeners that the project has been modified.
     */
    protected void indicateModified() {
        invalidationListenerManager.callListeners(this);
    }

    //// util methods

    @Override
    public String toString() {
        return tasks.asUnmodifiableObservableList().size() + " tasks";
        // TODO: refine later
    }

    @Override
    public ObservableList<Task> getTaskList() {
        return tasks.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Project // instanceof handles nulls
                && tasks.equals(((Project) other).tasks));
    }

    @Override
    public int hashCode() {
        return tasks.hashCode();
    }
}
