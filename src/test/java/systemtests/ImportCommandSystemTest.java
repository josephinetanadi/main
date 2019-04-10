package systemtests;

import static seedu.project.TestApp.IMPORT_LOCATION_FOR_TESTING;

import org.junit.Test;

import seedu.project.logic.commands.ImportCommand;
import seedu.project.model.Model;
import seedu.project.model.project.Project;
import seedu.project.model.util.SampleDataUtil;

public class ImportCommandSystemTest extends ProjectSystemTest {
    /**
     * Import project system test
     */
    @Test
    public void importProject() throws Exception {

        /* ------------------------ Perform import operations on the shown unfiltered list -------------------------- */

        createImportFile();

        /* Case: import a project list at project level
        * command with leading spaces and trailing spaces in project level -> imported */
        String command = "   " + ImportCommand.COMMAND_WORD + "  " + IMPORT_LOCATION_FOR_TESTING + " ";
        assertCommandSuccess(command, 2);
    }

    /**
     * Executes {@code ImportCommand} that imports {@code getProjectsToImport} to the model and asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing {@code AddCommand} with the details of
     * {@code toAdd}.<br>
     * 4. {@code Storage} and {@code TaskListPanel} equal to the corresponding components in
     * the current model added with {@code toAdd}.<br>
     */
    private void assertCommandSuccess(String command, int count) {
        Model expectedModel = getModel();
        for (Project p : SampleDataUtil.getProjectsToImport()) {
            expectedModel.addProject(p);
        }
        String expectedResultMessage = String.format(ImportCommand.MESSAGE_SUCCESS_PROJECT, count);
        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Task)} except asserts that
     * the,<br>
     * 1. Result display box displays {@code expectedResultMessage}.<br>
     * 2. {@code Storage} and {@code TaskListPanel} equal to the corresponding components in
     * {@code expectedModel}.<br>
     * @see ImportCommandSystemTest#assertCommandSuccess(String, int)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
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
