package seedu.project.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.project.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.project.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.project.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.project.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.project.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.project.commons.core.index.Index;
import seedu.project.logic.LogicManager;
import seedu.project.logic.commands.EditCommand;
import seedu.project.logic.commands.EditCommand.EditProjectDescriptor;
import seedu.project.logic.commands.EditCommand.EditTaskDescriptor;
import seedu.project.logic.parser.exceptions.ParseException;
import seedu.project.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        if (!LogicManager.getState()) {
            requireNonNull(args);
            ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME);

            Index index;

            try {
                index = ParserUtil.parseIndex(argMultimap.getPreamble());
            } catch (ParseException pe) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        EditCommand.PROJECT_MESSAGE_USAGE), pe);
            }

            EditProjectDescriptor editProjectDescriptor = new EditProjectDescriptor();
            if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
                editProjectDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
            }

            if (!editProjectDescriptor.isAnyFieldEdited()) {
                throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
            }
            return new EditCommand(index, editProjectDescriptor);
        } else {
            requireNonNull(args);
            ArgumentMultimap argMultimap =
                    ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_DESCRIPTION, PREFIX_DEADLINE, PREFIX_TAG);

            Index index;

            try {
                index = ParserUtil.parseIndex(argMultimap.getPreamble());
            } catch (ParseException pe) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        EditCommand.TASK_MESSAGE_USAGE), pe);
            }

            EditTaskDescriptor editTaskDescriptor = new EditTaskDescriptor();
            if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
                editTaskDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
            }
            if (argMultimap.getValue(PREFIX_DESCRIPTION).isPresent()) {
                editTaskDescriptor.setDescription(ParserUtil.parseDescription(argMultimap.getValue(PREFIX_DESCRIPTION)
                        .get()));
            }
            if (argMultimap.getValue(PREFIX_DEADLINE).isPresent()) {
                editTaskDescriptor.setDeadline(ParserUtil.parseDeadline(argMultimap.getValue(PREFIX_DEADLINE).get()));
            }
            parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editTaskDescriptor::setTags);

            if (!editTaskDescriptor.isAnyFieldEdited()) {
                throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
            }

            return new EditCommand(index, editTaskDescriptor);
        }
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

}
