package seedu.club.logic.commands.event;

import static seedu.club.logic.Messages.MESSAGE_EVENT_NAME_NOT_EXIST;
import static seedu.club.logic.Messages.MESSAGE_MEMBER_NAME_NOT_EXIST;
import static seedu.club.logic.Messages.MESSAGE_MEMBER_NAME_NOT_IN_EVENT_ROSTER;
import static seedu.club.logic.parser.CliSyntax.PREFIX_EVENT;
import static seedu.club.logic.parser.CliSyntax.PREFIX_MEMBER;

import seedu.club.logic.commands.Command;
import seedu.club.logic.commands.CommandResult;
import seedu.club.logic.commands.exceptions.CommandException;
import seedu.club.model.Model;
import seedu.club.model.ViewState;
import seedu.club.model.event.Event;
import seedu.club.model.member.Member;
import seedu.club.model.name.Name;

/**
 * Removes assignation between a member and an event.
 */
public class UnassignEventCommand extends Command {
    public static final String COMMAND_WORD = "unassignEvent";
    public static final String MESSAGE_SUCCESS_EVENT = "The event has been unassigned from the member successfully.";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Removes the event from the specified member.\n"
            + "If the member has roles under that event, they will be removed from the member's event roles list.\n"
            + "Parameters: "
            + PREFIX_MEMBER + "MEMBER "
            + PREFIX_EVENT + "EVENT\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_EVENT + "Meeting "
            + PREFIX_MEMBER + "John";
    public static final String MESSAGE_NAME_DOES_NOT_EXIST = "The event or member does not exist.\n";

    private final Name eventName;
    private final Name memberName;

    /**
     * Creates an UnassignEventCommand for the specified event, member, and roles.
     */
    public UnassignEventCommand(Name event, Name member) {
        this.eventName = event;
        this.memberName = member;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UnassignEventCommand compare)) {
            return false;
        }
        return (this.eventName.equals(compare.eventName)) && (this.memberName.equals(compare.memberName));
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
        if (!event.getRoster().contains(member)) {
            throw new CommandException(String.format(MESSAGE_MEMBER_NAME_NOT_IN_EVENT_ROSTER,
                    memberName, eventName));
        }


        member.removeEvent(event);
        event.removeMemberFromRoster(member);
        model.updateFilteredEventList(e -> e.equals(event));
        model.updateFilteredMemberList(m -> event.getRoster().contains(m));
        model.setViewState(ViewState.SINGLE_EVENT);
        return new CommandResult(String.format(MESSAGE_SUCCESS_EVENT),
                false, false);

    }
}
