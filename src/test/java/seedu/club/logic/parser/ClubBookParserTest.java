package seedu.club.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.club.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.club.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.club.testutil.Assert.assertThrows;
import static seedu.club.testutil.TypicalIndexes.INDEX_FIRST_EVENT;
import static seedu.club.testutil.TypicalIndexes.INDEX_FIRST_MEMBER;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.club.logic.commands.AddEventCommand;
import seedu.club.logic.commands.AddMemberCommand;
import seedu.club.logic.commands.ClearCommand;
import seedu.club.logic.commands.DeleteEventCommand;
import seedu.club.logic.commands.DeleteMemberCommand;
import seedu.club.logic.commands.EditCommand;
import seedu.club.logic.commands.EditCommand.EditMemberDescriptor;
import seedu.club.logic.commands.ExitCommand;
import seedu.club.logic.commands.FindCommand;
import seedu.club.logic.commands.HelpCommand;
import seedu.club.logic.commands.ListMemberCommand;
import seedu.club.logic.parser.exceptions.ParseException;
import seedu.club.model.event.Event;
import seedu.club.model.member.Member;
import seedu.club.model.name.NameContainsKeywordsPredicate;
import seedu.club.testutil.EditMemberDescriptorBuilder;
import seedu.club.testutil.EventBuilder;
import seedu.club.testutil.EventUtil;
import seedu.club.testutil.MemberBuilder;
import seedu.club.testutil.MemberUtil;

public class ClubBookParserTest {

    private final ClubBookParser parser = new ClubBookParser();

    @Test
    public void parseCommand_addMember() throws Exception {
        Member member = new MemberBuilder().build();
        AddMemberCommand command = (AddMemberCommand) parser.parseCommand(MemberUtil.getAddMemberCommand(member));
        assertEquals(new AddMemberCommand(member), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_deleteMember() throws Exception {
        DeleteMemberCommand command = (DeleteMemberCommand) parser.parseCommand(
                DeleteMemberCommand.COMMAND_WORD + " " + INDEX_FIRST_MEMBER.getOneBased());
        assertEquals(new DeleteMemberCommand(INDEX_FIRST_MEMBER), command);
    }

    @Test
    public void parseCommand_deleteEvent() throws Exception {
        DeleteEventCommand command = (DeleteEventCommand) parser.parseCommand(
                DeleteEventCommand.COMMAND_WORD + " " + INDEX_FIRST_EVENT.getOneBased());
        assertEquals(new DeleteEventCommand(INDEX_FIRST_EVENT), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Member member = new MemberBuilder().build();
        EditMemberDescriptor descriptor = new EditMemberDescriptorBuilder(member).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_MEMBER.getOneBased() + " " + MemberUtil.getEditMemberDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_MEMBER, descriptor), command);
    }

    @Test
    public void parseCommand_addEvent() throws Exception {
        Event event = new EventBuilder().build();
        AddEventCommand command = (AddEventCommand) parser.parseCommand(EventUtil.getAddEventCommand(event));
        assertEquals(new AddEventCommand(event), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate<>(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListMemberCommand.COMMAND_WORD) instanceof ListMemberCommand);
        assertTrue(parser.parseCommand(ListMemberCommand.COMMAND_WORD + " 3") instanceof ListMemberCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
