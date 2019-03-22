package seedu.project.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.project.logic.commands.ImportCommand.MESSAGE_DUPLICATE_PROJECT;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import seedu.project.commons.core.Messages;
import seedu.project.commons.core.index.Index;
import seedu.project.commons.util.FileUtil;
import seedu.project.commons.util.JsonUtil;
import seedu.project.logic.CommandHistory;
import seedu.project.logic.commands.exceptions.CommandException;
import seedu.project.model.Model;
import seedu.project.model.ProjectList;
import seedu.project.model.project.Project;
import seedu.project.storage.JsonSerializableProjectList;

/**
 * Exports projects to a specified destination
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";
    public static final String COMMAND_ALIAS = "ex";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Export projects specified by index to JSON file. "
            + "Parameters: INDEX " + "Example: " + COMMAND_WORD + " " + "1,3,4";

    public static final String MESSAGE_SUCCESS_PROJECT = "Project exported: %1$s";

    private final Set<Index> projectIdx;
    private final ProjectList projectsToExport = new ProjectList();
    private final Path toWrite;

    /**
     * Creates an ImportCommand to add the specified {@code ProjectList}
     */
    public ExportCommand(Set<Index> projectIdx, Path toWrite) {
        requireNonNull(projectIdx);
        this.projectIdx = projectIdx;
        this.toWrite = toWrite;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException, IOException {
        requireNonNull(model);
        List<Project> lastShownList = model.getFilteredProjectList();

        for (Index index : projectIdx) {
            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PROJECT_DISPLAYED_INDEX);
            }

            Project project = lastShownList.get(index.getZeroBased());

            if (projectsToExport.hasProject(project)) {
                throw new CommandException(MESSAGE_DUPLICATE_PROJECT);
            }

            projectsToExport.addProject(project);
        }
        saveProjectList();
        return new CommandResult(String.format(MESSAGE_SUCCESS_PROJECT, projectIdx.size()));
    }

    /**
     * Saves project list to JSON file
     */
    public void saveProjectList() throws IOException {
        FileUtil.createIfMissing(toWrite);
        JsonUtil.saveJsonFile(new JsonSerializableProjectList(projectsToExport), toWrite);
    }
}
