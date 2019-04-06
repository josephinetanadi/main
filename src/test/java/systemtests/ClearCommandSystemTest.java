package systemtests;

import static seedu.project.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.project.testutil.TypicalTasks.KEYWORD_MATCHING_TEST;

//import org.junit.Test;

import seedu.project.commons.core.index.Index;
import seedu.project.logic.commands.ClearCommand;
import seedu.project.logic.commands.RedoCommand;
import seedu.project.logic.commands.UndoCommand;
import seedu.project.model.Model;

public class ClearCommandSystemTest extends ProjectSystemTest {

    /**
     * Just for the sake of it
     */
    //@Test
    public void clear() {
        Model expectedModel = getModel();
        expectedModel.setSelectedProject(expectedModel.getFilteredProjectList().get(0));

        selectProject(Index.fromOneBased(1));

        /* Case: clear non-empty project, command with leading spaces and trailing alphanumeric characters and
         * spaces -> cleared
         */

        expectedModel.getSelectedProject().clearTasks();
        assertCommandSuccess("   " + ClearCommand.COMMAND_WORD + " ab12   ", expectedModel);
        //assertSelectedCardUnchanged();

        /* Case: undo clearing project -> original project restored */
        expectedModel = getModel();
        String command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, expectedResultMessage, expectedModel);
        //assertSelectedCardUnchanged();

        /* Case: redo clearing project -> cleared */
        expectedModel.setSelectedProject(expectedModel.getFilteredProjectList().get(0));
        expectedModel.getSelectedProject().clearTasks();
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, expectedResultMessage, expectedModel);
        //assertSelectedCardUnchanged();

        /* Case: selects first card in task list and clears project -> cleared and no card selected */
        executeCommand(UndoCommand.COMMAND_WORD); // restores the original project
        selectTask(Index.fromOneBased(1));
        assertCommandSuccess(ClearCommand.COMMAND_WORD, expectedModel);
        //assertSelectedCardDeselected();

        /* Case: filters the task list before clearing -> entire project cleared */
        executeCommand(UndoCommand.COMMAND_WORD); // restores the original project
        showTasksWithName(KEYWORD_MATCHING_TEST);
        assertCommandSuccess(ClearCommand.COMMAND_WORD, expectedModel);
        //assertSelectedCardUnchanged();

        /* Case: clear empty project -> cleared */
        assertCommandSuccess(ClearCommand.COMMAND_WORD, expectedModel);
        //assertSelectedCardUnchanged();

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("ClEaR", MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Executes {@code command} and verifies that the command box displays an empty string, the result display
     * box displays {@code ClearCommand#MESSAGE_SUCCESS} and the model related components equal to an empty model.
     * These verifications are done by
     * {@code ProjectSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the command box has the default style class and the status bar's sync status changes.
     * @see ProjectSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */

    private void assertCommandSuccess(String command, Model expectedModel) {
        assertCommandSuccess(command, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String)} except that the result box displays
     * {@code expectedResultMessage} and the model related components equal to {@code expectedModel}.
     * @see ClearCommandSystemTest#assertCommandSuccess(String, Model)
     */
    private void assertCommandSuccess(String command, String expectedResultMessage, Model expectedModel) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        //assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and verifies that the command box displays {@code command}, the result display
     * box displays {@code expectedResultMessage} and the model related components equal to the current model.
     * These verifications are done by
     * {@code ProjectSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the browser url, selected card and status bar remain unchanged, and the command box has the
     * error style.
     * @see ProjectSystemTest#assertApplicationDisplaysExpected(String, String, Model)
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
