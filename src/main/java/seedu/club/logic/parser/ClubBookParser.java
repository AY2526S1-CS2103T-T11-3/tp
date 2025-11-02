package seedu.club.logic.parser;

import static seedu.club.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.club.logic.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.club.commons.core.LogsCenter;
import seedu.club.logic.commands.Command;
import seedu.club.logic.commands.event.AddEventCommand;
import seedu.club.logic.commands.event.AssignEventCommand;
import seedu.club.logic.commands.event.DeleteEventCommand;
import seedu.club.logic.commands.event.DisplayEventCommand;
import seedu.club.logic.commands.event.EditEventCommand;
import seedu.club.logic.commands.event.FindEventCommand;
import seedu.club.logic.commands.event.ListEventCommand;
import seedu.club.logic.commands.event.UnassignEventCommand;
import seedu.club.logic.commands.event.UnassignEventRoleCommand;
import seedu.club.logic.commands.general.ClearCommand;
import seedu.club.logic.commands.general.ExitCommand;
import seedu.club.logic.commands.general.HelpCommand;
import seedu.club.logic.commands.member.AddMemberCommand;
import seedu.club.logic.commands.member.DeleteMemberCommand;
import seedu.club.logic.commands.member.EditMemberCommand;
import seedu.club.logic.commands.member.FindMemberCommand;
import seedu.club.logic.commands.member.ListMemberCommand;
import seedu.club.logic.parser.event.AddEventCommandParser;
import seedu.club.logic.parser.event.AssignEventCommandParser;
import seedu.club.logic.parser.event.DeleteEventCommandParser;
import seedu.club.logic.parser.event.DisplayEventCommandParser;
import seedu.club.logic.parser.event.EditEventCommandParser;
import seedu.club.logic.parser.event.FindEventCommandParser;
import seedu.club.logic.parser.event.UnassignEventCommandParser;
import seedu.club.logic.parser.event.UnassignEventRoleCommandParser;
import seedu.club.logic.parser.exceptions.ParseException;
import seedu.club.logic.parser.member.AddMemberCommandParser;
import seedu.club.logic.parser.member.DeleteMemberCommandParser;
import seedu.club.logic.parser.member.EditMemberCommandParser;
import seedu.club.logic.parser.member.FindMemberCommandParser;


/**
 * Parses user input.
 */
public class ClubBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
    private static final Logger logger = LogsCenter.getLogger(ClubBookParser.class);

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

        // Note to developers: Change the log level in config.json to enable lower level (i.e., FINE, FINER and lower)
        // log messages such as the one below.
        // Lower level log messages are used sparingly to minimize noise in the code.
        logger.fine("Command word: " + commandWord + "; Arguments: " + arguments);

        switch (commandWord.toLowerCase()) {

        case AddMemberCommand.COMMAND_WORD:
            return new AddMemberCommandParser().parse(arguments);

        case DeleteMemberCommand.COMMAND_WORD:
            return new DeleteMemberCommandParser().parse(arguments);

        case EditMemberCommand.COMMAND_WORD:
            return new EditMemberCommandParser().parse(arguments);

        case ListMemberCommand.COMMAND_WORD:
            return new ListMemberCommand();

        case FindMemberCommand.COMMAND_WORD:
            return new FindMemberCommandParser().parse(arguments);

        case AddEventCommand.COMMAND_WORD:
            return new AddEventCommandParser().parse(arguments);

        case DeleteEventCommand.COMMAND_WORD:
            return new DeleteEventCommandParser().parse(arguments);

        case EditEventCommand.COMMAND_WORD:
            return new EditEventCommandParser().parse(arguments);

        case ListEventCommand.COMMAND_WORD:
            return new ListEventCommand();

        case FindEventCommand.COMMAND_WORD:
            return new FindEventCommandParser().parse(arguments);

        case AssignEventCommand.COMMAND_WORD:
            return new AssignEventCommandParser().parse(arguments);

        case DisplayEventCommand.COMMAND_WORD:
            return new DisplayEventCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case UnassignEventCommand.COMMAND_WORD:
            return new UnassignEventCommandParser().parse(arguments);

        case UnassignEventRoleCommand.COMMAND_WORD:
            return new UnassignEventRoleCommandParser().parse(arguments);

        default:
            logger.finer("This user input caused a ParseException: " + userInput);
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
