package seedu.project.model;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import seedu.project.model.task.Task;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyProject extends Observable {

    /**
     * Returns an unmodifiable view of the tasks list.
     * This list will not contain any duplicate tasks.
     */
    ObservableList<Task> getTaskList();

}
