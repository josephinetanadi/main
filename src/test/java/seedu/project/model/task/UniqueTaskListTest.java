package seedu.project.model.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.project.logic.commands.CommandTestUtil.VALID_DESCRIPTION_CP2106;
import static seedu.project.logic.commands.CommandTestUtil.VALID_TAG_CS2101;
import static seedu.project.testutil.TypicalTasks.CP2106_MILESTONE;
import static seedu.project.testutil.TypicalTasks.CS2101_MILESTONE;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.project.model.task.exceptions.DuplicateTaskException;
import seedu.project.model.task.exceptions.TaskNotFoundException;
import seedu.project.testutil.TaskBuilder;

public class UniqueTaskListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final UniqueTaskList uniqueTaskList = new UniqueTaskList();

    @Test
    public void contains_nullTask_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueTaskList.contains(null);
    }

    @Test
    public void contains_taskNotInList_returnsFalse() {
        assertFalse(uniqueTaskList.contains(CS2101_MILESTONE));
    }

    @Test
    public void contains_taskInList_returnsTrue() {
        uniqueTaskList.add(CS2101_MILESTONE);
        assertTrue(uniqueTaskList.contains(CS2101_MILESTONE));
    }

    @Test
    public void contains_taskWithSameIdentityFieldsInList_returnsTrue() {
        uniqueTaskList.add(CS2101_MILESTONE);
        Task editedAlice = new TaskBuilder(CS2101_MILESTONE).withDescription(VALID_DESCRIPTION_CP2106)
                .withTags(VALID_TAG_CS2101).build();
        assertTrue(uniqueTaskList.contains(editedAlice));
    }

    @Test
    public void add_nullTask_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueTaskList.add(null);
    }

    @Test
    public void add_duplicateTask_throwsDuplicateTaskException() {
        uniqueTaskList.add(CS2101_MILESTONE);
        thrown.expect(DuplicateTaskException.class);
        uniqueTaskList.add(CS2101_MILESTONE);
    }

    @Test
    public void setTask_nullTargetTask_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueTaskList.setTask(null, CS2101_MILESTONE);
    }

    @Test
    public void setTask_nullEditedTask_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueTaskList.setTask(CS2101_MILESTONE, null);
    }

    @Test
    public void setTask_targetTaskNotInList_throwsTaskNotFoundException() {
        thrown.expect(TaskNotFoundException.class);
        uniqueTaskList.setTask(CS2101_MILESTONE, CS2101_MILESTONE);
    }

    @Test
    public void setTask_editedTaskIsSameTask_success() {
        uniqueTaskList.add(CS2101_MILESTONE);
        uniqueTaskList.setTask(CS2101_MILESTONE, CS2101_MILESTONE);
        UniqueTaskList expectedUniqueTaskList = new UniqueTaskList();
        expectedUniqueTaskList.add(CS2101_MILESTONE);
        assertEquals(expectedUniqueTaskList, uniqueTaskList);
    }

    @Test
    public void setTask_editedTaskHasSameIdentity_success() {
        uniqueTaskList.add(CS2101_MILESTONE);
        Task editedAlice = new TaskBuilder(CS2101_MILESTONE).withDescription(VALID_DESCRIPTION_CP2106)
                .withTags(VALID_TAG_CS2101).build();
        uniqueTaskList.setTask(CS2101_MILESTONE, editedAlice);
        UniqueTaskList expectedUniqueTaskList = new UniqueTaskList();
        expectedUniqueTaskList.add(editedAlice);
        assertEquals(expectedUniqueTaskList, uniqueTaskList);
    }

    @Test
    public void setTask_editedTaskHasDifferentIdentity_success() {
        uniqueTaskList.add(CS2101_MILESTONE);
        uniqueTaskList.setTask(CS2101_MILESTONE, CP2106_MILESTONE);
        UniqueTaskList expectedUniqueTaskList = new UniqueTaskList();
        expectedUniqueTaskList.add(CP2106_MILESTONE);
        assertEquals(expectedUniqueTaskList, uniqueTaskList);
    }

    @Test
    public void setTask_editedTaskHasNonUniqueIdentity_throwsDuplicateTaskException() {
        uniqueTaskList.add(CS2101_MILESTONE);
        uniqueTaskList.add(CP2106_MILESTONE);
        thrown.expect(DuplicateTaskException.class);
        uniqueTaskList.setTask(CS2101_MILESTONE, CP2106_MILESTONE);
    }

    @Test
    public void remove_nullTask_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueTaskList.remove(null);
    }

    @Test
    public void remove_taskDoesNotExist_throwsTaskNotFoundException() {
        thrown.expect(TaskNotFoundException.class);
        uniqueTaskList.remove(CS2101_MILESTONE);
    }

    @Test
    public void remove_existingTask_removesTask() {
        uniqueTaskList.add(CS2101_MILESTONE);
        uniqueTaskList.remove(CS2101_MILESTONE);
        UniqueTaskList expectedUniqueTaskList = new UniqueTaskList();
        assertEquals(expectedUniqueTaskList, uniqueTaskList);
    }

    @Test
    public void setTasks_nullUniqueTaskList_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueTaskList.setTasks((UniqueTaskList) null);
    }

    @Test
    public void setTasks_uniqueTaskList_replacesOwnListWithProvidedUniqueTaskList() {
        uniqueTaskList.add(CS2101_MILESTONE);
        UniqueTaskList expectedUniqueTaskList = new UniqueTaskList();
        expectedUniqueTaskList.add(CP2106_MILESTONE);
        uniqueTaskList.setTasks(expectedUniqueTaskList);
        assertEquals(expectedUniqueTaskList, uniqueTaskList);
    }

    @Test
    public void setTasks_nullList_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueTaskList.setTasks((List<Task>) null);
    }

    @Test
    public void setTasks_list_replacesOwnListWithProvidedList() {
        uniqueTaskList.add(CS2101_MILESTONE);
        List<Task> taskList = Collections.singletonList(CP2106_MILESTONE);
        uniqueTaskList.setTasks(taskList);
        UniqueTaskList expectedUniqueTaskList = new UniqueTaskList();
        expectedUniqueTaskList.add(CP2106_MILESTONE);
        assertEquals(expectedUniqueTaskList, uniqueTaskList);
    }

    @Test
    public void setTasks_listWithDuplicateTasks_throwsDuplicateTaskException() {
        List<Task> listWithDuplicateTasks = Arrays.asList(CS2101_MILESTONE, CS2101_MILESTONE);
        thrown.expect(DuplicateTaskException.class);
        uniqueTaskList.setTasks(listWithDuplicateTasks);
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        uniqueTaskList.asUnmodifiableObservableList().remove(0);
    }
}
