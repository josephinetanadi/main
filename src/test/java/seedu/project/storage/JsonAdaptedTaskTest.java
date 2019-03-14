package seedu.project.storage;

import static org.junit.Assert.assertEquals;
import static seedu.project.storage.JsonAdaptedTask.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.project.testutil.TypicalTasks.CS2101_MILESTONE;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import seedu.project.commons.exceptions.IllegalValueException;
import seedu.project.model.task.Deadline;
import seedu.project.model.task.Description;
import seedu.project.model.task.Name;
import seedu.project.testutil.Assert;

public class JsonAdaptedTaskTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_DESCRIPTION = "";
    private static final String INVALID_DEADLINE = "112011";
    private static final String INVALID_TAG = "#cs2101";

    private static final String VALID_NAME = CS2101_MILESTONE.getName().toString();
    private static final String VALID_DESCRIPTION = CS2101_MILESTONE.getDescription().toString();
    private static final String VALID_DEADLINE = CS2101_MILESTONE.getDeadline().toString();
    //private static final int VALID_TASK_ID = BENSON.getTaskId();
    private static final List<JsonAdaptedTag> VALID_TAGS = CS2101_MILESTONE.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());


    @Test
    public void toModelType_validTaskDetails_returnsTask() throws Exception {
        JsonAdaptedTask task = new JsonAdaptedTask(CS2101_MILESTONE);
        assertEquals(CS2101_MILESTONE, task.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedTask task =
                new JsonAdaptedTask(INVALID_NAME, VALID_DESCRIPTION, VALID_DEADLINE, VALID_TAGS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, task::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedTask task = new JsonAdaptedTask(null, VALID_DESCRIPTION, VALID_DEADLINE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, task::toModelType);
    }

    @Test
    public void toModelType_invalidDescription_throwsIllegalValueException() {
        JsonAdaptedTask task =
                new JsonAdaptedTask(VALID_NAME, INVALID_DESCRIPTION, VALID_DEADLINE,
                        VALID_TAGS);
        String expectedMessage = Description.MESSAGE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, task::toModelType);
    }

    @Test
    public void toModelType_nullDescription_throwsIllegalValueException() {
        JsonAdaptedTask task = new JsonAdaptedTask(VALID_NAME, null, VALID_DEADLINE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Description.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, task::toModelType);
    }

    @Test
    public void toModelType_invalidDeadline_throwsIllegalValueException() {
        JsonAdaptedTask task =
                new JsonAdaptedTask(VALID_NAME, VALID_DESCRIPTION, INVALID_DEADLINE, VALID_TAGS);
        String expectedMessage = Deadline.MESSAGE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, task::toModelType);
    }

    @Test
    public void toModelType_nullDeadline_throwsIllegalValueException() {
        JsonAdaptedTask task = new JsonAdaptedTask(VALID_NAME, VALID_DESCRIPTION, null, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Deadline.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, task::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedTask task =
                new JsonAdaptedTask(VALID_NAME, VALID_DESCRIPTION, VALID_DEADLINE, invalidTags);
        Assert.assertThrows(IllegalValueException.class, task::toModelType);
    }

}
