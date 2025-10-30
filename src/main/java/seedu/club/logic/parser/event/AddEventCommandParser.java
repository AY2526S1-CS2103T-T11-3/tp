package seedu.club.logic.parser.event;

import static seedu.club.logic.Messages.MESSAGE_END_BEFORE_START_DATE;
import static seedu.club.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.club.logic.parser.CliSyntax.PREFIX_DETAIL;
import static seedu.club.logic.parser.CliSyntax.PREFIX_FROM;
import static seedu.club.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.club.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.club.logic.parser.CliSyntax.PREFIX_TO;

import java.util.Collections;
import java.util.Set;

import seedu.club.logic.commands.event.AddEventCommand;
import seedu.club.logic.parser.ArgumentMultimap;
import seedu.club.logic.parser.ArgumentTokenizer;
import seedu.club.logic.parser.Parser;
import seedu.club.logic.parser.ParserUtil;
import seedu.club.logic.parser.exceptions.ParseException;
import seedu.club.model.event.DateTime;
import seedu.club.model.event.Event;
import seedu.club.model.name.Name;
import seedu.club.model.role.EventRole;

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

        if (!argMultimap.arePrefixesPresent(PREFIX_NAME, PREFIX_FROM, PREFIX_TO)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_FROM, PREFIX_TO, PREFIX_DETAIL);
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        DateTime from = ParserUtil.parseDateTime(argMultimap.getValue(PREFIX_FROM).get());
        DateTime to = ParserUtil.parseDateTime(argMultimap.getValue(PREFIX_TO).get());
        if (!from.isBefore(to)) {
            throw new ParseException(String.format(MESSAGE_END_BEFORE_START_DATE));
        }

        String detail = argMultimap.getValue(PREFIX_DETAIL).isPresent()
                        ? ParserUtil.parseDetail(argMultimap.getValue(PREFIX_DETAIL).get())
                        : "";
        Set<EventRole> eventRoleList = !argMultimap.getAllValues(PREFIX_ROLE).isEmpty()
                            ? ParserUtil.parseEventRoles(argMultimap.getAllValues(PREFIX_ROLE))
                            : Collections.emptySet();

        Event event = new Event(name, from, to, detail, eventRoleList);

        return new AddEventCommand(event);
    }
}
