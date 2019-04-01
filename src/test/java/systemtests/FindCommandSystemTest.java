package systemtests;

import static org.junit.Assert.assertFalse;
import static seedu.project.commons.core.Messages.MESSAGE_TASKS_LISTED_OVERVIEW;
import static seedu.project.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.project.testutil.TypicalTasks.FEEDBACK;
import static seedu.project.testutil.TypicalTasks.KEYWORD_MATCHING_TEST;
import static seedu.project.testutil.TypicalTasks.QUIZ;
import static seedu.project.testutil.TypicalTasks.TEACHING_FEEDBACK;
import static seedu.project.testutil.TypicalTasks.TUTORIAL;

import java.util.ArrayList;
import java.util.List;

//import org.junit.Test;

import seedu.project.commons.core.index.Index;
import seedu.project.logic.commands.DeleteCommand;
import seedu.project.logic.commands.FindCommand;
import seedu.project.logic.commands.RedoCommand;
import seedu.project.logic.commands.UndoCommand;
import seedu.project.model.Model;
import seedu.project.model.tag.Tag;

public class FindCommandSystemTest extends ProjectSystemTest {

    /**
     * Just for the sake of it
     */
    //@Test
    public void find() {
        /*
         * Case: find multiple tasks in project, command with leading spaces and
         * trailing spaces -> 2 tasks found
         */
        String command = "   " + FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_TEST + "   ";
        Model expectedModel = getModel();
        ModelHelper.setFilteredTaskList(expectedModel, FEEDBACK, TEACHING_FEEDBACK); // both task contains feedback
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /*
         * Case: repeat previous find command where task list is displaying the tasks we
         * are finding -> 2 tasks found
         */
        command = FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_TEST;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /*
         * Case: find task where task list is not displaying the task we are finding ->
         * 1 task found
         */
        command = FindCommand.COMMAND_WORD + " Quiz";
        ModelHelper.setFilteredTaskList(expectedModel, QUIZ);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple tasks in project, 2 keywords -> 2 tasks found */
        command = FindCommand.COMMAND_WORD + " Teaching Feedback";
        ModelHelper.setFilteredTaskList(expectedModel, TEACHING_FEEDBACK, FEEDBACK);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /*
         * Case: find multiple tasks in project, 2 keywords in reversed order -> 2
         * tasks found
         */
        command = FindCommand.COMMAND_WORD + " Feedback Teaching";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /*
         * Case: find multiple tasks in project, 2 keywords with 1 repeat -> 2
         * tasks found
         */
        command = FindCommand.COMMAND_WORD + " Teaching Feedback Teaching";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /*
         * Case: find multiple tasks in project, 2 matching keywords and 1
         * non-matching keyword -> 2 tasks found
         */
        command = FindCommand.COMMAND_WORD + " Teaching Feedback NonMatchingKeyWord";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: undo previous find command -> rejected */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: redo previous find command -> rejected */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /*
         * Case: find same tasks in project after deleting 1 of them -> 1 task
         * found
         */
        executeCommand(DeleteCommand.COMMAND_WORD + " 1");
        assertFalse(getModel().getProject().getTaskList().contains(FEEDBACK));
        command = FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_TEST;
        expectedModel = getModel();
        ModelHelper.setFilteredTaskList(expectedModel, TEACHING_FEEDBACK);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /*
         * Case: find task in project, keyword is same as name but of different
         * case -> 1 task found
         */
        command = FindCommand.COMMAND_WORD + " FeEdBaCk";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /*
         * Case: find task in project, keyword is substring of name -> 0 tasks
         * found
         */
        command = FindCommand.COMMAND_WORD + " Fee";
        ModelHelper.setFilteredTaskList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /*
         * Case: find task in project, name is substring of keyword -> 0 tasks
         * found
         */
        command = FindCommand.COMMAND_WORD + " Feedbacks";
        ModelHelper.setFilteredTaskList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find task not in project -> 0 tasks found */
        command = FindCommand.COMMAND_WORD + " Tutorial";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find deadline of task in project -> 0 tasks found */
        command = FindCommand.COMMAND_WORD + " " + TEACHING_FEEDBACK.getDeadline().value;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find description of task in project -> 0 tasks found */
        command = FindCommand.COMMAND_WORD + " " + TUTORIAL.getDescription().value;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find tags of task in project -> 0 tasks found */
        List<Tag> tags = new ArrayList<>(TUTORIAL.getTags());
        command = FindCommand.COMMAND_WORD + " " + tags.get(0).tagName;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find while a task is selected -> selected card deselected */
        showAllTasks();
        selectTask(Index.fromOneBased(1));
        assertFalse(getTaskListPanel().getHandleToSelectedCard().getName().equals(TEACHING_FEEDBACK
                .getName().fullName));
        command = FindCommand.COMMAND_WORD + " Teaching";
        ModelHelper.setFilteredTaskList(expectedModel, TEACHING_FEEDBACK);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardDeselected();

        /* Case: find task in empty project -> 0 tasks found */
        deleteAllTasks();
        command = FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_TEST;
        expectedModel = getModel();
        ModelHelper.setFilteredTaskList(expectedModel, TEACHING_FEEDBACK);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: mixed case command word -> rejected */
        command = "FiNd Teaching";
        assertCommandFailure(command, MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Executes {@code command} and verifies that the command box displays an empty
     * string, the result display box displays
     * {@code Messages#MESSAGE_TASKS_LISTED_OVERVIEW} with the number of people in
     * the filtered list, and the model related components equal to
     * {@code expectedModel}. These verifications are done by
     * {@code ProjectSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the status bar remains unchanged, and the command box has
     * the default style class, and the selected card updated accordingly, depending
     * on {@code cardStatus}.
     *
     * @see ProjectSystemTest#assertApplicationDisplaysExpected(String, String,
     *      Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel) {
        String expectedResultMessage = String.format(MESSAGE_TASKS_LISTED_OVERVIEW,
                expectedModel.getFilteredTaskList().size());

        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchanged();
    }

    /**
     * Executes {@code command} and verifies that the command box displays
     * {@code command}, the result display box displays
     * {@code expectedResultMessage} and the model related components equal to the
     * current model. These verifications are done by
     * {@code ProjectSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the browser url, selected card and status bar remain
     * unchanged, and the command box has the error style.
     *
     * @see ProjectSystemTest#assertApplicationDisplaysExpected(String, String,
     *      Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
