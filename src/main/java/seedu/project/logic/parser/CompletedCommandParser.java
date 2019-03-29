package seedu.project.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.project.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.InputMismatchException;

import seedu.project.commons.core.index.Index;
import seedu.project.logic.commands.CompletedCommand;
import seedu.project.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new CompletedCommand object
 */
public class CompletedCommandParser implements Parser<CompletedCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the CompletedCommand
     * and returns an CompletedCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public CompletedCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap;
        try {
            argMultimap = ArgumentTokenizer.tokenize(args);
        } catch (InputMismatchException ime) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    CompletedCommand.MESSAGE_USAGE), ime);
        }

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    CompletedCommand.MESSAGE_USAGE), pe);
        }

        return new CompletedCommand(index);
    }
}
