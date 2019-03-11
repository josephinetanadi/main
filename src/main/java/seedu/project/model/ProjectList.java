package seedu.project.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.collections.ObservableList;
import seedu.project.commons.util.InvalidationListenerManager;
import seedu.project.model.project.Project;
import seedu.project.model.project.ReadOnlyProject;
import seedu.project.model.project.UniqueProjectList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSameTask comparison)
 */
public class ProjectList implements ReadOnlyProjectList {

    private final UniqueProjectList projects;
    private final InvalidationListenerManager invalidationListenerManager = new InvalidationListenerManager();

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        projects = new UniqueProjectList();
    }

    public ProjectList() {}

    /**
     * Creates a ProjectList using the Projects in the {@code toBeCopied}
     */
    public ProjectList(ReadOnlyProjectList toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the project list with {@code projects}.
     * {@code projects} must not contain duplicate projects.
     */
    public void setProjects(List<Project> projects) {
        this.projects.setProjects(projects);
        indicateModified();
    }

    /**
     * Resets the existing data of this {@code ProjectList} with {@code newData}.
     */
    public void resetData(ReadOnlyProjectList newData) {
        requireNonNull(newData);

        setProjects(newData.getProjectList());
    }

    //// task-level operations

    /**
     * Returns true if a project with the same identity as {@code project} exists in the project list.
     */
    public boolean hasProject(Project project) {
        requireNonNull(project);
        return projects.contains(project);
    }

    /**
     * Adds a project to the project list.
     * The project must not already exist in the project list.
     */
    public void addProject(Project p) {
        projects.add(p);
        indicateModified();
    }

    /**
     * Replaces the given project {@code target} in the list with {@code editedProject}.
     * {@code target} must exist in the project list.
     * The project identity of {@code editedProject} must not be the same as another existing project in the project list.
     */
    public void setProject(Project target, Project editedProject) {
        requireNonNull(editedProject);

        projects.setProject(target, editedProject);
        indicateModified();
    }

    /**
     * Removes {@code key} from this {@code ProjectList}.
     * {@code key} must exist in the project list.
     */
    public void removeProject(Project key) {
        projects.remove(key);
        indicateModified();
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
        return projects.asUnmodifiableObservableList().size() + " projects";
        // TODO: refine later
    }

    @Override
    public ObservableList<Project> getProjectList() {
        return projects.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Project // instanceof handles nulls
                && projects.equals(((ProjectList) other).projects));
    }

    @Override
    public int hashCode() {
        return projects.hashCode();
    }
}
