package seedu.project.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static seedu.project.testutil.TypicalTasks.ALICE;
import static seedu.project.testutil.TypicalTasks.HOON;
import static seedu.project.testutil.TypicalTasks.IDA;
import static seedu.project.testutil.TypicalTasks.getTypicalProject;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.project.commons.exceptions.DataConversionException;
import seedu.project.model.Project;
import seedu.project.model.ReadOnlyProject;

public class JsonProjectStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonProjectStorageTest");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readProject_nullFilePath_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        readProject(null);
    }

    private java.util.Optional<ReadOnlyProject> readProject(String filePath) throws Exception {
        return new JsonProjectStorage(Paths.get(filePath)).readProject(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder) : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readProject("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() throws Exception {

        thrown.expect(DataConversionException.class);
        readProject("notJsonFormatProject.json");

        // IMPORTANT: Any code below an exception-throwing line (like the one above)
        // will be ignored.
        // That means you should not have more than one exception test in one method
    }

    @Test
    public void readProject_invalidTaskProject_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readProject("invalidTaskProject.json");
    }

    @Test
    public void readProject_invalidAndValidTaskProject_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readProject("invalidAndValidTaskProject.json");
    }

    @Test
    public void readAndSaveProject_allInOrder_success() throws Exception {
        Path filePath = testFolder.getRoot().toPath().resolve("TempProject.json");
        Project original = getTypicalProject();
        JsonProjectStorage jsonProjectStorage = new JsonProjectStorage(filePath);

        // Save in new file and read back
        jsonProjectStorage.saveProject(original, filePath);
        ReadOnlyProject readBack = jsonProjectStorage.readProject(filePath).get();
        assertEquals(original, new Project(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addTask(HOON);
        original.removeTask(ALICE);
        jsonProjectStorage.saveProject(original, filePath);
        readBack = jsonProjectStorage.readProject(filePath).get();
        assertEquals(original, new Project(readBack));

        // Save and read without specifying file path
        original.addTask(IDA);
        jsonProjectStorage.saveProject(original); // file path not specified
        readBack = jsonProjectStorage.readProject().get(); // file path not specified
        assertEquals(original, new Project(readBack));

    }

    @Test
    public void saveProject_nullProject_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveProject(null, "SomeFile.json");
    }

    /**
     * Saves {@code project} at the specified {@code filePath}.
     */
    private void saveProject(ReadOnlyProject project, String filePath) {
        try {
            new JsonProjectStorage(Paths.get(filePath)).saveProject(project, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveProject_nullFilePath_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveProject(new Project(), null);
    }
}
