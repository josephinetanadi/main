package seedu.project.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.project.logic.commands.CommandTestUtil.VALID_DESCRIPTION_CP2106;
import static seedu.project.model.Model.PREDICATE_SHOW_ALL_TASKS;
import static seedu.project.testutil.TypicalTasks.CP2106;
import static seedu.project.testutil.TypicalTasks.CP2106_MILESTONE;
import static seedu.project.testutil.TypicalTasks.CS2101;
import static seedu.project.testutil.TypicalTasks.CS2101_MILESTONE;

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
import seedu.project.testutil.ProjectListBuilder;
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
        assertFalse(modelManager.hasTask(CS2101_MILESTONE));
    }

    @Test
    public void hasTask_taskInProject_returnsTrue() {
        modelManager.addTask(CS2101_MILESTONE);
        assertTrue(modelManager.hasTask(CS2101_MILESTONE));
    }

    @Test
    public void deleteTask_taskIsSelectedAndFirstTaskInFilteredTaskList_selectionCleared() {
        modelManager.addTask(CS2101_MILESTONE);
        modelManager.setSelectedTask(CS2101_MILESTONE);
        modelManager.deleteTask(CS2101_MILESTONE);
        assertEquals(null, modelManager.getSelectedTask());
    }

    @Test
    public void deleteTask_taskIsSelectedAndSecondTaskInFilteredTaskList_firstTaskSelected() {
        modelManager.addTask(CS2101_MILESTONE);
        modelManager.addTask(CP2106_MILESTONE);
        assertEquals(Arrays.asList(CS2101_MILESTONE, CP2106_MILESTONE), modelManager.getFilteredTaskList());
        modelManager.setSelectedTask(CP2106_MILESTONE);
        modelManager.deleteTask(CP2106_MILESTONE);
        assertEquals(CS2101_MILESTONE, modelManager.getSelectedTask());
    }

    @Test
    public void setTask_taskIsSelected_selectedTaskUpdated() {
        modelManager.addTask(CS2101_MILESTONE);
        modelManager.setSelectedTask(CS2101_MILESTONE);
        Task updatedAlice = new TaskBuilder(CS2101_MILESTONE).withDescription(VALID_DESCRIPTION_CP2106).build();
        modelManager.setTask(CS2101_MILESTONE, updatedAlice);
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
        modelManager.setSelectedTask(CS2101_MILESTONE);
    }

    @Test
    public void setSelectedTask_taskInFilteredTaskList_setsSelectedTask() {
        modelManager.addTask(CS2101_MILESTONE);
        assertEquals(Collections.singletonList(CS2101_MILESTONE), modelManager.getFilteredTaskList());
        modelManager.setSelectedTask(CS2101_MILESTONE);
        assertEquals(CS2101_MILESTONE, modelManager.getSelectedTask());
    }

    @Test
    public void equals() {
        ProjectList projectList = new ProjectListBuilder().withProject(CS2101).withProject(CP2106).build();
        //Project CS2101_proj = new Project(CS2101);
        //ProjectList projectList = new ProjectListBuilder().withProject(CS2101_proj).build();
        Project project = new ProjectBuilder().withTask(CS2101_MILESTONE).withTask(CP2106_MILESTONE).build();
        Project differentProject = new Project();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        ModelManager modelManager = new ModelManager(projectList, project, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(projectList, project, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different project -> returns false
        assertFalse(modelManager.equals(new ModelManager(projectList, differentProject, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = CS2101_MILESTONE.getName().fullName.split("\\s+");
        modelManager.updateFilteredTaskList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(projectList, project, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setProjectFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(projectList, project, differentUserPrefs)));
    }
}
