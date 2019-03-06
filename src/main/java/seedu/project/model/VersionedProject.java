package seedu.project.model;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code Project} that keeps track of its own history.
 */
public class VersionedProject extends Project {

    private final List<ReadOnlyProject> projectStateList;
    private int currentStatePointer;

    public VersionedProject(ReadOnlyProject initialState) {
        super(initialState);

        projectStateList = new ArrayList<>();
        projectStateList.add(new Project(initialState));
        currentStatePointer = 0;
    }

    /**
     * Saves a copy of the current {@code Project} state at the end of the state
     * list. Undone states are removed from the state list.
     */
    public void commit() {
        removeStatesAfterCurrentPointer();
        projectStateList.add(new Project(this));
        currentStatePointer++;
        indicateModified();
    }

    private void removeStatesAfterCurrentPointer() {
        projectStateList.subList(currentStatePointer + 1, projectStateList.size()).clear();
    }

    /**
     * Restores the address book to its previous state.
     */
    public void undo() {
        if (!canUndo()) {
            throw new NoUndoableStateException();
        }
        currentStatePointer--;
        resetData(projectStateList.get(currentStatePointer));
    }

    /**
     * Restores the address book to its previously undone state.
     */
    public void redo() {
        if (!canRedo()) {
            throw new NoRedoableStateException();
        }
        currentStatePointer++;
        resetData(projectStateList.get(currentStatePointer));
    }

    /**
     * Returns true if {@code undo()} has address book states to undo.
     */
    public boolean canUndo() {
        return currentStatePointer > 0;
    }

    /**
     * Returns true if {@code redo()} has address book states to redo.
     */
    public boolean canRedo() {
        return currentStatePointer < projectStateList.size() - 1;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof VersionedProject)) {
            return false;
        }

        VersionedProject otherVersionedProject = (VersionedProject) other;

        // state check
        return super.equals(otherVersionedProject) && projectStateList.equals(otherVersionedProject.projectStateList)
                && currentStatePointer == otherVersionedProject.currentStatePointer;
    }

    /**
     * Thrown when trying to {@code undo()} but can't.
     */
    public static class NoUndoableStateException extends RuntimeException {
        private NoUndoableStateException() {
            super("Current state pointer at start of projectState list, unable to undo.");
        }
    }

    /**
     * Thrown when trying to {@code redo()} but can't.
     */
    public static class NoRedoableStateException extends RuntimeException {
        private NoRedoableStateException() {
            super("Current state pointer at end of projectState list, unable to redo.");
        }
    }
}
