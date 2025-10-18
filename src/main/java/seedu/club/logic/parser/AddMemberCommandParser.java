package seedu.club.logic.parser;

import static seedu.club.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.club.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.club.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.club.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.club.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;

import seedu.club.logic.commands.AddMemberCommand;
import seedu.club.logic.parser.exceptions.ParseException;
import seedu.club.model.member.Email;
import seedu.club.model.member.Member;
import seedu.club.model.member.Phone;
import seedu.club.model.name.Name;
import seedu.club.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddMemberCommand object
 */
public class AddMemberCommandParser implements Parser<AddMemberCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddMemberCommand
     * and returns an AddMemberCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddMemberCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_TAG);

        if (!argMultimap.arePrefixesPresent(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddMemberCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL);
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        Member member = new Member(name, phone, email, tagList);

        return new AddMemberCommand(member);
    }
}
