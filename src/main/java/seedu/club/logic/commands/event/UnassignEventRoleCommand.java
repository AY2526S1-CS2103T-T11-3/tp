package seedu.club.logic.commands.event;

import static seedu.club.logic.Messages.MESSAGE_EVENTROLE_NAME_NOT_EXIST;
import static seedu.club.logic.Messages.MESSAGE_EVENT_NAME_NOT_EXIST;
import static seedu.club.logic.Messages.MESSAGE_MEMBER_NAME_NOT_EXIST;
import static seedu.club.logic.parser.CliSyntax.PREFIX_EVENT;
import static seedu.club.logic.parser.CliSyntax.PREFIX_MEMBER;
import static seedu.club.logic.parser.CliSyntax.PREFIX_ROLE;

import java.util.Set;

import seedu.club.logic.commands.Command;
import seedu.club.logic.commands.CommandResult;
import seedu.club.logic.commands.exceptions.CommandException;
import seedu.club.model.Model;
import seedu.club.model.ViewState;
import seedu.club.model.event.Event;
import seedu.club.model.member.Member;
import seedu.club.model.name.Name;
import seedu.club.model.role.EventRole;

/**
 * Removes an event role from a member.
 */
public class UnassignEventRoleCommand extends Command {
    public static final String COMMAND_WORD = "unassignEventRole";
    public static final String MESSAGE_SUCCESS_EVENT_ROLE =
            "The event role(s) has/have been unassigned from the member successfully.";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Removes the event role(s) from the specified member\n"
            + "Parameters: "
            + PREFIX_EVENT + "EVENT "
            + PREFIX_MEMBER + "MEMBER "
            + PREFIX_ROLE + "EVENT ROLE...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_EVENT + "Meeting "
            + PREFIX_MEMBER + "John Doe "
            + PREFIX_ROLE + "Facilitator";

    public static final String MESSAGE_ROLE_DOES_NOT_EXIST_IN_MEMBER =
            "The specified member does not have that role\n";


    private final Name eventName;
    private final Name memberName;

    private final Set<EventRole> roles;


    /**
     * Creates an UnassignEventRoleCommand for the specified event, member, and roles.
     */
    public UnassignEventRoleCommand(Name event, Name member, Set<EventRole> roles) {
        this.eventName = event;
        this.memberName = member;
        this.roles = roles;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UnassignEventRoleCommand compare)) {
            return false;
        }
        return (this.eventName.equals(compare.eventName)) && (this.memberName.equals(compare.memberName))
                && (this.roles.equals(compare.roles));
    }


    @Override
    public CommandResult execute(Model model) throws CommandException {
        int eventIndex = model.eventNameIndex(eventName);
        int memberIndex = model.memberNameIndex(memberName);
        if (eventIndex == -1) {
            throw new CommandException(String.format(MESSAGE_EVENT_NAME_NOT_EXIST, eventName));
        }
        if (memberIndex == -1) {
            throw new CommandException(String.format(MESSAGE_MEMBER_NAME_NOT_EXIST, memberName));
        }
        Member member = model.getFullMemberList().get(memberIndex);
        Event event = model.getFullEventList().get(eventIndex);

        if (!event.getRoles().containsAll(roles)) {
            throw new CommandException(String.format(MESSAGE_EVENTROLE_NAME_NOT_EXIST,
                    event.getName(), roles));
        }

        if (!member.getEventRoles().containsAll(roles)) {
            throw new CommandException(String.format(MESSAGE_ROLE_DOES_NOT_EXIST_IN_MEMBER,
                    event.getName(), roles));
        }


        member.removeEventRole(roles);
        model.updateFilteredEventList(e -> e.equals(event));
        model.updateFilteredMemberList(m -> true);
        model.updateFilteredMemberList(m -> event.getRoster().contains(m));
        model.setViewState(ViewState.SINGLE_EVENT);
        return new CommandResult(String.format(MESSAGE_SUCCESS_EVENT_ROLE),
                false, false);
    }
}
