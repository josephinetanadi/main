package seedu.project.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static seedu.project.testutil.TypicalTasks.CP2106;
import static seedu.project.testutil.TypicalTasks.FEEDBACK;
import static seedu.project.testutil.TypicalTasks.LECTURE;
import static seedu.project.testutil.TypicalTasks.SAMPLE2;
import static seedu.project.testutil.TypicalTasks.TUTORIAL;
import static seedu.project.testutil.TypicalTasks.CS2101;
import static seedu.project.testutil.TypicalTasks.getTypicalProject;
import static seedu.project.testutil.TypicalTasks.getTypicalProjectList;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.project.commons.exceptions.DataConversionException;
import seedu.project.model.ProjectList;
import seedu.project.model.ReadOnlyProjectList;

public class JsonProjectListStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data",
            "JsonProjectListStorageTest");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readProjectList_nullFilePath_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        readProjectList(null);
    }

    private java.util.Optional<ReadOnlyProjectList> readProjectList(String filePath) throws Exception {
        return new JsonProjectListStorage(Paths.get(filePath)).readProjectList(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder) : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readProjectList("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() throws Exception {

        thrown.expect(DataConversionException.class);
        readProjectList("notJsonFormatProject.json");

        // IMPORTANT: Any code below an exception-throwing line (like the one above)
        // will be ignored.
        // That means you should not have more than one exception test in one method
    }

    @Test
    public void readAndSaveProjectList_allInOrder_success() throws Exception {
        Path filePath = testFolder.getRoot().toPath().resolve("TempProject.json");
        ProjectList original = getTypicalProjectList();
        JsonProjectListStorage jsonProjectListStorage = new JsonProjectListStorage(filePath);

        // Save in new file and read back
        jsonProjectListStorage.saveProjectList(original, filePath);
        ReadOnlyProjectList readBack = jsonProjectListStorage.readProjectList(filePath).get();
        assertEquals(original.getProjectList(), new ProjectList(readBack).getProjectList());

        // Modify data, overwrite exiting file, and read back
        original.addProject(CS2101);
        original.removeProject(SAMPLE2);
        jsonProjectListStorage.saveProjectList(original, filePath);
        readBack = jsonProjectListStorage.readProjectList(filePath).get();
        assertEquals(original.getProjectList(), new ProjectList(readBack).getProjectList());

        // Save and read without specifying file path
        original.addProject(CP2106);
        jsonProjectListStorage.saveProjectList(original); // file path not specified
        readBack = jsonProjectListStorage.readProjectList().get(); // file path not specified
        assertEquals(original.getProjectList(), new ProjectList(readBack).getProjectList());

    }

    @Test
    public void saveProjectList_nullProject_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveProjectList(null, "SomeFile.json");
    }

    /**
     * Saves {@code project} at the specified {@code filePath}.
     */
    private void saveProjectList(ReadOnlyProjectList projectList, String filePath) {
        try {
            new JsonProjectListStorage(Paths.get(filePath)).saveProjectList(projectList, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveProjectList_nullFilePath_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveProjectList(new ProjectList(), null);
    }
}
