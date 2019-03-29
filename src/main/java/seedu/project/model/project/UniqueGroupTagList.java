package seedu.project.model.project;

import static java.util.Objects.requireNonNull;
import static seedu.project.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.project.model.project.exceptions.DuplicateGroupTagException;
import seedu.project.model.project.exceptions.GroupTagNotFoundException;
import seedu.project.model.tag.GroupTag;

/**
 * A list of GroupTags that enforces uniqueness between its elements and does not allow nulls.
 * A GroupTag is considered unique by comparing using {@code GroupTag#isSameGroupTag(GroupTag)}. As such, adding and
 * updating of tasks uses GroupTag#isSameGroupTag(GroupTag) for equality so as to ensure that the task being added or
 * updated is unique in terms of identity in the UniqueGroupTagList. However, the removal of a GroupTag uses
 * GroupTag#equals(Object) so as to ensure that the task with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see GroupTag#isSameGroupTag(GroupTag)
 */
public class UniqueGroupTagList implements Iterable<GroupTag> {

    private final ObservableList<GroupTag> internalList = FXCollections.observableArrayList();
    private final ObservableList<GroupTag> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent task as the given argument.
     */
    public boolean contains(GroupTag toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameGroupTag);
    }

    /**
     * Adds a GroupTag to the list.
     * The GroupTag must not already exist in the list.
     */
    public void add(GroupTag toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateGroupTagException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the GroupTag {@code target} in the list with {@code editedGroupTag}.
     * {@code target} must exist in the list.
     * The GroupTag identity of {@code editedTask} must not be the same as another existing task in the list.
     */
    public void setGroupTag(GroupTag target, GroupTag editedGroupTag) {
        requireAllNonNull(target, editedGroupTag);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new GroupTagNotFoundException();
        }

        if (!target.isSameGroupTag(editedGroupTag) && contains(editedGroupTag)) {
            throw new DuplicateGroupTagException();
        }

        internalList.set(index, editedGroupTag);
    }

    /**
     * Removes the equivalent GroupTag from the list.
     * The GroupTag must exist in the list.
     */
    public void remove(GroupTag toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new GroupTagNotFoundException();
        }
    }

    /**
     * Replaces the contents of this list with {@code tasks}.
     * {@code tasks} must not contain duplicate tasks.
     */
    public void setGroupTags(List<GroupTag> GroupTags) {
        requireAllNonNull(GroupTags);
        if (!GroupTagsAreUnique(GroupTags)) {
            throw new DuplicateGroupTagException();
        }

        internalList.setAll(GroupTags);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<GroupTag> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<GroupTag> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueGroupTagList // instanceof handles nulls
                && internalList.equals(((UniqueGroupTagList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    /**
     * Returns true if {@code tasks} contains only unique tasks.
     */
    private boolean GroupTagsAreUnique(List<GroupTag> GroupTags) {
        for (int i = 0; i < GroupTags.size() - 1; i++) {
            for (int j = i + 1; j < GroupTags.size(); j++) {
                if (GroupTags.get(i).isSameGroupTag(GroupTags.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
