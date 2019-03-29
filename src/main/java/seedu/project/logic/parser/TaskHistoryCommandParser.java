package seedu.project.logic.parser;

import static seedu.project.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.project.commons.core.index.Index;
import seedu.project.logic.commands.CompareCommand;
import seedu.project.logic.commands.TaskHistoryCommand;
import seedu.project.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new TaskHistoryCommand object
 */
public class TaskHistoryCommandParser implements Parser<TaskHistoryCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the TaskHistoryCommand
     * and returns an TaskHistoryCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public TaskHistoryCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new TaskHistoryCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, CompareCommand.MESSAGE_USAGE), pe);
        }
    }

}
