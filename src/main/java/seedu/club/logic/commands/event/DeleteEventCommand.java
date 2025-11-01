package seedu.club.logic.commands.event;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Set;

import seedu.club.commons.core.index.Index;
import seedu.club.commons.util.ToStringBuilder;
import seedu.club.logic.Messages;
import seedu.club.logic.commands.Command;
import seedu.club.logic.commands.CommandResult;
import seedu.club.logic.commands.exceptions.CommandException;
import seedu.club.model.Model;
import seedu.club.model.ViewState;
import seedu.club.model.event.Event;
import seedu.club.model.member.Member;

/**
 * Deletes an event identified using its displayed index from the event list
 */
public class DeleteEventCommand extends Command {
    public static final String COMMAND_WORD = "deleteEvent";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the event identified by the index number used in the displayed events list.\n"
            + "Parameters : INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_EVENT_SUCCESS = "Event: %1$s deleted successfully";

    private final Index targetIndex;

    public DeleteEventCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        /* Ensure that event list is displaying first, so that user does not unintentionally delete
           an event if they are on member list instead.
         */
        if (model.getViewState() != ViewState.EVENT) {
            throw new CommandException(Messages.MESSAGE_NOT_EVENT_STATE);
        }

        List<Event> lastShownList = model.getFilteredEventList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
        }

        Event eventToDelete = lastShownList.get(targetIndex.getZeroBased());

        Set<Member> assignedMembers = eventToDelete.getRoster();
        for (Member member: assignedMembers) {
            member.removeEvent(eventToDelete);
        }

        // The following line is technically not necessary since already guaranteed to be on event list,
        // but is there as a safety measure.
        model.setViewState(ViewState.EVENT);
        model.deleteEvent(eventToDelete);
        model.updateFilteredEventList(e -> true);
        model.updateFilteredMemberList(m -> true);
        return new CommandResult(String.format(MESSAGE_DELETE_EVENT_SUCCESS, Messages.format(eventToDelete)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        //instanceof handles nulls
        if (!(other instanceof DeleteEventCommand)) {
            return false;
        }

        DeleteEventCommand otherDeleteEventCommand = (DeleteEventCommand) other;
        return targetIndex.equals(otherDeleteEventCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
