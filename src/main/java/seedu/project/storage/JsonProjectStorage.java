package seedu.project.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.project.commons.core.LogsCenter;
import seedu.project.commons.exceptions.DataConversionException;
import seedu.project.commons.exceptions.IllegalValueException;
import seedu.project.commons.util.FileUtil;
import seedu.project.commons.util.JsonUtil;
import seedu.project.model.ReadOnlyProject;

/**
 * A class to access Project data stored as a json file on the hard disk.
 */
public class JsonProjectStorage implements ProjectStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonProjectStorage.class);

    private Path filePath;
    private Path backupFilePath;

    public JsonProjectStorage(Path filePath) {
        this.filePath = filePath;
        backupFilePath = Paths.get(filePath.toString() + ".backup");
    }

    public Path getProjectFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyProject> readProject() throws DataConversionException {
        return readProject(filePath);
    }

    /**
     * Similar to {@link #readProject()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyProject> readProject(Path filePath) throws DataConversionException {
        requireNonNull(filePath);

        Optional<JsonSerializableProject> jsonProject = JsonUtil.readJsonFile(filePath, JsonSerializableProject.class);
        if (!jsonProject.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonProject.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveProject(ReadOnlyProject project) throws IOException {
        saveProject(project, filePath);
    }

    /**
     * Similar to {@link #saveProject(ReadOnlyProject)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    public void saveProject(ReadOnlyProject project, Path filePath) throws IOException {
        requireNonNull(project);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableProject(project), filePath);
    }

    public void backupProject(ReadOnlyProject project) throws IOException {
        saveProject(project, backupFilePath);
    }

}
