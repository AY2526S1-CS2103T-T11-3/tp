package seedu.club.logic.parser;

import static seedu.club.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.club.logic.parser.CliSyntax.PREFIX_DETAIL;
import static seedu.club.logic.parser.CliSyntax.PREFIX_FROM;
import static seedu.club.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.club.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.club.logic.parser.CliSyntax.PREFIX_TO;

import java.util.Collections;
import java.util.Set;

import seedu.club.logic.commands.AddEventCommand;
import seedu.club.logic.parser.exceptions.ParseException;
import seedu.club.model.event.Event;
import seedu.club.model.name.Name;
import seedu.club.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddEventCommand object
 */
public class AddEventCommandParser implements Parser<AddEventCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the AddEventCommand
     * and returns an AddEventCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddEventCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_FROM, PREFIX_TO, PREFIX_DETAIL, PREFIX_ROLE);

        if (!argMultimap.arePrefixesPresent(PREFIX_NAME, PREFIX_FROM)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_FROM, PREFIX_TO, PREFIX_DETAIL);
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        String from = ParserUtil.parseDate(argMultimap.getValue(PREFIX_FROM).get());
        String to = argMultimap.getValue(PREFIX_TO).isPresent()
                    ? ParserUtil.parseDate(argMultimap.getValue(PREFIX_TO).get())
                    : "";
        String detail = argMultimap.getValue(PREFIX_DETAIL).isPresent()
                        ? ParserUtil.parseDetail(argMultimap.getValue(PREFIX_DETAIL).get())
                        : "";
        Set<Tag> roleList = !argMultimap.getAllValues(PREFIX_ROLE).isEmpty()
                            ? ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_ROLE))
                            : Collections.emptySet();

        Event event = new Event(name, from, to, detail, roleList);

        return new AddEventCommand(event);
    }
}
