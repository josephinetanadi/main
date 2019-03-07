package seedu.project.ui;

import static java.time.Duration.ofMillis;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static seedu.project.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.project.testutil.TypicalTasks.getTypicalTasks;
import static seedu.project.ui.testutil.GuiTestAssert.assertCardDisplaysTask;
import static seedu.project.ui.testutil.GuiTestAssert.assertCardEquals;

import java.util.Collections;

import org.junit.Test;

import guitests.guihandles.TaskCardHandle;
import guitests.guihandles.TaskListPanelHandle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.project.model.task.Address;
import seedu.project.model.task.Email;
import seedu.project.model.task.Name;
import seedu.project.model.task.Phone;
import seedu.project.model.task.Task;

public class TaskListPanelTest extends GuiUnitTest {
    private static final ObservableList<Task> TYPICAL_TASKS = FXCollections.observableList(getTypicalTasks());

    private static final long CARD_CREATION_AND_DELETION_TIMEOUT = 2500;

    private final SimpleObjectProperty<Task> selectedTask = new SimpleObjectProperty<>();
    private TaskListPanelHandle taskListPanelHandle;

    @Test
    public void display() {
        initUi(TYPICAL_TASKS);

        for (int i = 0; i < TYPICAL_TASKS.size(); i++) {
            taskListPanelHandle.navigateToCard(TYPICAL_TASKS.get(i));
            Task expectedTask = TYPICAL_TASKS.get(i);
            TaskCardHandle actualCard = taskListPanelHandle.getTaskCardHandle(i);

            assertCardDisplaysTask(expectedTask, actualCard);
            assertEquals(Integer.toString(i + 1) + ". ", actualCard.getId());
        }
    }

    @Test
    public void selection_modelSelectedTaskChanged_selectionChanges() {
        initUi(TYPICAL_TASKS);
        Task secondTask = TYPICAL_TASKS.get(INDEX_SECOND_PERSON.getZeroBased());
        guiRobot.interact(() -> selectedTask.set(secondTask));
        guiRobot.pauseForHuman();

        TaskCardHandle expectedTask = taskListPanelHandle.getTaskCardHandle(INDEX_SECOND_PERSON.getZeroBased());
        TaskCardHandle selectedTask = taskListPanelHandle.getHandleToSelectedCard();
        assertCardEquals(expectedTask, selectedTask);
    }

    /**
     * Verifies that creating and deleting large number of tasks in
     * {@code TaskListPanel} requires lesser than
     * {@code CARD_CREATION_AND_DELETION_TIMEOUT} milliseconds to execute.
     */
    @Test
    public void performanceTest() {
        ObservableList<Task> backingList = createBackingList(10000);

        assertTimeoutPreemptively(ofMillis(CARD_CREATION_AND_DELETION_TIMEOUT), () -> {
            initUi(backingList);
            guiRobot.interact(backingList::clear);
        }, "Creation and deletion of task cards exceeded time limit");
    }

    /**
     * Returns a list of tasks containing {@code taskCount} tasks that is used to
     * populate the {@code TaskListPanel}.
     */
    private ObservableList<Task> createBackingList(int taskCount) {
        ObservableList<Task> backingList = FXCollections.observableArrayList();
        for (int i = 0; i < taskCount; i++) {
            Name name = new Name(i + "a");
            Phone phone = new Phone("000");
            Email email = new Email("a@aa");
            Address address = new Address("a");
            Task task = new Task(name, phone, email, address, Collections.emptySet());
            backingList.add(task);
        }
        return backingList;
    }

    /**
     * Initializes {@code taskListPanelHandle} with a {@code TaskListPanel} backed
     * by {@code backingList}. Also shows the {@code Stage} that displays only
     * {@code TaskListPanel}.
     */
    private void initUi(ObservableList<Task> backingList) {
        TaskListPanel taskListPanel = new TaskListPanel(backingList, selectedTask, selectedTask::set);
        uiPartRule.setUiPart(taskListPanel);

        taskListPanelHandle = new TaskListPanelHandle(
                getChildNode(taskListPanel.getRoot(), TaskListPanelHandle.PERSON_LIST_VIEW_ID));
    }
}
