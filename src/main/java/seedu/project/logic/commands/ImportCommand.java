package seedu.project.logic.commands;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.util.Optional;

import seedu.project.commons.core.Messages;
import seedu.project.commons.exceptions.DataConversionException;
import seedu.project.commons.exceptions.IllegalValueException;
import seedu.project.commons.util.JsonUtil;
import seedu.project.logic.CommandHistory;
import seedu.project.logic.LogicManager;
import seedu.project.logic.commands.exceptions.CommandException;
import seedu.project.model.Model;
import seedu.project.model.ReadOnlyProjectList;
import seedu.project.model.project.Project;
import seedu.project.storage.JsonSerializableProjectList;

/**
 * Import project list to add on to current list
 */
public class ImportCommand extends Command {

    public static final String COMMAND_WORD = "import";
    public static final String COMMAND_ALIAS = "i";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Import a JSON file containing to the project list. "
            + "Parameters: PATH " + "Example: " + COMMAND_WORD + " " + "C:\\Users\\Documents\\project.json";

    public static final String MESSAGE_SUCCESS_PROJECT = "New project added: %1$s";
    public static final String MESSAGE_DUPLICATE_PROJECT = "This project already exists in the project list";

    private final Path toAdd;

    /**
     * Creates an ImportCommand to add the specified {@code ProjectList}
     */
    public ImportCommand(Path file) {
        requireNonNull(file);
        toAdd = file;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException, DataConversionException {
        requireNonNull(model);
        Optional<ReadOnlyProjectList> projectListToAdd = readProjectList();
        for (Project project : projectListToAdd.get().getProjectList()) {
            if (model.hasProject(project)) {
                throw new CommandException(MESSAGE_DUPLICATE_PROJECT);
            }
            model.addProject(project);
        }
        model.commitProjectList();
        return new CommandResult(String.format(MESSAGE_SUCCESS_PROJECT,
                projectListToAdd.get().getProjectList().size()));
    }

    /**
     * Read JSON project list
     */
    public Optional<ReadOnlyProjectList> readProjectList() throws DataConversionException {

        Optional<JsonSerializableProjectList> jsonProjectList = JsonUtil.readJsonFile(toAdd,
                JsonSerializableProjectList.class);
        if (!jsonProjectList.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonProjectList.get().toModelType());
        } catch (IllegalValueException ive) {
            throw new DataConversionException(ive);
        }
    }
}
