package systemtests;

import static seedu.project.logic.commands.CommandTestUtil.INVALID_INDEX;
import static seedu.project.logic.commands.CommandTestUtil.PATH_EXPORT_INDEX;
import static seedu.project.logic.commands.CommandTestUtil.PATH_EXPORT_SELECTED;

import org.junit.Test;

import seedu.project.commons.core.Messages;
import seedu.project.commons.core.index.Index;
import seedu.project.logic.commands.ExportCommand;
import seedu.project.model.Model;

public class ExportCommandSystemTest extends ProjectSystemTest {
    /**
     * Export project system test
     */
    @Test
    public void exportProject() {

        /* ------------------------ Perform export operations on the shown unfiltered list -------------------------- */

        /* Case: select a project */
        selectProject(Index.fromOneBased(1));
        /* Case: export a selected project, command with leading spaces and trailing spaces in project level
        -> export */
        String command = "   " + ExportCommand.COMMAND_WORD + "  " + PATH_EXPORT_SELECTED + " ";
        assertCommandSuccess(command, 1);

        /* Case: select a task */
        selectTask(Index.fromOneBased(1));
        /* Case: export a selected project, command with leading spaces and trailing spaces in task level
        -> export */
        command = "   " + ExportCommand.COMMAND_WORD + "  " + PATH_EXPORT_SELECTED + " ";
        assertCommandSuccess(command, 1);

        /* Case: list project */
        listProject();
        /* Case: export projects indicated via index in project level */
        command = ExportCommand.COMMAND_WORD + " " + PATH_EXPORT_INDEX;
        assertCommandSuccess(command, 1);

        /* Case: select a project */
        selectProject(Index.fromOneBased(1));
        /* Case: export projects indicated via index in task level  */
        command = ExportCommand.COMMAND_WORD + " " + PATH_EXPORT_INDEX;
        assertCommandSuccess(command, 1);

        /* ----------------------------------- Perform invalid export operations ------------------------------------ */

        /* Case: list project */
        listProject();
        /* Case: invalid project index in project level */
        command = ExportCommand.COMMAND_WORD + " " + INVALID_INDEX + " " + PATH_EXPORT_SELECTED;
        assertCommandFailure(command, Messages.MESSAGE_INVALID_PROJECT_DISPLAYED_INDEX);

        /* Case: select a project */
        selectProject(Index.fromOneBased(1));
        /* Case: invalid project index in task level  */
        command = ExportCommand.COMMAND_WORD + " " + INVALID_INDEX + " " + PATH_EXPORT_SELECTED;
        assertCommandFailure(command, Messages.MESSAGE_INVALID_PROJECT_DISPLAYED_INDEX);
    }

    /**
     * Executes the {@code ExportCommand} that exports {@code toAdd} to the model and asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing {@code AddCommand} with the details of
     * {@code toAdd}.<br>
     * 4. {@code Storage} and {@code TaskListPanel} equal to the corresponding components in
     * the current model added with {@code toAdd}.<br>
     * 5. Status bar's sync status unchanged.<br>
     */
    private void assertCommandSuccess(String command, int count) {
        Model expectedModel = getModel();
        String expectedResultMessage = String.format(ExportCommand.MESSAGE_SUCCESS_PROJECT, count);
        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Task)} except asserts that
     * the,<br>
     * 1. Result display box displays {@code expectedResultMessage}.<br>
     * 2. {@code Storage} and {@code TaskListPanel} equal to the corresponding components in
     * {@code expectedModel}.<br>
     * @see ExportCommandSystemTest#assertCommandSuccess(String, int)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchanged();
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays {@code command}.<br>
     * 2. Command box has the error style class.<br>
     * 3. Result display box displays {@code expectedResultMessage}.<br>
     * 4. {@code Storage} and {@code TaskListPanel} remain unchanged.<br>
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
