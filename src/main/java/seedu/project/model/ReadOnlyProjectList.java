package seedu.project.model;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import seedu.project.model.project.Project;
import seedu.project.model.tag.GroupTag;

/**
 * Unmodifiable view of all projects
 */
public interface ReadOnlyProjectList extends Observable {

    /**
     * Returns an unmodifiable view of the tasks list. This list will not contain
     * any duplicate tasks.
     */
    ObservableList<Project> getProjectList();
    ObservableList<GroupTag> getGroupTagList();
}
