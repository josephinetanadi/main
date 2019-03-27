package seedu.project.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.project.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.project.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.project.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.project.logic.parser.CliSyntax.PREFIX_TAG;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.project.commons.core.index.Index;
import seedu.project.commons.exceptions.DataConversionException;
import seedu.project.logic.CommandHistory;
import seedu.project.logic.commands.exceptions.CommandException;
import seedu.project.model.Model;
import seedu.project.model.project.Project;
import seedu.project.model.task.NameContainsKeywordsPredicate;
import seedu.project.model.task.Task;
import seedu.project.testutil.EditTaskDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_NAME_CS2101 = "Milestone";
    public static final String VALID_NAME_CP2106 = "Orbital Project";
    public static final String VALID_DESCRIPTION_CS2101 = "config labels, milestone, project board";
    public static final String VALID_DESCRIPTION_CP2106 = "listings, notifications";
    public static final String VALID_DEADLINE_CS2101 = "1-1-2011";
    public static final String VALID_DEADLINE_CP2106 = "20-6-2019";
    public static final String VALID_TAG_CS2101 = "CS2101";
    public static final String VALID_TAG_CP2106 = "CP2106";

    public static final String NAME_DESC_CS2101 = " " + PREFIX_NAME + VALID_NAME_CS2101;
    public static final String NAME_DESC_CP2106 = " " + PREFIX_NAME + VALID_NAME_CP2106;
    public static final String DESC_DESC_CS2101 = " " + PREFIX_DESCRIPTION + VALID_DESCRIPTION_CS2101;
    public static final String DESC_DESC_CP2106 = " " + PREFIX_DESCRIPTION + VALID_DESCRIPTION_CP2106;
    public static final String DEADLINE_DESC_CS2101 = " " + PREFIX_DEADLINE + VALID_DEADLINE_CS2101;
    public static final String DEADLINE_DESC_CP2106 = " " + PREFIX_DEADLINE + VALID_DEADLINE_CP2106;
    public static final String TAG_DESC_CS2101 = " " + PREFIX_TAG + VALID_TAG_CS2101;
    public static final String TAG_DESC_CP2106 = " " + PREFIX_TAG + VALID_TAG_CP2106;

    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "Milestone&"; // '&' not allowed in names
    public static final String INVALID_DESC_DESC = " " + PREFIX_DESCRIPTION + ""; // '(blank)' not allowed in desc
    public static final String INVALID_DEADLINE_DESC = " " + PREFIX_DEADLINE + "112011"; // does not follow date format
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "mod*"; // '*' not allowed in tags

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditCommand.EditTaskDescriptor DESC_CS2101;
    public static final EditCommand.EditTaskDescriptor DESC_CP2106;

    static {
        DESC_CS2101 = new EditTaskDescriptorBuilder().withName(VALID_NAME_CS2101)
                .withDescription(VALID_DESCRIPTION_CS2101).withDeadline(VALID_DEADLINE_CS2101)
                .withTags(VALID_TAG_CS2101).build();
        DESC_CP2106 = new EditTaskDescriptorBuilder().withName(VALID_NAME_CP2106)
                .withDescription(VALID_DESCRIPTION_CP2106).withDeadline(VALID_DEADLINE_CP2106)
                .withTags(VALID_TAG_CP2106).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel} <br>
     * - the {@code actualCommandHistory} remains unchanged.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandHistory actualCommandHistory,
            CommandResult expectedCommandResult, Model expectedModel) {
        CommandHistory expectedCommandHistory = new CommandHistory(actualCommandHistory);
        try {
            CommandResult result = command.execute(actualModel, actualCommandHistory);
            assertEquals(expectedCommandResult, result);
                assertEquals(expectedModel, actualModel);
            assertEquals(expectedCommandHistory, actualCommandHistory);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        } catch (DataConversionException e) {
            throw new AssertionError("Data Conversation Exception.", e);
        } catch (IOException e) {
            throw new AssertionError("Input / Output Exception.", e);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandHistory, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandHistory actualCommandHistory,
            String expectedMessage, Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, actualCommandHistory, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the project, filtered task list and selected task in {@code actualModel} remain unchanged <br>
     * - {@code actualCommandHistory} remains unchanged.
     */
    public static void assertCommandFailure(Command command, Model actualModel, CommandHistory actualCommandHistory,
            String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        Project expectedProject = new Project(actualModel.getProject());
        List<Task> expectedFilteredList = new ArrayList<>(actualModel.getFilteredTaskList());
        Task expectedSelectedTask = actualModel.getSelectedTask();

        CommandHistory expectedCommandHistory = new CommandHistory(actualCommandHistory);

        try {
            command.execute(actualModel, actualCommandHistory);
            throw new AssertionError("The expected CommandException was not thrown.");
        } catch (CommandException | DataConversionException | IOException e) {
            assertEquals(expectedMessage, e.getMessage());
            assertEquals(expectedProject, actualModel.getProject());
            assertEquals(expectedFilteredList, actualModel.getFilteredTaskList());
            assertEquals(expectedSelectedTask, actualModel.getSelectedTask());
            assertEquals(expectedCommandHistory, actualCommandHistory);
        }
    }

    /**
     * Updates {@code model}'s filtered list to show only the task at the given {@code targetIndex} in the
     * {@code model}'s project.
     */
    public static void showTaskAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredTaskList().size());

        Task task = model.getFilteredTaskList().get(targetIndex.getZeroBased());
        final String[] splitName = task.getName().fullName.split("\\s+");
        model.updateFilteredTaskList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredTaskList().size());
    }

    /**
     * Deletes the first task in {@code model}'s filtered list from {@code model}'s project.
     */
    public static void deleteFirstTask(Model model) {
        Task firstTask = model.getFilteredTaskList().get(0);
        model.deleteTask(firstTask);
        model.commitProject();
    }

}
