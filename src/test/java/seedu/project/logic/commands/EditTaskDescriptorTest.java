package seedu.project.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.project.logic.commands.CommandTestUtil.DESC_CP2106;
import static seedu.project.logic.commands.CommandTestUtil.DESC_CS2101;
import static seedu.project.logic.commands.CommandTestUtil.VALID_DEADLINE_CP2106;
import static seedu.project.logic.commands.CommandTestUtil.VALID_DESCRIPTION_CP2106;
import static seedu.project.logic.commands.CommandTestUtil.VALID_NAME_CP2106;
import static seedu.project.logic.commands.CommandTestUtil.VALID_TAG_CP2106;

import org.junit.Test;

import seedu.project.logic.commands.EditCommand.EditTaskDescriptor;
import seedu.project.testutil.EditTaskDescriptorBuilder;

public class EditTaskDescriptorTest {

    @Test
    public void equals() {
        // same values -> returns true
        EditTaskDescriptor descriptorWithSameValues = new EditTaskDescriptor(DESC_CS2101);
        assertTrue(DESC_CS2101.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_CS2101.equals(DESC_CS2101));

        // null -> returns false
        assertFalse(DESC_CS2101.equals(null));

        // different types -> returns false
        assertFalse(DESC_CS2101.equals(5));

        // different values -> returns false
        assertFalse(DESC_CS2101.equals(DESC_CP2106));

        // different name -> returns false
        EditTaskDescriptor editedCS2101 = new EditTaskDescriptorBuilder(DESC_CS2101).withName(VALID_NAME_CP2106)
                .build();
        assertFalse(DESC_CS2101.equals(editedCS2101));

        // different description -> returns false
        editedCS2101 = new EditTaskDescriptorBuilder(DESC_CS2101).withDescription(VALID_DESCRIPTION_CP2106).build();
        assertFalse(DESC_CS2101.equals(editedCS2101));

        // different deadline -> returns false
        editedCS2101 = new EditTaskDescriptorBuilder(DESC_CS2101).withDeadline(VALID_DEADLINE_CP2106).build();
        assertFalse(DESC_CS2101.equals(editedCS2101));

        // different tags -> returns false
        editedCS2101 = new EditTaskDescriptorBuilder(DESC_CS2101).withTags(VALID_TAG_CP2106).build();
        assertFalse(DESC_CS2101.equals(editedCS2101));
    }
}
