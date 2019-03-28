package systemtests;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import seedu.project.model.Model;
import seedu.project.model.project.Project;
import seedu.project.model.task.Task;

/**
 * Contains helper methods to set up {@code Model} for testing.
 */
public class ModelHelper {
    private static final Predicate<Task> PREDICATE_MATCHING_NO_TASKS = unused -> false;
    private static final Predicate<Project> PREDICATE_MATCHING_NO_PROJECTS = unused -> false;
    /**
     * Updates {@code model}'s filtered list to display only {@code toDisplay}.
     */
    public static void setFilteredProjectList(Model model, List<Project> toDisplay) {
        Optional<Predicate<Project>> predicate = toDisplay.stream().map(ModelHelper::getPredicateMatching)
                .reduce(Predicate::or);
        model.updateFilteredProjectList(predicate.orElse(PREDICATE_MATCHING_NO_PROJECTS));
    }

    /**
     * @see ModelHelper#setFilteredProjectList(Model, List)
     */
    public static void setFilteredProjectList(Model model, Project... toDisplay) {
        setFilteredProjectList(model, Arrays.asList(toDisplay));
    }
    /**
     * Updates {@code model}'s filtered list to display only {@code toDisplay}.
     */
    public static void setFilteredTaskList(Model model, List<Task> toDisplay) {
        Optional<Predicate<Task>> predicate = toDisplay.stream().map(ModelHelper::getPredicateMatching)
                .reduce(Predicate::or);
        model.updateFilteredTaskList(predicate.orElse(PREDICATE_MATCHING_NO_TASKS));
    }

    /**
     * @see ModelHelper#setFilteredTaskList(Model, List)
     */
    public static void setFilteredTaskList(Model model, Task... toDisplay) {
        setFilteredTaskList(model, Arrays.asList(toDisplay));
    }

    /**
     * Returns a predicate that evaluates to true if this {@code Task} equals to
     * {@code other}.
     */
    private static Predicate<Project> getPredicateMatching(Project other) {
        return project -> project.equals(other);
    }
    private static Predicate<Task> getPredicateMatching(Task other) {
        return task -> task.equals(other);
    }
}
