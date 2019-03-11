package seedu.project.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.project.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.project.model.Model.PREDICATE_SHOW_ALL_TASKS;
import static seedu.project.testutil.TypicalTasks.ALICE;
import static seedu.project.testutil.TypicalTasks.BENSON;
import static seedu.project.testutil.TypicalTasks.BOB;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.project.commons.core.GuiSettings;
import seedu.project.model.project.Project;
import seedu.project.model.task.NameContainsKeywordsPredicate;
import seedu.project.model.task.Task;
import seedu.project.model.task.exceptions.TaskNotFoundException;
import seedu.project.testutil.ProjectBuilder;
import seedu.project.testutil.TaskBuilder;

public class ModelManagerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new Project(), new Project(modelManager.getProject()));
        assertEquals(null, modelManager.getSelectedTask());
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        modelManager.setUserPrefs(null);
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setProjectFilePath(Paths.get("address/book/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setProjectFilePath(Paths.get("new/address/book/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        modelManager.setGuiSettings(null);
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setProjectFilePath_nullPath_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        modelManager.setProjectFilePath(null);
    }

    @Test
    public void setProjectFilePath_validPath_setsProjectFilePath() {
        Path path = Paths.get("address/book/file/path");
        modelManager.setProjectFilePath(path);
        assertEquals(path, modelManager.getProjectFilePath());
    }

    @Test
    public void hasTask_nullTask_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        modelManager.hasTask(null);
    }

    @Test
    public void hasTask_taskNotInProject_returnsFalse() {
        assertFalse(modelManager.hasTask(ALICE));
    }

    @Test
    public void hasTask_taskInProject_returnsTrue() {
        modelManager.addTask(ALICE);
        assertTrue(modelManager.hasTask(ALICE));
    }

    @Test
    public void deleteTask_taskIsSelectedAndFirstTaskInFilteredTaskList_selectionCleared() {
        modelManager.addTask(ALICE);
        modelManager.setSelectedTask(ALICE);
        modelManager.deleteTask(ALICE);
        assertEquals(null, modelManager.getSelectedTask());
    }

    @Test
    public void deleteTask_taskIsSelectedAndSecondTaskInFilteredTaskList_firstTaskSelected() {
        modelManager.addTask(ALICE);
        modelManager.addTask(BOB);
        assertEquals(Arrays.asList(ALICE, BOB), modelManager.getFilteredTaskList());
        modelManager.setSelectedTask(BOB);
        modelManager.deleteTask(BOB);
        assertEquals(ALICE, modelManager.getSelectedTask());
    }

    @Test
    public void setTask_taskIsSelected_selectedTaskUpdated() {
        modelManager.addTask(ALICE);
        modelManager.setSelectedTask(ALICE);
        Task updatedAlice = new TaskBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        modelManager.setTask(ALICE, updatedAlice);
        assertEquals(updatedAlice, modelManager.getSelectedTask());
    }

    @Test
    public void getFilteredTaskList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        modelManager.getFilteredTaskList().remove(0);
    }

    @Test
    public void setSelectedTask_taskNotInFilteredTaskList_throwsTaskNotFoundException() {
        thrown.expect(TaskNotFoundException.class);
        modelManager.setSelectedTask(ALICE);
    }

    @Test
    public void setSelectedTask_taskInFilteredTaskList_setsSelectedTask() {
        modelManager.addTask(ALICE);
        assertEquals(Collections.singletonList(ALICE), modelManager.getFilteredTaskList());
        modelManager.setSelectedTask(ALICE);
        assertEquals(ALICE, modelManager.getSelectedTask());
    }

    @Test
    public void equals() {
        Project project = new ProjectBuilder().withTask(ALICE).withTask(BENSON).build();
        Project differentProject = new Project();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(project, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(project, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different project -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentProject, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredTaskList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(project, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setProjectFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(project, differentUserPrefs)));
    }
}
