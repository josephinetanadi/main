package seedu.project.logic.parser;

import static seedu.project.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.project.logic.commands.CommandTestUtil.DEADLINE_DESC_CP2106;
import static seedu.project.logic.commands.CommandTestUtil.DEADLINE_DESC_CS2101;
import static seedu.project.logic.commands.CommandTestUtil.DESC_DESC_CP2106;
import static seedu.project.logic.commands.CommandTestUtil.DESC_DESC_CS2101;
import static seedu.project.logic.commands.CommandTestUtil.INVALID_DEADLINE_DESC;
import static seedu.project.logic.commands.CommandTestUtil.INVALID_DESC_DESC;
import static seedu.project.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.project.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.project.logic.commands.CommandTestUtil.NAME_DESC_CP2106;
import static seedu.project.logic.commands.CommandTestUtil.NAME_DESC_CS2101;
import static seedu.project.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.project.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.project.logic.commands.CommandTestUtil.TAG_DESC_CP2106;
import static seedu.project.logic.commands.CommandTestUtil.VALID_DEADLINE_CP2106;
import static seedu.project.logic.commands.CommandTestUtil.VALID_DESCRIPTION_CP2106;
import static seedu.project.logic.commands.CommandTestUtil.VALID_NAME_CP2106;
import static seedu.project.logic.commands.CommandTestUtil.VALID_TAG_CP2106;
import static seedu.project.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.project.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.project.testutil.TypicalTasks.CP2106_MILESTONE;
import static seedu.project.testutil.TypicalTasks.CS2101_MILESTONE;

import org.junit.Test;

import seedu.project.logic.LogicManager;
import seedu.project.logic.commands.AddCommand;
import seedu.project.model.Name;
import seedu.project.model.tag.Tag;
import seedu.project.model.task.Deadline;
import seedu.project.model.task.Description;
import seedu.project.model.task.Task;
import seedu.project.testutil.TaskBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Task expectedTask = new TaskBuilder(CP2106_MILESTONE).withTags(VALID_TAG_CP2106).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_CP2106 + DESC_DESC_CP2106
                + DEADLINE_DESC_CP2106 + TAG_DESC_CP2106, new AddCommand(expectedTask));

        // multiple names - last name accepted
        assertParseSuccess(parser, NAME_DESC_CS2101 + NAME_DESC_CP2106 + DESC_DESC_CP2106
                + DEADLINE_DESC_CP2106 + TAG_DESC_CP2106, new AddCommand(expectedTask));

        // multiple descriptions - last description accepted
        assertParseSuccess(parser, NAME_DESC_CP2106 + DESC_DESC_CS2101 + DESC_DESC_CP2106
                + DEADLINE_DESC_CP2106 + TAG_DESC_CP2106, new AddCommand(expectedTask));

        // multiple deadlines - last deadline accepted
        assertParseSuccess(parser, NAME_DESC_CP2106 + DESC_DESC_CP2106 + DEADLINE_DESC_CS2101
                + DEADLINE_DESC_CP2106 + TAG_DESC_CP2106, new AddCommand(expectedTask));

        // multiple tags - all accepted
        Task expectedTaskMultipleTags = new TaskBuilder(CP2106_MILESTONE).withTags(VALID_TAG_CP2106)
                .build();
        assertParseSuccess(parser, NAME_DESC_CP2106 + DESC_DESC_CP2106 + DEADLINE_DESC_CP2106
                + TAG_DESC_CP2106, new AddCommand(expectedTaskMultipleTags));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Task expectedTask = new TaskBuilder(CS2101_MILESTONE).withTags().build();

        LogicManager.setState(true);

        assertParseSuccess(parser, NAME_DESC_CS2101 + DESC_DESC_CS2101 + DEADLINE_DESC_CS2101,
                new AddCommand(expectedTask));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.TASK_MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_CP2106 + DESC_DESC_CP2106 + DEADLINE_DESC_CP2106,
                expectedMessage);

        // missing deadline prefix
        assertParseFailure(parser, NAME_DESC_CP2106 + VALID_DESCRIPTION_CP2106 + DEADLINE_DESC_CP2106,
                expectedMessage);

        // missing description prefix
        assertParseFailure(parser, NAME_DESC_CP2106 + DESC_DESC_CP2106 + VALID_DEADLINE_CP2106,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_CP2106 + VALID_DESCRIPTION_CP2106 + VALID_DEADLINE_CP2106,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + DESC_DESC_CP2106 + DEADLINE_DESC_CP2106
                + TAG_DESC_CP2106, Name.MESSAGE_CONSTRAINTS);

        // invalid description
        assertParseFailure(parser, NAME_DESC_CP2106 + INVALID_DESC_DESC + DEADLINE_DESC_CP2106
                + TAG_DESC_CP2106, Description.MESSAGE_CONSTRAINTS);

        // invalid deadline
        assertParseFailure(parser, NAME_DESC_CP2106 + DESC_DESC_CP2106 + INVALID_DEADLINE_DESC
                + TAG_DESC_CP2106, Deadline.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, NAME_DESC_CP2106 + DESC_DESC_CP2106 + DEADLINE_DESC_CP2106
                + INVALID_TAG_DESC, Tag.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + DESC_DESC_CP2106 + INVALID_DEADLINE_DESC,
                Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_CP2106 + DESC_DESC_CP2106 + DEADLINE_DESC_CP2106
                + TAG_DESC_CP2106, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.TASK_MESSAGE_USAGE));
    }
}
