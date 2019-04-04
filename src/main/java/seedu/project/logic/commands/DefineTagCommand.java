package seedu.project.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.project.logic.parser.CliSyntax.PREFIX_GROUPTAG;
import static seedu.project.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.project.logic.CommandHistory;
import seedu.project.logic.commands.exceptions.CommandException;
import seedu.project.model.Model;
import seedu.project.model.tag.GroupTag;

/**
 * Allow users to define a group tag.
 */
public class DefineTagCommand extends Command {
    public static final String COMMAND_ALIAS = "dt";
    public static final String COMMAND_WORD = "definetag";
    public static final String SUCCESS_MESSAGE = "Group tag created: %1$s";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Allows users to define tag parent and its child tags\n"
            + "Parameters: " + PREFIX_GROUPTAG + "GROUPTAG " + PREFIX_TAG + "TAG\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_GROUPTAG + "Graded "
            + PREFIX_TAG + "HighPriority " + PREFIX_TAG + "ToCheckWithOthers";
    public static final String MESSAGE_DUPLICATE_GROUPTAG = "This group tag already exists in the group tag list";

    private final GroupTag toAdd;

    public DefineTagCommand(GroupTag groupTag) {
        requireNonNull(groupTag);
        toAdd = groupTag;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        if (model.hasGroupTag(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_GROUPTAG);
        }

        model.addGroupTag(toAdd);
        model.commitProjectList();
        return new CommandResult(String.format(SUCCESS_MESSAGE, toAdd.getName().toString()));
    }
}
