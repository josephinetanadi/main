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
import static seedu.project.logic.commands.CommandTestUtil.NAME_DESC_CS2101;
import static seedu.project.logic.commands.CommandTestUtil.TAG_DESC_CP2106;
import static seedu.project.logic.commands.CommandTestUtil.TAG_DESC_CS2101;
import static seedu.project.logic.commands.CommandTestUtil.VALID_DEADLINE_CP2106;
import static seedu.project.logic.commands.CommandTestUtil.VALID_DEADLINE_CS2101;
import static seedu.project.logic.commands.CommandTestUtil.VALID_DESCRIPTION_CP2106;
import static seedu.project.logic.commands.CommandTestUtil.VALID_DESCRIPTION_CS2101;
import static seedu.project.logic.commands.CommandTestUtil.VALID_NAME_CS2101;
import static seedu.project.logic.commands.CommandTestUtil.VALID_TAG_CP2106;
import static seedu.project.logic.commands.CommandTestUtil.VALID_TAG_CS2101;
import static seedu.project.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.project.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.project.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.project.testutil.TypicalIndexes.INDEX_FIRST_TASK;
import static seedu.project.testutil.TypicalIndexes.INDEX_SECOND_TASK;
import static seedu.project.testutil.TypicalIndexes.INDEX_THIRD_TASK;

import org.junit.Test;

import seedu.project.commons.core.index.Index;
import seedu.project.logic.commands.EditCommand.EditTaskDescriptor;
import seedu.project.logic.commands.EditCommand;
import seedu.project.model.tag.Tag;
import seedu.project.model.task.Deadline;
import seedu.project.model.task.Description;
import seedu.project.model.Name;
import seedu.project.testutil.EditTaskDescriptorBuilder;

public class EditCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_TAG;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);

    private EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_NAME_CS2101, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + NAME_DESC_CS2101, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + NAME_DESC_CS2101, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS); // invalid name
        assertParseFailure(parser, "1" + INVALID_DESC_DESC, Description.MESSAGE_CONSTRAINTS); // invalid description
        assertParseFailure(parser, "1" + INVALID_DEADLINE_DESC, Deadline.MESSAGE_CONSTRAINTS); // invalid deadline
        assertParseFailure(parser, "1" + INVALID_TAG_DESC, Tag.MESSAGE_CONSTRAINTS); // invalid tag

        // invalid description followed by valid deadline
        assertParseFailure(parser, "1" + INVALID_DESC_DESC + DEADLINE_DESC_CS2101, Description.MESSAGE_CONSTRAINTS);

        // valid description followed by invalid description.
        // The test case for invalid description followed by valid description
        // is tested at {@code parse_invalidValueFollowedByValidValue_success()}
        assertParseFailure(parser, "1" + DESC_DESC_CP2106 + INVALID_DESC_DESC, Description.MESSAGE_CONSTRAINTS);

        // while parsing {@code PREFIX_TAG} alone will reset the tags of the {@code Task} being edited,
        // parsing it together with a valid tag results in error
        assertParseFailure(parser, "1" + TAG_DESC_CS2101 + TAG_EMPTY, Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_DESC_CS2101 + TAG_EMPTY + TAG_DESC_CP2106, Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_EMPTY + TAG_DESC_CS2101 + TAG_DESC_CP2106, Tag.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_NAME_DESC + INVALID_DESC_DESC + VALID_DEADLINE_CS2101,
                Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_TASK;
        String userInput = targetIndex.getOneBased() + DESC_DESC_CS2101 + DEADLINE_DESC_CS2101 + NAME_DESC_CS2101
                + TAG_DESC_CS2101;

        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withName(VALID_NAME_CS2101)
                .withDescription(VALID_DESCRIPTION_CS2101).withDeadline(VALID_DEADLINE_CS2101)
                .withTags(VALID_TAG_CS2101).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_TASK;
        String userInput = targetIndex.getOneBased() + DESC_DESC_CP2106 + DEADLINE_DESC_CS2101;

        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withDescription(VALID_DESCRIPTION_CP2106)
                .withDeadline(VALID_DEADLINE_CS2101).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        Index targetIndex = INDEX_THIRD_TASK;
        String userInput = targetIndex.getOneBased() + NAME_DESC_CS2101;
        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withName(VALID_NAME_CS2101).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // description
        userInput = targetIndex.getOneBased() + DESC_DESC_CS2101;
        descriptor = new EditTaskDescriptorBuilder().withDescription(VALID_DESCRIPTION_CS2101).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // deadline
        userInput = targetIndex.getOneBased() + DEADLINE_DESC_CS2101;
        descriptor = new EditTaskDescriptorBuilder().withDeadline(VALID_DEADLINE_CS2101).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // tags
        userInput = targetIndex.getOneBased() + TAG_DESC_CS2101;
        descriptor = new EditTaskDescriptorBuilder().withTags(VALID_TAG_CS2101).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() {
        Index targetIndex = INDEX_FIRST_TASK;
        String userInput = targetIndex.getOneBased() + DESC_DESC_CS2101 + DEADLINE_DESC_CS2101 + TAG_DESC_CS2101
                + DESC_DESC_CS2101 + DEADLINE_DESC_CS2101 + TAG_DESC_CS2101
                + DESC_DESC_CP2106 + DEADLINE_DESC_CP2106 + TAG_DESC_CP2106;

        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withDescription(VALID_DESCRIPTION_CP2106)
                .withDeadline(VALID_DEADLINE_CP2106).withTags(VALID_TAG_CS2101, VALID_TAG_CP2106).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidValueFollowedByValidValue_success() {
        // no other valid values specified
        Index targetIndex = INDEX_FIRST_TASK;
        String userInput = targetIndex.getOneBased() + INVALID_DESC_DESC + DESC_DESC_CP2106;
        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withDescription(VALID_DESCRIPTION_CP2106)
                .build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // other valid values specified
        userInput = targetIndex.getOneBased() + INVALID_DESC_DESC + DEADLINE_DESC_CP2106 + DESC_DESC_CP2106;
        descriptor = new EditTaskDescriptorBuilder().withDescription(VALID_DESCRIPTION_CP2106)
                .withDeadline(VALID_DEADLINE_CP2106).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_resetTags_success() {
        Index targetIndex = INDEX_THIRD_TASK;
        String userInput = targetIndex.getOneBased() + TAG_EMPTY;

        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withTags().build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
