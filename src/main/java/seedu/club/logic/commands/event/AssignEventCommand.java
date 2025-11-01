package seedu.club.logic.commands.event;

import static java.util.Objects.requireNonNull;
import static seedu.club.logic.parser.CliSyntax.PREFIX_EVENT;
import static seedu.club.logic.parser.CliSyntax.PREFIX_MEMBER;
import static seedu.club.logic.parser.CliSyntax.PREFIX_ROLE;

import java.util.HashSet;
import java.util.Set;

import seedu.club.commons.util.ToStringBuilder;
import seedu.club.logic.Messages;
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
 * Assigns a member to an event in club book.
 */
public class AssignEventCommand extends Command {
    public static final String COMMAND_WORD = "assignEvent";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Assigns a member to an event with any specified role(s). "
            + "Parameters: "
            + PREFIX_EVENT + "EVENT "
            + PREFIX_MEMBER + "MEMBER "
            + "[" + PREFIX_ROLE + "EVENTROLE]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_EVENT + "Movie Night "
            + PREFIX_MEMBER + "John Doe "
            + PREFIX_ROLE + "FoodIC ";

    public static final String MESSAGE_SUCCESS = "Assigned member to event: %1$s";
    public static final String MESSAGE_DUPLICATE_MEMBER = "This member already exists in the event";

    private final Name eventName;
    private final Name memberName;
    private final Set<EventRole> roles;

    /**
     * Creates an AssignEventCommand to add the specified {@code Member} to {@code Event}
     */
    public AssignEventCommand(Name event, Name member, Set<EventRole> roles) {
        requireNonNull(event);
        this.eventName = event;
        this.memberName = member;
        this.roles = roles;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // Check if event exists
        int eventIndex = model.eventNameIndex(eventName);
        if (eventIndex == -1) {
            throw new CommandException(String.format(Messages.MESSAGE_EVENT_NAME_NOT_EXIST, eventName));
        }
        Event event = model.getFullEventList().get(eventIndex);

        // Check if role exists, no role specified vacuously passes
        Set<EventRole> missingRoles = new HashSet<>(roles);
        missingRoles.removeAll(event.getRoles());
        if (!missingRoles.isEmpty()) {
            throw new CommandException(String.format(
                    Messages.MESSAGE_EVENTROLE_NAME_NOT_EXIST, event.getName(), missingRoles));
        }

        // Check if member exists
        int memberIndex = model.memberNameIndex(memberName);
        if (memberIndex == -1) {
            throw new CommandException(String.format(Messages.MESSAGE_MEMBER_NAME_NOT_EXIST, memberName));
        }
        Member member = model.getFullMemberList().get(memberIndex);

        // Check if member already exists
        if (event.hasMember(member)) {
            throw new CommandException(MESSAGE_DUPLICATE_MEMBER);
        }

        Set<EventRole> rolesToAssign = new HashSet<>();
        // Replace user-supplied roles with event's actual role objects
        for (EventRole roleFromUser : roles) {
            for (EventRole roleFromEvent : event.getRoles()) {
                if (roleFromEvent.equals(roleFromUser)) { // uses equalsIgnoreCase
                    rolesToAssign.add(roleFromEvent);
                    break;
                }
            }
        }

        // Handle no role provided, roles is empty so add an "unassigned" role
        if (rolesToAssign.isEmpty()) {
            EventRole unassignedRole = new EventRole(event.getName());
            rolesToAssign.add(unassignedRole);
        }

        member.addEventRoles(rolesToAssign);
        event.addMember(member);

        model.setViewState(ViewState.SINGLE_EVENT);
        model.updateFilteredEventList(e -> e.equals(event));
        model.updateFilteredMemberList(m -> event.getRoster().contains(m));

        return new CommandResult(Messages.formatAssignRole(member, event, rolesToAssign));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AssignEventCommand)) {
            return false;
        }

        AssignEventCommand otherAssignEventCommand = (AssignEventCommand) other;
        return eventName.equals(otherAssignEventCommand.eventName)
                && memberName.equals(otherAssignEventCommand.memberName)
                && roles.equals(otherAssignEventCommand.roles);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("event", eventName)
                .add("member", memberName)
                .add("roles", roles)
                .toString();
    }
}
