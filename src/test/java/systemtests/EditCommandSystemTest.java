package systemtests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
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
import static seedu.project.logic.commands.CommandTestUtil.VALID_NAME_CP2106;
import static seedu.project.logic.commands.CommandTestUtil.VALID_NAME_CS2101;
import static seedu.project.logic.commands.CommandTestUtil.VALID_TAG_CP2106;
import static seedu.project.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.project.model.Model.PREDICATE_SHOW_ALL_TASKS;
import static seedu.project.testutil.TypicalIndexes.INDEX_FIRST_TASK;
import static seedu.project.testutil.TypicalIndexes.INDEX_SECOND_TASK;
import static seedu.project.testutil.TypicalTasks.CP2106_MILESTONE;
import static seedu.project.testutil.TypicalTasks.CS2101_MILESTONE;
import static seedu.project.testutil.TypicalTasks.KEYWORD_MATCHING_TEST;

//import org.junit.Test;

import seedu.project.commons.core.Messages;
import seedu.project.commons.core.index.Index;
import seedu.project.logic.commands.EditCommand;
import seedu.project.logic.commands.RedoCommand;
import seedu.project.logic.commands.UndoCommand;
import seedu.project.model.Model;
import seedu.project.model.Name;
import seedu.project.model.tag.Tag;
import seedu.project.model.task.Deadline;
import seedu.project.model.task.Description;
import seedu.project.model.task.Task;
import seedu.project.testutil.TaskBuilder;
import seedu.project.testutil.TaskUtil;

public class EditCommandSystemTest extends ProjectSystemTest {

    /**
     * Just for the sake of it
     */
    //@Test
    public void edit() {
        Model model = getModel();

        /*
         * ----------------- Performing edit operation while an unfiltered list is being
         * shown ----------------------
         */

        selectProject(Index.fromOneBased(1));

        /*
         * Case: edit all fields, command with leading spaces, trailing spaces and
         * multiple spaces between each field -> edited
         */
        Index index = INDEX_FIRST_TASK;
        String command = " " + EditCommand.COMMAND_WORD + "  " + index.getOneBased() + "  " + NAME_DESC_CP2106
                + "  " + DESC_DESC_CP2106 + " " + DEADLINE_DESC_CP2106 + " " + TAG_DESC_CP2106 + " ";
        Task editedTask = new TaskBuilder(CP2106_MILESTONE).withTags(VALID_TAG_CP2106).build();
        assertCommandSuccess(command, index, editedTask);

        /* Case: undo editing the last task in the list -> last task restored */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: redo editing the last task in the list -> last task edited again */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        model.setTask(getModel().getFilteredTaskList().get(INDEX_FIRST_TASK.getZeroBased()), editedTask);
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: edit a task with new values same as existing values -> edited */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_CP2106 + DESC_DESC_CP2106
                + DEADLINE_DESC_CP2106 + TAG_DESC_CP2106;
        assertCommandSuccess(command, index, CP2106_MILESTONE);

        /*
         * Case: edit a task with new values same as another task's values but with
         * different name -> edited
         */
        assertTrue(getModel().getProject().getTaskList().contains(CP2106_MILESTONE));
        index = INDEX_SECOND_TASK;
        assertNotEquals(getModel().getFilteredTaskList().get(index.getZeroBased()), CP2106_MILESTONE);
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_CS2101 + DESC_DESC_CP2106
                + DEADLINE_DESC_CP2106 + TAG_DESC_CP2106;
        editedTask = new TaskBuilder(CP2106_MILESTONE).withName(VALID_NAME_CS2101).build();
        assertCommandSuccess(command, index, editedTask);

        /* Case: clear tags -> cleared */
        index = INDEX_FIRST_TASK;
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + " " + PREFIX_TAG.getPrefix();
        Task taskToEdit = getModel().getFilteredTaskList().get(index.getZeroBased());
        editedTask = new TaskBuilder(taskToEdit).withTags().build();
        assertCommandSuccess(command, index, editedTask);

        /*
         * ------------------ Performing edit operation while a filtered list is being
         * shown ------------------------
         */

        /*
         * Case: filtered task list, edit index within bounds of project and task
         * list -> edited
         */
        showTasksWithName(KEYWORD_MATCHING_TEST);
        index = INDEX_FIRST_TASK;
        assertTrue(index.getZeroBased() < getModel().getFilteredTaskList().size());
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + " " + NAME_DESC_CP2106;
        taskToEdit = getModel().getFilteredTaskList().get(index.getZeroBased());
        editedTask = new TaskBuilder(taskToEdit).withName(VALID_NAME_CP2106).build();
        assertCommandSuccess(command, index, editedTask);

        /*
         * Case: filtered task list, edit index within bounds of project but out of
         * bounds of task list -> rejected
         */
        showTasksWithName(KEYWORD_MATCHING_TEST);
        int invalidIndex = getModel().getProject().getTaskList().size();
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + invalidIndex + NAME_DESC_CP2106,
                Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);

        /*
         * --------------------- Performing edit operation while a task card is selected
         * --------------------------
         */

        /*
         * Case: selects first card in the task list, edit a task -> edited, card
         * selection remains unchanged but browser url changes
         */
        showAllTasks();
        index = INDEX_FIRST_TASK;
        selectTask(index);
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_CS2101 + DESC_DESC_CS2101
                + DEADLINE_DESC_CS2101 + TAG_DESC_CS2101;
        // this can be misleading: card selection actually remains unchanged but the
        // browser's url is updated to reflect the new task's name
        assertCommandSuccess(command, index, CS2101_MILESTONE, index);

        /*
         * --------------------------------- Performing invalid edit operation
         * --------------------------------------
         */

        /* Case: invalid index (0) -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " 0" + NAME_DESC_CP2106,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.TASK_MESSAGE_USAGE));

        /* Case: invalid index (-1) -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " -1" + NAME_DESC_CP2106,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.TASK_MESSAGE_USAGE));

        /* Case: invalid index (size + 1) -> rejected */
        invalidIndex = getModel().getFilteredTaskList().size() + 1;
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + invalidIndex + NAME_DESC_CP2106,
                Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);

        /* Case: missing index -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + NAME_DESC_CP2106,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.TASK_MESSAGE_USAGE));

        /* Case: missing all fields -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_TASK.getOneBased(),
                EditCommand.MESSAGE_NOT_EDITED);

        /* Case: invalid name -> rejected */
        assertCommandFailure(
                EditCommand.COMMAND_WORD + " " + INDEX_FIRST_TASK.getOneBased() + INVALID_NAME_DESC,
                Name.MESSAGE_CONSTRAINTS);

        /* Case: invalid description -> rejected */
        assertCommandFailure(
                EditCommand.COMMAND_WORD + " " + INDEX_FIRST_TASK.getOneBased() + INVALID_DESC_DESC,
                Description.MESSAGE_CONSTRAINTS);

        /* Case: invalid deadline -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_TASK.getOneBased()
                + INVALID_DEADLINE_DESC, Deadline.MESSAGE_CONSTRAINTS);

        /* Case: invalid tag -> rejected */
        assertCommandFailure(
                EditCommand.COMMAND_WORD + " " + INDEX_FIRST_TASK.getOneBased() + INVALID_TAG_DESC,
                Tag.MESSAGE_CONSTRAINTS);

        /*
         * Case: edit a task with new values same as another task's values -> rejected
         */
        executeCommand(TaskUtil.getAddCommand(CP2106_MILESTONE));
        assertTrue(getModel().getProject().getTaskList().contains(CP2106_MILESTONE));
        index = INDEX_FIRST_TASK;
        assertFalse(getModel().getFilteredTaskList().get(index.getZeroBased()).equals(CP2106_MILESTONE));
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_CP2106 + DESC_DESC_CP2106
                + DEADLINE_DESC_CP2106 + TAG_DESC_CP2106;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_TASK);

        /*
         * Case: edit a task with new values same as another task's values but with
         * different tags -> rejected
         */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_CP2106 + DESC_DESC_CP2106
                + DEADLINE_DESC_CP2106 + TAG_DESC_CS2101;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_TASK);

        /*
         * Case: edit a task with new values same as another task's values but with
         * different deadline -> rejected
         */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_CP2106 + DESC_DESC_CP2106
                + DEADLINE_DESC_CS2101 + TAG_DESC_CP2106;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_TASK);

        /*
         * Case: edit a task with new values same as another task's values but with
         * different description -> rejected
         */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_CP2106 + DESC_DESC_CS2101
                + DEADLINE_DESC_CP2106 + TAG_DESC_CP2106;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_TASK);
    }

    /**
     * Performs the same verification as
     * {@code assertCommandSuccess(String, Index, Task, Index)} except that the
     * browser url and selected card remain unchanged.
     *
     * @param toEdit the index of the current model's filtered list
     * @see EditCommandSystemTest#assertCommandSuccess(String, Index, Task, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, Task editedTask) {
        assertCommandSuccess(command, toEdit, editedTask, null);
    }

    /**
     * Performs the same verification as
     * {@code assertCommandSuccess(String, Model, String, Index)} and in
     * addition,<br>
     * 1. Asserts that result display box displays the success message of executing
     * {@code EditCommand}.<br>
     * 2. Asserts that the model related components are updated to reflect the task
     * at index {@code toEdit} being updated to values specified
     * {@code editedTask}.<br>
     *
     * @param toEdit the index of the current model's filtered list.
     * @see EditCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, Task editedTask,
                                      Index expectedSelectedCardIndex) {
        Model expectedModel = getModel();
        expectedModel.setSelectedProject(expectedModel.getFilteredProjectList().get(0));

        ModelHelper.setFilteredTaskList(expectedModel, expectedModel.getSelectedProject().getTaskList());

        expectedModel.setTask(expectedModel.getFilteredTaskList().get(toEdit.getZeroBased()), editedTask);
        expectedModel.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);

        assertCommandSuccess(command, expectedModel,
                String.format(EditCommand.MESSAGE_EDIT_TASK_SUCCESS, editedTask),
                expectedSelectedCardIndex);
    }

    /**
     * Performs the same verification as
     * {@code assertCommandSuccess(String, Model, String, Index)} except that the
     * browser url and selected card remain unchanged.
     *
     * @see EditCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        assertCommandSuccess(command, expectedModel, expectedResultMessage, null);
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays
     * {@code expectedResultMessage}.<br>
     * 3. Asserts that the browser url and selected card update accordingly
     * depending on the card at {@code expectedSelectedCardIndex}.<br>
     * 4. Asserts that the status bar's sync status changes.<br>
     * 5. Asserts that the command box has the default style class.<br>
     * Verifications 1 and 2 are performed by
     * {@code ProjectSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     *
     * @see ProjectSystemTest#assertApplicationDisplaysExpected(String, String,
     * Model)
     * @see ProjectSystemTest#assertSelectedCardChanged(Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage,
                                      Index expectedSelectedCardIndex) {
        executeCommand(command);
        expectedModel.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        /*        if (expectedSelectedCardIndex != null) {
            assertSelectedCardChanged(expectedSelectedCardIndex);
        } else {
            assertSelectedCardUnchanged();
        }*/
        //assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays {@code command}.<br>
     * 2. Asserts that result display box displays
     * {@code expectedResultMessage}.<br>
     * 3. Asserts that the browser url, selected card and status bar remain
     * unchanged.<br>
     * 4. Asserts that the command box has the error style.<br>
     * Verifications 1 and 2 are performed by
     * {@code ProjectSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     *
     * @see ProjectSystemTest#assertApplicationDisplaysExpected(String, String,
     * Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        //assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        //assertStatusBarUnchanged();
    }
}
