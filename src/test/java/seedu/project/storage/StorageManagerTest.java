package seedu.project.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static seedu.project.testutil.TypicalTasks.getTypicalProject;
import static seedu.project.testutil.TypicalTasks.getTypicalProjectList;

import java.nio.file.Path;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.project.commons.core.GuiSettings;
import seedu.project.model.ProjectList;
import seedu.project.model.UserPrefs;
import seedu.project.model.project.Project;
import seedu.project.model.project.ReadOnlyProject;
import seedu.project.model.ReadOnlyProjectList;


public class StorageManagerTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    private StorageManager storageManager;

    @Before
    public void setUp() {
        JsonProjectListStorage projectListStorage = new JsonProjectListStorage(getTempFilePath("pl"));
        JsonProjectStorage projectStorage = new JsonProjectStorage(getTempFilePath("p"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        storageManager = new StorageManager(projectListStorage, userPrefsStorage);
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.getRoot().toPath().resolve(fileName);
    }

    @Test
    public void prefsReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is
         * properly wired to the {@link JsonUserPrefsStorage} class. More extensive
         * testing of UserPref saving/reading is done in {@link
         * JsonUserPrefsStorageTest} class.
         */
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(new GuiSettings(300, 600, 4, 6));
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void projectReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is
         * properly wired to the {@link JsonProjectStorage} class. More extensive
         * testing of UserPref saving/reading is done in {@link JsonProjectStorageTest}
         * class.
         */
        ReadOnlyProjectList original = getTypicalProjectList();
        storageManager.saveProjectList(original);
        ReadOnlyProjectList retrieved = storageManager.readProjectList().get();
        assertEquals(original, new ProjectList(retrieved));
    }

    @Test
    public void getProjectFilePath() {
        assertNotNull(storageManager.getProjectListFilePath());
    }

}
