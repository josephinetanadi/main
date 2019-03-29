package seedu.project.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.project.logic.commands.CommandTestUtil.VALID_DEADLINE_CP2106;
import static seedu.project.logic.commands.CommandTestUtil.VALID_TAG_CP2106;
import static seedu.project.testutil.TypicalTasks.CS2101_MILESTONE;
import static seedu.project.testutil.TypicalTasks.getTypicalProject;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.project.model.project.Project;
import seedu.project.model.project.ReadOnlyProject;
import seedu.project.model.task.Task;
import seedu.project.model.task.exceptions.DuplicateTaskException;
import seedu.project.testutil.TaskBuilder;

public class ProjectTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final Project project = new Project();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), project.getTaskList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        project.resetData(null);
    }

    @Test
    public void resetData_withValidReadOnlyProject_replacesData() {

        Project newData = getTypicalProject();
        project.resetData(newData);
        assertEquals(newData.getTaskList(), project.getTaskList());
    }

    @Test
    public void resetData_withDuplicateTasks_throwsDuplicateTaskException() {
        // Two tasks with the same identity fields
        Task editedAlice = new TaskBuilder(CS2101_MILESTONE).withDeadline(VALID_DEADLINE_CP2106)
                .withTags(VALID_TAG_CP2106).build();
        List<Task> newTasks = Arrays.asList(CS2101_MILESTONE, editedAlice);
        ProjectStub newData = new ProjectStub(newTasks);

        thrown.expect(DuplicateTaskException.class);
        project.resetData(newData);
    }

    @Test
    public void hasTask_nullTask_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        project.hasTask(null);
    }

    @Test
    public void hasTask_taskNotInProject_returnsFalse() {
        assertFalse(project.hasTask(CS2101_MILESTONE));
    }

    @Test
    public void hasTask_taskInProject_returnsTrue() {
        project.addTask(CS2101_MILESTONE);
        assertTrue(project.hasTask(CS2101_MILESTONE));
    }

    @Test
    public void hasTask_taskWithSameIdentityFieldsInProject_returnsTrue() {
        project.addTask(CS2101_MILESTONE);
        Task editedAlice = new TaskBuilder(CS2101_MILESTONE).withDeadline(VALID_DEADLINE_CP2106)
                .withTags(VALID_TAG_CP2106).build();
        assertTrue(project.hasTask(editedAlice));
    }

    @Test
    public void getTaskList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        project.getTaskList().remove(0);
    }

    @Test
    public void addListener_withInvalidationListener_listenerAdded() {
        SimpleIntegerProperty counter = new SimpleIntegerProperty();
        InvalidationListener listener = observable -> counter.set(counter.get() + 1);
        project.addListener(listener);
        project.addTask(CS2101_MILESTONE);
        assertEquals(1, counter.get());
    }

    @Test
    public void removeListener_withInvalidationListener_listenerRemoved() {
        SimpleIntegerProperty counter = new SimpleIntegerProperty();
        InvalidationListener listener = observable -> counter.set(counter.get() + 1);
        project.addListener(listener);
        project.removeListener(listener);
        project.addTask(CS2101_MILESTONE);
        assertEquals(0, counter.get());
    }

    /**
     * A stub ReadOnlyProject whose tasks list can violate interface constraints.
     */
    private static class ProjectStub implements ReadOnlyProject {
        private final ObservableList<Task> tasks = FXCollections.observableArrayList();
        private int index;
        private Name name;

        ProjectStub(Collection<Task> tasks) {
            this.tasks.setAll(tasks);
        }

        @Override
        public ObservableList<Task> getTaskList() {
            return tasks;
        }

        @Override
        public int getIndex(int taskId) {
            return index;
        }

        @Override
        public void addListener(InvalidationListener listener) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void removeListener(InvalidationListener listener) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Name getName() {
            return name;
        }

    }

}
