package seedu.project.logic;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableList;
import seedu.project.commons.core.GuiSettings;
import seedu.project.commons.core.LogsCenter;
import seedu.project.logic.commands.Command;
import seedu.project.logic.commands.CommandResult;
import seedu.project.logic.commands.exceptions.CommandException;
import seedu.project.logic.parser.ProjectParser;
import seedu.project.logic.parser.exceptions.ParseException;
import seedu.project.model.Model;
import seedu.project.model.ReadOnlyProject;
import seedu.project.model.task.Task;
import seedu.project.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_MESSAGE = "Could not save data to file: ";
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final CommandHistory history;
    private final ProjectParser projectParser;
    private boolean projectModified;

    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        history = new CommandHistory();
        projectParser = new ProjectParser();

        // Set projectModified to true whenever the models' address book is modified.
        model.getProject().addListener(observable -> projectModified = true);
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        projectModified = false;

        CommandResult commandResult;
        try {
            Command command = projectParser.parseCommand(commandText);
            commandResult = command.execute(model, history);
        } finally {
            history.add(commandText);
        }

        if (projectModified) {
            logger.info("Address book modified, saving to file.");
            try {
                storage.saveProject(model.getProject());
            } catch (IOException ioe) {
                throw new CommandException(FILE_OPS_ERROR_MESSAGE + ioe, ioe);
            }
        }

        return commandResult;
    }

    @Override
    public ReadOnlyProject getProject() {
        return model.getProject();
    }

    @Override
    public ObservableList<Task> getFilteredTaskList() {
        return model.getFilteredTaskList();
    }

    @Override
    public ObservableList<String> getHistory() {
        return history.getHistory();
    }

    @Override
    public Path getProjectFilePath() {
        return model.getProjectFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }

    @Override
    public ReadOnlyProperty<Task> selectedTaskProperty() {
        return model.selectedTaskProperty();
    }

    @Override
    public void setSelectedTask(Task task) {
        model.setSelectedTask(task);
    }
}
