package seedu.project.model;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code ProjectList} that keeps track of its own history.
 */
public class VersionedProjectList extends ProjectList {

    private final List<ReadOnlyProjectList> projectListStateList;
    private int currentStatePointer;

    public VersionedProjectList(ReadOnlyProjectList initialState) {
        super(initialState);

        projectListStateList = new ArrayList<>();
        projectListStateList.add(new ProjectList(initialState));
        currentStatePointer = 0;
    }

    /**
     * Saves a copy of the current {@code ProjectList} state at the end of the state
     * list. Undone states are removed from the state list.
     */
    public void commit() {
        removeStatesAfterCurrentPointer();
        projectListStateList.add(new ProjectList(this));
        currentStatePointer++;
        indicateModified();
    }

    private void removeStatesAfterCurrentPointer() {
        projectListStateList.subList(currentStatePointer + 1, projectListStateList.size()).clear();
    }

    /**
     * Restores the project list to its previous state.
     */
    public void undo() {
        if (!canUndo()) {
            throw new NoUndoableStateException();
        }
        currentStatePointer--;
        resetData(projectListStateList.get(currentStatePointer));
    }

    /**
     * Restores the project list to its previously undone state.
     */
    public void redo() {
        if (!canRedo()) {
            throw new NoRedoableStateException();
        }
        currentStatePointer++;
        resetData(projectListStateList.get(currentStatePointer));
    }

    /**
     * Returns true if {@code undo()} has project list states to undo.
     */
    public boolean canUndo() {
        return currentStatePointer > 0;
    }

    /**
     * Returns true if {@code redo()} has project list states to redo.
     */
    public boolean canRedo() {
        return currentStatePointer < projectListStateList.size() - 1;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof VersionedProjectList)) {
            return false;
        }

        VersionedProjectList otherVersionedProjectList = (VersionedProjectList) other;

        // state check
        return super.equals(otherVersionedProjectList)
                && projectListStateList.equals(otherVersionedProjectList.projectListStateList)
                && currentStatePointer == otherVersionedProjectList.currentStatePointer;
    }

    /**
     * Thrown when trying to {@code undo()} but can't.
     */
    public static class NoUndoableStateException extends RuntimeException {
        private NoUndoableStateException() {
            super("Current state pointer at start of projectListState list, unable to undo.");
        }
    }

    /**
     * Thrown when trying to {@code redo()} but can't.
     */
    public static class NoRedoableStateException extends RuntimeException {
        private NoRedoableStateException() {
            super("Current state pointer at end of projectListState list, unable to redo.");
        }
    }
}
