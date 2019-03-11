package seedu.project.model;

import seedu.project.model.task.Task;

/**
 * Compares task
 */
public class CompareTask {

    /**
     * Returns index of a {@code taskID} in the list.
     * {@code taskID} must exist in the project
     */
    public boolean compareTask(Task task1, Task task2) {
        System.out.print("XXXXXX\n");
        System.out.print(task1 + "\n");
        System.out.print(task2 + "\n\n");
        if (task1.getName() != task2.getName()) {
            return true;
        } else if (task1.getAddress() != task2.getAddress()) {
            return true;
        } else if (task1.getEmail() != task2.getEmail()) {
            return true;
        } else {
            return false;
        }
    }
}
