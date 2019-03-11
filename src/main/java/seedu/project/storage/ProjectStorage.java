package seedu.project.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.project.commons.exceptions.DataConversionException;
import seedu.project.model.project.ReadOnlyProject;
import seedu.project.model.project.Project;

/**
 * Represents a storage for {@link Project}.
 */
public interface ProjectStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getProjectFilePath();

    /**
     * Returns Project data as a {@link ReadOnlyProject}. Returns
     * {@code Optional.empty()} if storage file is not found.
     *
     * @throws DataConversionException if the data in storage is not in the expected
     *                                 format.
     * @throws IOException             if there was any problem when reading from
     *                                 the storage.
     */
    Optional<ReadOnlyProject> readProject() throws DataConversionException, IOException;

    /**
     * @see #getProjectFilePath()
     */
    Optional<ReadOnlyProject> readProject(Path filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyProject} to the storage.
     *
     * @param project cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveProject(ReadOnlyProject project) throws IOException;

    /**
     * @see #saveProject(ReadOnlyProject)
     */
    void saveProject(ReadOnlyProject project, Path filePath) throws IOException;

    void backupProject(ReadOnlyProject project) throws IOException;
}
