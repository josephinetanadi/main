package seedu.project.model.project;

import static java.util.Objects.requireNonNull;
import static seedu.project.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javafx.beans.InvalidationListener;
import javafx.collections.ObservableList;
import seedu.project.commons.util.InvalidationListenerManager;
import seedu.project.model.Name;
import seedu.project.model.tag.Tag;
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
    public Project(Name name, List<Task> tasks) {
        this();
        requireAllNonNull(name, tasks);
        this.name = name;
        setTasks(tasks);
    }

    public void setName(String name) {
        this.name = new Name(name);
    }

    /**
     * Replaces the contents of the name with {@code newData}.
     */
    public void resetName(ReadOnlyProject newData) {
        requireNonNull(newData);
        setName(newData.getName().toString());
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

    public void clearTasks() {
        Project clearedProject = new Project();
        clearedProject.setName(getName().toString());
        clearedProject.setTasks(new ArrayList<>());
        resetData(clearedProject);
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
    public void addTask(Task t) {
        tasks.add(t);
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
     * Removes tag from a specific Task.
     */
    private void removeTagTask(Tag tag, Task task) {
        Set<Tag> newTags = new HashSet<>(task.getTags());

        if (!newTags.remove(tag)) {
            return;
        }

        Task update = new Task(task.getName(), task.getDescription(), task.getDeadline(), newTags);
        setTask(task, update);
        removeTask(update);
    }

    public void removeTag(Tag tag) {
        tasks.forEach(task -> removeTagTask(tag, task));
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
        return getName().toString();
    }

    @Override
    public Name getName() {
        return name;
    }

    @Override
    public ObservableList<Task> getTaskList() {
        return tasks.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Project // instanceof handles nulls
                && name.equals(((Project) other).getName())
                && tasks.equals(((Project) other).tasks));
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, tasks);
    }

    /**
     * Returns index of a {@code taskID} in the list.
     * {@code taskID} must exist in the project
     */
    public int getIndex(int taskId) {
        return tasks.getIndexOfTask(taskId);
    }

}
