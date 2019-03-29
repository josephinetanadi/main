package seedu.project;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Supplier;

import javafx.stage.Screen;
import javafx.stage.Stage;
import seedu.project.commons.core.Config;
import seedu.project.commons.core.GuiSettings;
import seedu.project.commons.exceptions.DataConversionException;
import seedu.project.model.Model;
import seedu.project.model.ModelManager;
import seedu.project.model.ProjectList;
import seedu.project.model.ReadOnlyProjectList;
import seedu.project.model.UserPrefs;
import seedu.project.storage.JsonProjectListStorage;
import seedu.project.storage.UserPrefsStorage;
import seedu.project.testutil.TestUtil;
import systemtests.ModelHelper;

/**
 * This class is meant to override some properties of MainApp so that it will be suited for
 * testing
 */
public class TestApp extends MainApp {


    public static final Path SAVE_LOCATION_FOR_TESTING =
            TestUtil.getFilePathInSandboxFolder("sampleProjectList.json");

    protected static final Path DEFAULT_PREF_FILE_LOCATION_FOR_TESTING =
            TestUtil.getFilePathInSandboxFolder("pref_testing.json");

    protected Supplier<ReadOnlyProjectList> initialProjectListDataSupplier = () -> null;
    protected Path saveProjectListFileLocation = SAVE_LOCATION_FOR_TESTING;

    public TestApp() {
    }

    public TestApp(Supplier<ReadOnlyProjectList> pl, Path path) {
        super();
        this.initialProjectListDataSupplier = pl;
        this.saveProjectListFileLocation = path;

        // If some initial local data has been provided, write those to the file
        if (pl.get() != null) {
            JsonProjectListStorage jsonProjectListStorage = new JsonProjectListStorage(path);
            try {
                jsonProjectListStorage.saveProjectList(pl.get());
            } catch (IOException ioe) {
                throw new AssertionError(ioe);
            }
        }
    }

    @Override
    protected Config initConfig(Path configFilePath) {
        Config config = super.initConfig(configFilePath);
        config.setUserPrefsFilePath(DEFAULT_PREF_FILE_LOCATION_FOR_TESTING);
        return config;
    }

    @Override
    protected UserPrefs initPrefs(UserPrefsStorage storage) {
        UserPrefs userPrefs = super.initPrefs(storage);
        double x = Screen.getPrimary().getVisualBounds().getMinX();
        double y = Screen.getPrimary().getVisualBounds().getMinY();
        userPrefs.setGuiSettings(new GuiSettings(600.0, 600.0, (int) x, (int) y));
        userPrefs.setProjectListFilePath(saveProjectListFileLocation);
        return userPrefs;
    }

    /**
     * Returns a defensive copy of the project list data stored inside the storage file.
     */
    public ProjectList readStorageProjectList() {
        try {
            return new ProjectList(storage.readProjectList().get());
        } catch (DataConversionException dce) {
            throw new AssertionError("Data is not in the ProjectList format.", dce);
        } catch (IOException ioe) {
            throw new AssertionError("Storage file cannot be found.", ioe);
        }
    }

    /**
     * Returns the file path of the storage file.
     */
    public Path getProjectListSaveLocation() {
        return storage.getProjectListFilePath();
    }

    /**
     * Returns a defensive copy of the model.
     */
    public Model getModel() {
        Model copy = new ModelManager((model.getProjectList()), (model.getProject()), new UserPrefs());
        ModelHelper.setFilteredProjectList(copy, model.getFilteredProjectList());
        ModelHelper.setFilteredTaskList(copy, model.getFilteredTaskList());
        return copy;
    }

    @Override
    public void start(Stage primaryStage) {
        ui.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
