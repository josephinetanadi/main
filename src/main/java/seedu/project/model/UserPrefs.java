package seedu.project.model;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import seedu.project.commons.core.GuiSettings;

/**
 * Represents User's preferences.
 */
public class UserPrefs implements ReadOnlyUserPrefs {

    private GuiSettings guiSettings = new GuiSettings();
    private Path projectListFilePath = Paths.get("data", "projectlist.json");
    private Path projectFilePath = Paths.get("data", "project.json");

    /**
     * Creates a {@code UserPrefs} with default values.
     */
    public UserPrefs() {
    }

    /**
     * Creates a {@code UserPrefs} with the prefs in {@code userPrefs}.
     */
    public UserPrefs(ReadOnlyUserPrefs userPrefs) {
        this();
        resetData(userPrefs);
    }

    /**
     * Resets the existing data of this {@code UserPrefs} with {@code newUserPrefs}.
     */
    public void resetData(ReadOnlyUserPrefs newUserPrefs) {
        requireNonNull(newUserPrefs);
        setGuiSettings(newUserPrefs.getGuiSettings());
        setProjectListFilePath(newUserPrefs.getProjectListFilePath());
        setProjectFilePath(newUserPrefs.getProjectFilePath());
    }

    public GuiSettings getGuiSettings() {
        return guiSettings;
    }

    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        this.guiSettings = guiSettings;
    }

    public Path getProjectListFilePath() {
        return projectListFilePath;
    }

    public void setProjectListFilePath(Path projectListFilePath) {
        requireNonNull(projectListFilePath);
        this.projectListFilePath = projectListFilePath;
    }

    public Path getProjectFilePath() {
        return projectFilePath;
    }

    public void setProjectFilePath(Path projectFilePath) {
        requireNonNull(projectFilePath);
        this.projectFilePath = projectFilePath;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UserPrefs)) { // this handles null as well.
            return false;
        }

        UserPrefs o = (UserPrefs) other;

        return guiSettings.equals(o.guiSettings) && projectListFilePath.equals(o.projectListFilePath)
                && projectFilePath.equals(o.projectFilePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guiSettings, projectListFilePath, projectFilePath);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gui Settings : " + guiSettings);
        sb.append("\nProject list file location: " + projectListFilePath);
        sb.append("\nProject data file location : " + projectFilePath);
        return sb.toString();
    }

}
