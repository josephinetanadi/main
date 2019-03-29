package seedu.project.logic.parser;

import static seedu.project.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.project.logic.parser.CliSyntax.PREFIX_GROUPTAG;
import static seedu.project.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;
import java.util.stream.Stream;

import seedu.project.logic.commands.DefineTagCommand;
import seedu.project.logic.parser.exceptions.ParseException;
import seedu.project.model.Name;
import seedu.project.model.tag.GroupTag;
import seedu.project.model.tag.Tag;

public class DefineTagCommandParser implements Parser<DefineTagCommand>{
    public DefineTagCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_GROUPTAG, PREFIX_TAG);

        if (!arePrefixesPresent(argMultimap, PREFIX_GROUPTAG, PREFIX_TAG)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DefineTagCommand.MESSAGE_USAGE));
        }

        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_GROUPTAG).get());
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
        GroupTag groupTag = new GroupTag(name, tagList);

        return new DefineTagCommand(groupTag);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }


}
