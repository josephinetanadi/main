package seedu.project.logic.parser;

import static seedu.project.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.project.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.project.logic.commands.AddCommand;
import seedu.project.logic.commands.AddTagCommand;
import seedu.project.logic.commands.ClearCommand;
import seedu.project.logic.commands.Command;
import seedu.project.logic.commands.CompareCommand;
import seedu.project.logic.commands.CompletedCommand;
import seedu.project.logic.commands.DefineTagCommand;
import seedu.project.logic.commands.DeleteCommand;
import seedu.project.logic.commands.EditCommand;
import seedu.project.logic.commands.ExitCommand;
import seedu.project.logic.commands.ExportCommand;
import seedu.project.logic.commands.FindCommand;
import seedu.project.logic.commands.HelpCommand;
import seedu.project.logic.commands.HistoryCommand;
import seedu.project.logic.commands.ImportCommand;
import seedu.project.logic.commands.ListCommand;
import seedu.project.logic.commands.ListProjectCommand;
import seedu.project.logic.commands.ListTagCommand;
import seedu.project.logic.commands.RedoCommand;
import seedu.project.logic.commands.SelectCommand;
import seedu.project.logic.commands.TaskHistoryCommand;
import seedu.project.logic.commands.UndoCommand;
import seedu.project.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class ProjectParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
            return new AddCommandParser().parse(arguments);

        case AddCommand.COMMAND_ALIAS:
            return new AddCommandParser().parse(arguments);

        case CompareCommand.COMMAND_WORD:
            return new CompareCommandParser().parse(arguments);

        case CompareCommand.COMMAND_ALIAS:
            return new CompareCommandParser().parse(arguments);

        case CompletedCommand.COMMAND_WORD:
            return new CompletedCommandParser().parse(arguments);

        case CompletedCommand.COMMAND_ALIAS:
            return new CompletedCommandParser().parse(arguments);

        case DefineTagCommand.COMMAND_WORD:
            return new DefineTagCommandParser().parse(arguments);

        case AddTagCommand.COMMAND_WORD:
            return new AddTagCommandParser().parse(arguments);

        case TaskHistoryCommand.COMMAND_WORD:
            return new TaskHistoryCommandParser().parse(arguments);

        case TaskHistoryCommand.COMMAND_ALIAS:
            return new TaskHistoryCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case EditCommand.COMMAND_ALIAS:
            return new EditCommandParser().parse(arguments);

        case SelectCommand.COMMAND_WORD:
            return new SelectCommandParser().parse(arguments);

        case SelectCommand.COMMAND_ALIAS:
            return new SelectCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_ALIAS:
            return new DeleteCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case ClearCommand.COMMAND_ALIAS:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments);

        case FindCommand.COMMAND_ALIAS:
            return new FindCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case ListCommand.COMMAND_ALIAS:
            return new ListCommand();

        case ListTagCommand.COMMAND_WORD:
            return new ListTagCommand();

        case ListProjectCommand.COMMAND_WORD:
            return new ListProjectCommand();

        case ListProjectCommand.COMMAND_ALIAS:
            return new ListProjectCommand();

        case HistoryCommand.COMMAND_WORD:
            return new HistoryCommand();

        case HistoryCommand.COMMAND_ALIAS:
            return new HistoryCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case UndoCommand.COMMAND_WORD:
            return new UndoCommand();

        case UndoCommand.COMMAND_ALIAS:
            return new UndoCommand();

        case RedoCommand.COMMAND_WORD:
            return new RedoCommand();

        case RedoCommand.COMMAND_ALIAS:
            return new RedoCommand();

        case ImportCommand.COMMAND_WORD:
            return new ImportCommandParser().parse(arguments);

        case ImportCommand.COMMAND_ALIAS:
            return new ImportCommandParser().parse(arguments);

        case ExportCommand.COMMAND_WORD:
            return new ExportCommandParser().parse(arguments);

        case ExportCommand.COMMAND_ALIAS:
            return new ExportCommandParser().parse(arguments);

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
