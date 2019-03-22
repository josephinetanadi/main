package seedu.project.model;

import java.nio.file.Path;

import seedu.project.commons.core.GuiSettings;

/**
 * Unmodifiable view of user prefs.
 */
public interface ReadOnlyUserPrefs {

    GuiSettings getGuiSettings();

    Path getProjectListFilePath();

}
