package seedu.project.logic.parser;

import static seedu.project.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.project.logic.parser.CliSyntax.PREFIX_INDEX;
import static seedu.project.logic.parser.CliSyntax.PREFIX_OUTPUT;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import seedu.project.commons.core.index.Index;
import seedu.project.logic.commands.ExportCommand;
import seedu.project.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class ExportCommandParser implements Parser<ExportCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ExportCommand
     * and returns an ExportCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ExportCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_INDEX, PREFIX_OUTPUT);

        if (!arePrefixesPresent(argMultimap, PREFIX_INDEX, PREFIX_OUTPUT) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
        }

        Path dst = Paths.get(argMultimap.getValue(PREFIX_OUTPUT).get());
        String trimmedArgs = argMultimap.getValue(PREFIX_INDEX).get().trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
        }

        String[] arg = trimmedArgs.split(",");
        Set<Index> projectIdx = new HashSet<>();
        for (String idx : arg) {
            projectIdx.add(ParserUtil.parseIndex(idx));
        }

        return new ExportCommand(projectIdx, dst);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
