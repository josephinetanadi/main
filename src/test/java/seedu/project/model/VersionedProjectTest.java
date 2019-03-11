package seedu.project.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.project.testutil.TypicalTasks.AMY;
import static seedu.project.testutil.TypicalTasks.BOB;
import static seedu.project.testutil.TypicalTasks.CARL;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.project.model.project.Project;
import seedu.project.model.project.ReadOnlyProject;
import seedu.project.model.project.VersionedProject;
import seedu.project.testutil.ProjectBuilder;

public class VersionedProjectTest {

    private final ReadOnlyProject projectWithAmy = new ProjectBuilder().withTask(AMY).build();
    private final ReadOnlyProject projectWithBob = new ProjectBuilder().withTask(BOB).build();
    private final ReadOnlyProject projectWithCarl = new ProjectBuilder().withTask(CARL).build();
    private final ReadOnlyProject emptyProject = new ProjectBuilder().build();

    @Test
    public void commit_singleProject_noStatesRemovedCurrentStateSaved() {
        VersionedProject versionedProject = prepareProjectList(emptyProject);

        versionedProject.commit();
        assertProjectListStatus(versionedProject, Collections.singletonList(emptyProject), emptyProject,
                Collections.emptyList());
    }

    @Test
    public void commit_multipleProjectPointerAtEndOfStateList_noStatesRemovedCurrentStateSaved() {
        VersionedProject versionedProject = prepareProjectList(emptyProject, projectWithAmy, projectWithBob);

        versionedProject.commit();
        assertProjectListStatus(versionedProject, Arrays.asList(emptyProject, projectWithAmy, projectWithBob),
                projectWithBob, Collections.emptyList());
    }

    @Test
    public void commit_multipleProjectPointerNotAtEndOfStateList_statesAfterPointerRemovedCurrentStateSaved() {
        VersionedProject versionedProject = prepareProjectList(emptyProject, projectWithAmy, projectWithBob);
        shiftCurrentStatePointerLeftwards(versionedProject, 2);

        versionedProject.commit();
        assertProjectListStatus(versionedProject, Collections.singletonList(emptyProject), emptyProject,
                Collections.emptyList());
    }

    @Test
    public void canUndo_multipleProjectPointerAtEndOfStateList_returnsTrue() {
        VersionedProject versionedProject = prepareProjectList(emptyProject, projectWithAmy, projectWithBob);

        assertTrue(versionedProject.canUndo());
    }

    @Test
    public void canUndo_multipleProjectPointerAtStartOfStateList_returnsTrue() {
        VersionedProject versionedProject = prepareProjectList(emptyProject, projectWithAmy, projectWithBob);
        shiftCurrentStatePointerLeftwards(versionedProject, 1);

        assertTrue(versionedProject.canUndo());
    }

    @Test
    public void canUndo_singleProject_returnsFalse() {
        VersionedProject versionedProject = prepareProjectList(emptyProject);

        assertFalse(versionedProject.canUndo());
    }

    @Test
    public void canUndo_multipleProjectPointerAtStartOfStateList_returnsFalse() {
        VersionedProject versionedProject = prepareProjectList(emptyProject, projectWithAmy, projectWithBob);
        shiftCurrentStatePointerLeftwards(versionedProject, 2);

        assertFalse(versionedProject.canUndo());
    }

    @Test
    public void canRedo_multipleProjectPointerNotAtEndOfStateList_returnsTrue() {
        VersionedProject versionedProject = prepareProjectList(emptyProject, projectWithAmy, projectWithBob);
        shiftCurrentStatePointerLeftwards(versionedProject, 1);

        assertTrue(versionedProject.canRedo());
    }

    @Test
    public void canRedo_multipleProjectPointerAtStartOfStateList_returnsTrue() {
        VersionedProject versionedProject = prepareProjectList(emptyProject, projectWithAmy, projectWithBob);
        shiftCurrentStatePointerLeftwards(versionedProject, 2);

        assertTrue(versionedProject.canRedo());
    }

    @Test
    public void canRedo_singleProject_returnsFalse() {
        VersionedProject versionedProject = prepareProjectList(emptyProject);

        assertFalse(versionedProject.canRedo());
    }

    @Test
    public void canRedo_multipleProjectPointerAtEndOfStateList_returnsFalse() {
        VersionedProject versionedProject = prepareProjectList(emptyProject, projectWithAmy, projectWithBob);

        assertFalse(versionedProject.canRedo());
    }

    @Test
    public void undo_multipleProjectPointerAtEndOfStateList_success() {
        VersionedProject versionedProject = prepareProjectList(emptyProject, projectWithAmy, projectWithBob);

        versionedProject.undo();
        assertProjectListStatus(versionedProject, Collections.singletonList(emptyProject), projectWithAmy,
                Collections.singletonList(projectWithBob));
    }

    @Test
    public void undo_multipleProjectPointerNotAtStartOfStateList_success() {
        VersionedProject versionedProject = prepareProjectList(emptyProject, projectWithAmy, projectWithBob);
        shiftCurrentStatePointerLeftwards(versionedProject, 1);

        versionedProject.undo();
        assertProjectListStatus(versionedProject, Collections.emptyList(), emptyProject,
                Arrays.asList(projectWithAmy, projectWithBob));
    }

    @Test
    public void undo_singleProject_throwsNoUndoableStateException() {
        VersionedProject versionedProject = prepareProjectList(emptyProject);

        assertThrows(VersionedProject.NoUndoableStateException.class, versionedProject::undo);
    }

    @Test
    public void undo_multipleProjectPointerAtStartOfStateList_throwsNoUndoableStateException() {
        VersionedProject versionedProject = prepareProjectList(emptyProject, projectWithAmy, projectWithBob);
        shiftCurrentStatePointerLeftwards(versionedProject, 2);

        assertThrows(VersionedProject.NoUndoableStateException.class, versionedProject::undo);
    }

    @Test
    public void redo_multipleProjectPointerNotAtEndOfStateList_success() {
        VersionedProject versionedProject = prepareProjectList(emptyProject, projectWithAmy, projectWithBob);
        shiftCurrentStatePointerLeftwards(versionedProject, 1);

        versionedProject.redo();
        assertProjectListStatus(versionedProject, Arrays.asList(emptyProject, projectWithAmy), projectWithBob,
                Collections.emptyList());
    }

    @Test
    public void redo_multipleProjectPointerAtStartOfStateList_success() {
        VersionedProject versionedProject = prepareProjectList(emptyProject, projectWithAmy, projectWithBob);
        shiftCurrentStatePointerLeftwards(versionedProject, 2);

        versionedProject.redo();
        assertProjectListStatus(versionedProject, Collections.singletonList(emptyProject), projectWithAmy,
                Collections.singletonList(projectWithBob));
    }

    @Test
    public void redo_singleProject_throwsNoRedoableStateException() {
        VersionedProject versionedProject = prepareProjectList(emptyProject);

        assertThrows(VersionedProject.NoRedoableStateException.class, versionedProject::redo);
    }

    @Test
    public void redo_multipleProjectPointerAtEndOfStateList_throwsNoRedoableStateException() {
        VersionedProject versionedProject = prepareProjectList(emptyProject, projectWithAmy, projectWithBob);

        assertThrows(VersionedProject.NoRedoableStateException.class, versionedProject::redo);
    }

    @Test
    public void equals() {
        VersionedProject versionedProject = prepareProjectList(projectWithAmy, projectWithBob);

        // same values -> returns true
        VersionedProject copy = prepareProjectList(projectWithAmy, projectWithBob);
        assertTrue(versionedProject.equals(copy));

        // same object -> returns true
        assertTrue(versionedProject.equals(versionedProject));

        // null -> returns false
        assertFalse(versionedProject.equals(null));

        // different types -> returns false
        assertFalse(versionedProject.equals(1));

        // different state list -> returns false
        VersionedProject differentProjectList = prepareProjectList(projectWithBob, projectWithCarl);
        assertFalse(versionedProject.equals(differentProjectList));

        // different current pointer index -> returns false
        VersionedProject differentCurrentStatePointer = prepareProjectList(projectWithAmy, projectWithBob);
        shiftCurrentStatePointerLeftwards(versionedProject, 1);
        assertFalse(versionedProject.equals(differentCurrentStatePointer));
    }

    /**
     * Asserts that {@code versionedProject} is currently pointing at
     * {@code expectedCurrentState}, states before
     * {@code versionedProject#currentStatePointer} is equal to
     * {@code expectedStatesBeforePointer}, and states after
     * {@code versionedProject#currentStatePointer} is equal to
     * {@code expectedStatesAfterPointer}.
     */
    private void assertProjectListStatus(VersionedProject versionedProject,
            List<ReadOnlyProject> expectedStatesBeforePointer, ReadOnlyProject expectedCurrentState,
            List<ReadOnlyProject> expectedStatesAfterPointer) {
        // check state currently pointing at is correct
        assertEquals(new Project(versionedProject), expectedCurrentState);

        // shift pointer to start of state list
        while (versionedProject.canUndo()) {
            versionedProject.undo();
        }

        // check states before pointer are correct
        for (ReadOnlyProject expectedProject : expectedStatesBeforePointer) {
            assertEquals(expectedProject, new Project(versionedProject));
            versionedProject.redo();
        }

        // check states after pointer are correct
        for (ReadOnlyProject expectedProject : expectedStatesAfterPointer) {
            versionedProject.redo();
            assertEquals(expectedProject, new Project(versionedProject));
        }

        // check that there are no more states after pointer
        assertFalse(versionedProject.canRedo());

        // revert pointer to original position
        expectedStatesAfterPointer.forEach(unused -> versionedProject.undo());
    }

    /**
     * Creates and returns a {@code VersionedProject} with the {@code projectStates}
     * added into it, and the {@code VersionedProject#currentStatePointer} at the
     * end of list.
     */
    private VersionedProject prepareProjectList(ReadOnlyProject... projectStates) {
        assertFalse(projectStates.length == 0);

        VersionedProject versionedProject = new VersionedProject(projectStates[0]);
        for (int i = 1; i < projectStates.length; i++) {
            versionedProject.resetData(projectStates[i]);
            versionedProject.commit();
        }

        return versionedProject;
    }

    /**
     * Shifts the {@code versionedProject#currentStatePointer} by {@code count} to
     * the left of its list.
     */
    private void shiftCurrentStatePointerLeftwards(VersionedProject versionedProject, int count) {
        for (int i = 0; i < count; i++) {
            versionedProject.undo();
        }
    }
}
