package systemtests;

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
import static seedu.project.logic.commands.CommandTestUtil.TAG_DESC_CP2106;
import static seedu.project.logic.commands.CommandTestUtil.TAG_DESC_CS2101;
import static seedu.project.logic.commands.CommandTestUtil.VALID_DEADLINE_CS2101;
import static seedu.project.logic.commands.CommandTestUtil.VALID_DESCRIPTION_CS2101;
import static seedu.project.logic.commands.CommandTestUtil.VALID_NAME_CP2106;
import static seedu.project.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.project.testutil.TypicalTasks.CP2106_MILESTONE;
import static seedu.project.testutil.TypicalTasks.CS2101_MILESTONE;
import static seedu.project.testutil.TypicalTasks.GROUP_MEETING;
import static seedu.project.testutil.TypicalTasks.KEYWORD_MATCHING_TEST;
import static seedu.project.testutil.TypicalTasks.LECTURE;
import static seedu.project.testutil.TypicalTasks.QUIZ;

import org.junit.Test;

import seedu.project.commons.core.Messages;
import seedu.project.commons.core.index.Index;
import seedu.project.logic.commands.AddCommand;
import seedu.project.logic.commands.UndoCommand;
import seedu.project.model.Model;
import seedu.project.model.Name;
import seedu.project.model.project.Project;
import seedu.project.model.tag.Tag;
import seedu.project.model.task.Deadline;
import seedu.project.model.task.Description;
import seedu.project.model.task.Task;
import seedu.project.testutil.TaskBuilder;
import seedu.project.testutil.TaskUtil;

public class AddCommandSystemTest extends ProjectSystemTest {
    /**
     * Just for the sake of it
     */
    @Test
    public void add() {
        Model model = getModel();

        /* ------------------------ Perform add operations on the shown unfiltered list ----------------------------- */

        /* Case: add a project to a non-empty project list, command with leading spaces and trailing spaces -> added */
        /*        Project projectToAdd = CS2101;
        String command = "   " + AddCommand.COMMAND_WORD + "  " + NAME_DESC_CS2101_PROJECT + "  ";
        assertCommandSuccess(command, projectToAdd);*/

        /* Case: select a project */
        selectProject(Index.fromOneBased(1));

        /* ------------------------ Perform add operations on the shown unfiltered list ----------------------------- */

        /* Case: add a task without tags to a non-empty project, command with leading spaces and trailing spaces
         * -> added
         */
        Task toAdd = CS2101_MILESTONE;
        String command = "   " + AddCommand.COMMAND_WORD + "  " + NAME_DESC_CS2101 + "  " + DESC_DESC_CS2101 + "   "
                + DEADLINE_DESC_CS2101 + "   " + TAG_DESC_CS2101 + " ";
        assertCommandSuccess(command, toAdd);

        /* Case: undo adding CS2101_MILESTONE to the list -> CS2101_MILESTONE deleted */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: redo adding CS2101_MILESTONE to the list -> CS2101_MILESTONE added again */
        /*        command = RedoCommand.COMMAND_WORD;
        model.addTask(toAdd);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);*/

        /* Case: add a task with all fields same as another task in the project except name -> added */
        toAdd = new TaskBuilder(CS2101_MILESTONE).withName(VALID_NAME_CP2106).build();
        command = AddCommand.COMMAND_WORD + NAME_DESC_CP2106 + DESC_DESC_CS2101 + DEADLINE_DESC_CS2101
                + TAG_DESC_CS2101;
        assertCommandSuccess(command, toAdd);

        /* Case: add to empty project -> added */
        deleteAllTasks();
        assertCommandSuccess(CS2101_MILESTONE);

        /* Case: add a task with tags, command with parameters in random order -> added */
        toAdd = CP2106_MILESTONE;
        command = AddCommand.COMMAND_WORD + TAG_DESC_CP2106 + DEADLINE_DESC_CP2106 + NAME_DESC_CP2106
                + DESC_DESC_CP2106;
        assertCommandSuccess(command, toAdd);

        /* Case: add a task, missing tags -> added */
        assertCommandSuccess(LECTURE);

        /* -------------------------- Perform add operation on the shown filtered list ------------------------------ */

        /* Case: filters the task list before adding -> added */
        showTasksWithName(KEYWORD_MATCHING_TEST);
        assertCommandSuccess(GROUP_MEETING);

        /* ------------------------ Perform add operation while a task card is selected --------------------------- */

        /* Case: selects first card in the task list, add a task -> added, card selection remains unchanged */
        selectTask(Index.fromOneBased(1));
        assertCommandSuccess(QUIZ);

        /* ----------------------------------- Perform invalid add operations --------------------------------------- */

        /* Case: add a duplicate task -> rejected */
        command = TaskUtil.getAddCommand(LECTURE);
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_TASK);

        /* Case: add a duplicate task except with different description -> rejected */
        toAdd = new TaskBuilder(LECTURE).withDescription(VALID_DESCRIPTION_CS2101).build();
        command = TaskUtil.getAddCommand(toAdd);
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_TASK);

        /* Case: add a duplicate task except with different deadline -> rejected */
        toAdd = new TaskBuilder(LECTURE).withDeadline(VALID_DEADLINE_CS2101).build();
        command = TaskUtil.getAddCommand(toAdd);
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_TASK);

        /* Case: add a duplicate task except with different tags -> rejected */
        command = TaskUtil.getAddCommand(LECTURE) + " " + PREFIX_TAG.getPrefix() + "CS2101";
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_TASK);

        /* Case: missing name -> rejected */
        command = AddCommand.COMMAND_WORD + DESC_DESC_CS2101 + DEADLINE_DESC_CS2101;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.TASK_MESSAGE_USAGE));

        /* Case: missing description -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_CS2101 + DEADLINE_DESC_CS2101;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.TASK_MESSAGE_USAGE));

        /* Case: missing deadline -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_CS2101 + DESC_DESC_CS2101;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.TASK_MESSAGE_USAGE));

        /* Case: invalid keyword -> rejected */
        command = "adds " + TaskUtil.getTaskDetails(toAdd);
        assertCommandFailure(command, Messages.MESSAGE_UNKNOWN_COMMAND);

        /* Case: invalid name -> rejected */
        command = AddCommand.COMMAND_WORD + INVALID_NAME_DESC + DESC_DESC_CS2101 + DEADLINE_DESC_CS2101;
        assertCommandFailure(command, Name.MESSAGE_CONSTRAINTS);

        /* Case: invalid description -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_CS2101 + INVALID_DESC_DESC + DEADLINE_DESC_CS2101;
        assertCommandFailure(command, Description.MESSAGE_CONSTRAINTS);

        /* Case: invalid deadline -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_CS2101 + DESC_DESC_CS2101 + INVALID_DEADLINE_DESC;
        assertCommandFailure(command, Deadline.MESSAGE_CONSTRAINTS);

        /* Case: invalid tag -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_CS2101 + DESC_DESC_CS2101 + DEADLINE_DESC_CS2101
                + INVALID_TAG_DESC;
        assertCommandFailure(command, Tag.MESSAGE_CONSTRAINTS);
    }

    /**
     * Executes the {@code AddCommand} that adds {@code toAdd} to the model and asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing {@code AddCommand} with the details of
     * {@code toAdd}.<br>
     * 4. {@code Storage} and {@code TaskListPanel} equal to the corresponding components in
     * the current model added with {@code toAdd}.<br>
     * 5. Browser url and selected card remain unchanged.<br>
     * 6. Status bar's sync status changes.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code ProjectSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see ProjectSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(Task toAdd) {
        assertCommandSuccess(TaskUtil.getAddCommand(toAdd), toAdd);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(Task)}. Executes {@code command}
     * instead.
     * @see AddCommandSystemTest#assertCommandSuccess(Task)
     */
    private void assertCommandSuccess(String command, Task toAdd) {
        Model expectedModel = getModel();
        expectedModel.addTask(toAdd);
        String expectedResultMessage = String.format(AddCommand.MESSAGE_SUCCESS_TASK, toAdd);
        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    /**
     * For the sake of it
     */
    private void assertCommandSuccess(String command, Project toAdd) {
        Model expectedModel = getModel();
        expectedModel.addProject(toAdd);
        String expectedResultMessage = String.format(AddCommand.MESSAGE_SUCCESS_PROJECT, toAdd);
        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Task)} except asserts that
     * the,<br>
     * 1. Result display box displays {@code expectedResultMessage}.<br>
     * 2. {@code Storage} and {@code TaskListPanel} equal to the corresponding components in
     * {@code expectedModel}.<br>
     * @see AddCommandSystemTest#assertCommandSuccess(String, Task)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        //assertSelectedCardUnchanged();
        assertCommandBoxShowsDefaultStyle();
        //assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays {@code command}.<br>
     * 2. Command box has the error style class.<br>
     * 3. Result display box displays {@code expectedResultMessage}.<br>
     * 4. {@code Storage} and {@code TaskListPanel} remain unchanged.<br>
     * 5. Browser url, selected card and status bar remain unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code ProjectSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see ProjectSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        //assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
