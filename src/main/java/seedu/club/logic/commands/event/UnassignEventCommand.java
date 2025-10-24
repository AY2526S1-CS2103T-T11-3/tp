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
 * Removes assignation between a member and an event or event role.
 */
public class UnassignEventCommand extends Command {
    public static final String COMMAND_WORD = "unassignEvent";
    public static final String MESSAGE_SUCCESS_EVENT = "The event has been unassigned from the member successfully.";
    public static final String MESSAGE_SUCCESS_EVENT_ROLE =
            "The event role has been unassigned from the member successfully.";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Removes the event roles from the specified member\n"
            + "If no roles are specified, the member will be removed from the event\n"
            + "Parameters: "
            + PREFIX_MEMBER + "MEMBER "
            + PREFIX_EVENT + "EVENT "
            + "[" + PREFIX_ROLE + "EVENT ROLE]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_EVENT + "Meeting "
            + PREFIX_MEMBER + "John "
            + PREFIX_ROLE + "Facilitator";
    public static final String MESSAGE_NAME_DOES_NOT_EXIST = "The event or member does not exist.\n";

    private final Name eventName;
    private final Name memberName;

    private final Set<EventRole> roles;

    private boolean hasRoles = true;

    /**
     * Creates an UnassignEventCommand for the specified event, member, and roles.
     */
    public UnassignEventCommand(Name event, Name member, Set<EventRole> roles) {
        this.eventName = event;
        this.memberName = member;
        this.roles = roles;
        if (this.roles.isEmpty()) {
            hasRoles = false;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UnassignEventCommand compare)) {
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

        if (hasRoles) {
            return executeWithEventRole(member, roles, model, event);
        }

        return executeNoEventRole(member, event, model);
    }

    private CommandResult executeWithEventRole(Member member, Set<EventRole> eventRoles,
                                                Model model, Event event) throws CommandException {
        if (!member.getEventRoles().containsAll(eventRoles)) {
            throw new CommandException(String.format(MESSAGE_EVENTROLE_NAME_NOT_EXIST, eventRoles.toString()));
        }
        member.removeEventRole(eventRoles);
        model.updateFilteredEventList(e -> e.equals(event));
        model.updateFilteredMemberList(m -> event.getRoster().contains(m));
        model.setViewState(ViewState.MEMBER);
        return new CommandResult(String.format(MESSAGE_SUCCESS_EVENT_ROLE),
                false, false);
    }

    private CommandResult executeNoEventRole(Member member, Event event,
                                              Model model) {
        member.removeEvent(event);
        event.removeMemberFromRoster(member);
        model.updateFilteredEventList(e -> e.equals(event));
        model.updateFilteredMemberList(m -> event.getRoster().contains(m));
        model.setViewState(ViewState.EVENT);
        return new CommandResult(String.format(MESSAGE_SUCCESS_EVENT),
                false, false);
    }
}
