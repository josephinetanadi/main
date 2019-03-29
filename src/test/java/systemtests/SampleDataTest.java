package systemtests;

import static seedu.project.ui.testutil.GuiTestAssert.assertListMatching;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

//import org.junit.Test;

import seedu.project.model.ProjectList;
import seedu.project.model.project.Project;
import seedu.project.model.task.Task;
import seedu.project.model.util.SampleDataUtil;
import seedu.project.testutil.TestUtil;

public class SampleDataTest extends ProjectSystemTest {
    /**
     * Returns null to force test app to load data of the file in
     * {@code getProjectListSaveLocation()}.
     */
    @Override
    protected ProjectList getInitialProjectList() {
        return null;
    }

    /**
     * Returns null to force test app to load data of the file in
     * {@code getProjectSaveLocation()}.
     */
    @Override
    protected Project getInitialProject() {
        return null;
    }

    /**
     * Returns a non-existent file location to force test app to load sample data.
     */
    @Override
    protected Path getProjectListSaveLocation() {
        Path filePath = TestUtil.getFilePathInSandboxFolder("SomeFileThatDoesNotExist1234567890.xml");
        deleteFileIfExists(filePath);
        return filePath;
    }

    /**
     * Returns a non-existent file location to force test app to load sample data.
     */
    @Override
    protected Path getProjectSaveLocation() {
        Path filePath = TestUtil.getFilePathInSandboxFolder("SomeFileThatDoesNotExist1234567890.xml");
        deleteFileIfExists(filePath);
        return filePath;
    }

    /**
     * Deletes the file at {@code filePath} if it exists.
     */
    private void deleteFileIfExists(Path filePath) {
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException ioe) {
            throw new AssertionError(ioe);
        }
    }

    /**
     * Just for the sake of it
     */
    //@Test
    public void project_dataFileDoesNotExist_loadSampleData() {
        Task[] expectedList = SampleDataUtil.getSampleTasks();
        assertListMatching(getTaskListPanel(), expectedList);
    }
}
