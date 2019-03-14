package seedu.project.model.project;

import java.util.ArrayList;
import java.util.List;

import seedu.project.model.task.Task;

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
     * Restores the project to its previous state.
     */
    public void undo() {
        if (!canUndo()) {
            throw new NoUndoableStateException();
        }
        currentStatePointer--;
        resetData(projectStateList.get(currentStatePointer));
    }

    /**
     * Restores the project to its previously undone state.
     */
    public void redo() {
        if (!canRedo()) {
            throw new NoRedoableStateException();
        }
        currentStatePointer++;
        resetData(projectStateList.get(currentStatePointer));
    }

    /**
     * Returns true if {@code undo()} has project states to undo.
     */
    public boolean canUndo() {
        return currentStatePointer > 0;
    }

    /**
     * Returns true if {@code redo()} has project states to redo.
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
     * Compares the target task with the previous edited version
     */
    public Task compareTask(Task target1) {
        int targetTaskId = target1.getTaskId();
        int movingStatePointer = currentStatePointer;
        System.out.print("CSP IS : " + currentStatePointer + "\n");
        movingStatePointer--;
        while (movingStatePointer >= 0) {
            System.out.print("MSP IS : " + movingStatePointer + "\n");
            System.out.print("Tasklist is: " + projectStateList.get(movingStatePointer).getTaskList() + "\n");
            int taskIndex = projectStateList.get(movingStatePointer).getIndex(targetTaskId);
            if (getDiff(target1, projectStateList.get(movingStatePointer).getTaskList()
                    .get(taskIndex))) {
                System.out.println("got diff");
                return projectStateList.get(movingStatePointer).getTaskList().get(taskIndex);
            } else {
                movingStatePointer--;
            }
        }

        System.out.print("nothing to compare");
        return null;
    }

    /**
     * Returns true if there is a difference between {@code task1, task}.
     * Else returns false
     * {@code task1, task2} must exist in the project
     */
    public boolean getDiff(Task task1, Task task2) {
        System.out.print("XXXXXX\n");
        System.out.print(task1 + "\n");
        System.out.print(task2 + "\n\n");
        if (task1.getName() != task2.getName()) {
            return true;
        } else if (task1.getDescription() != task2.getDescription()) {
            return true;
        } else if (task1.getDeadline() != task2.getDeadline()) {
            return true;
        } else {
            return false;
        }
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
