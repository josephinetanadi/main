package seedu.project.model.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.project.logic.commands.CommandTestUtil.VALID_DEADLINE_CP2106;
import static seedu.project.logic.commands.CommandTestUtil.VALID_DESCRIPTION_CP2106;
import static seedu.project.logic.commands.CommandTestUtil.VALID_NAME_CP2106;
import static seedu.project.logic.commands.CommandTestUtil.VALID_TAG_CP2106;
import static seedu.project.testutil.TypicalTasks.CP2106_MILESTONE;
import static seedu.project.testutil.TypicalTasks.CS2101_MILESTONE;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.project.logic.LogicManager;
import seedu.project.testutil.TaskBuilder;

public class TaskTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Task task = new TaskBuilder().build();
        thrown.expect(UnsupportedOperationException.class);
        task.getTags().remove(0);
    }

    @Test
    public void isSameTask() {
        // same object -> returns true
        assertTrue(CS2101_MILESTONE.isSameTask(CS2101_MILESTONE));

        // null -> returns false
        assertFalse(CS2101_MILESTONE.isSameTask(null));

        // different description and deadline -> returns false
        Task editedCS2101Milestone = new TaskBuilder(CS2101_MILESTONE).withDescription(VALID_DESCRIPTION_CP2106)
                .withDeadline(VALID_DEADLINE_CP2106).build();
        assertFalse(CS2101_MILESTONE.isSameTask(editedCS2101Milestone));

        // different name -> returns false
        editedCS2101Milestone = new TaskBuilder(CS2101_MILESTONE).withName(VALID_NAME_CP2106).build();
        assertFalse(CS2101_MILESTONE.isSameTask(editedCS2101Milestone));

        // same name, same description, different attributes -> returns true
        editedCS2101Milestone = new TaskBuilder(CS2101_MILESTONE).withDeadline(VALID_DEADLINE_CP2106)
                .withTags(VALID_TAG_CP2106).build();
        assertTrue(CS2101_MILESTONE.isSameTask(editedCS2101Milestone));

        // same name, same deadline, different attributes -> returns true
        editedCS2101Milestone = new TaskBuilder(CS2101_MILESTONE).withDescription(VALID_DESCRIPTION_CP2106)
                .withTags(VALID_TAG_CP2106).build();
        assertTrue(CS2101_MILESTONE.isSameTask(editedCS2101Milestone));

        // same name, same description, same deadline, different attributes -> returns true
        editedCS2101Milestone = new TaskBuilder(CS2101_MILESTONE).withTags(VALID_TAG_CP2106).build();
        assertTrue(CS2101_MILESTONE.isSameTask(editedCS2101Milestone));
    }

    @Test
    public void equals() {
        LogicManager.setState(true);
        System.out.println("hello");

        // same values -> returns true
        System.out.println(CS2101_MILESTONE.getName().toString());
        System.out.println(CS2101_MILESTONE.getDeadline().toString());
        System.out.println(CS2101_MILESTONE.getTags().toString());
        System.out.println(CS2101_MILESTONE.getDescription().toString());

        Task cs2101MilestoneCopy = new TaskBuilder(CS2101_MILESTONE).build();
        assertTrue(CS2101_MILESTONE.equals(cs2101MilestoneCopy));

        // same object -> returns true
        assertTrue(CS2101_MILESTONE.equals(CS2101_MILESTONE));

        // null -> returns false
        assertFalse(CS2101_MILESTONE.equals(null));

        // different type -> returns false
        assertFalse(CS2101_MILESTONE.equals(5));

        // different task -> returns false
        assertFalse(CS2101_MILESTONE.equals(CP2106_MILESTONE));

        // different name -> returns false
        Task editedCS2101Milestone = new TaskBuilder(CS2101_MILESTONE).withName(VALID_NAME_CP2106).build();
        assertFalse(CS2101_MILESTONE.equals(editedCS2101Milestone));

        // different description -> returns false
        editedCS2101Milestone = new TaskBuilder(CS2101_MILESTONE).withDescription(VALID_DESCRIPTION_CP2106).build();
        assertFalse(CS2101_MILESTONE.equals(editedCS2101Milestone));

        // different deadline -> returns false
        editedCS2101Milestone = new TaskBuilder(CS2101_MILESTONE).withDeadline(VALID_DEADLINE_CP2106).build();
        assertFalse(CS2101_MILESTONE.equals(editedCS2101Milestone));

        // different tags -> returns false
        editedCS2101Milestone = new TaskBuilder(CS2101_MILESTONE).withTags(VALID_TAG_CP2106).build();
        assertFalse(CS2101_MILESTONE.equals(editedCS2101Milestone));
    }
}
