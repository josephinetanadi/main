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
import seedu.project.model.ReadOnlyProjectList;

/**
 * A class to access Project data stored as a json file on the hard disk.
 */
public class JsonProjectListStorage implements ProjectListStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonProjectListStorage.class);

    private Path filePath;
    private Path backupFilePath;

    public JsonProjectListStorage(Path filePath) {
        this.filePath = filePath;
        backupFilePath = Paths.get(filePath.toString() + ".backup");
    }

    public Path getProjectListFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyProjectList> readProjectList() throws DataConversionException {
        return readProjectList(filePath);
    }

    /**
     * Similar to {@link #readProjectList()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyProjectList> readProjectList(Path filePath) throws DataConversionException {
        requireNonNull(filePath);

        Optional<JsonSerializableProjectList> jsonProjectList = JsonUtil.readJsonFile(filePath, JsonSerializableProjectList.class);
        if (!jsonProjectList.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonProjectList.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveProjectList(ReadOnlyProjectList projectList) throws IOException {
        saveProjectList(projectList, filePath);
    }

    /**
     * Similar to {@link #saveProjectList(ReadOnlyProjectList)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    public void saveProjectList(ReadOnlyProjectList projectList, Path filePath) throws IOException {
        requireNonNull(projectList);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableProjectList(projectList), filePath);
    }

    public void backupProjectList(ReadOnlyProjectList projectList) throws IOException {
        saveProjectList(projectList, backupFilePath);
    }

}
