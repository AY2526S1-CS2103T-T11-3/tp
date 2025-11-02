package seedu.club.logic.commands.member;

import static java.util.Objects.requireNonNull;
import static seedu.club.model.event.Event.updateMemberInAllEvents;

import java.util.List;

import seedu.club.commons.core.index.Index;
import seedu.club.commons.util.ToStringBuilder;
import seedu.club.logic.Messages;
import seedu.club.logic.commands.Command;
import seedu.club.logic.commands.CommandResult;
import seedu.club.logic.commands.exceptions.CommandException;
import seedu.club.model.Model;
import seedu.club.model.ViewState;
import seedu.club.model.member.Member;

/**
 * Deletes a member identified using it's displayed index from the member list.
 */
public class DeleteMemberCommand extends Command {

    public static final String COMMAND_WORD = "deletemember";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the member identified by the index number used in the displayed member list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_MEMBER_SUCCESS = "Member: %1$s deleted successfully";

    private final Index targetIndex;

    public DeleteMemberCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        /* Ensure that member list is displaying first, so that user does not unintentionally delete
           an event if they are on event list instead.
         */
        if (model.getViewState() != ViewState.MEMBER) {
            throw new CommandException(Messages.MESSAGE_NOT_MEMBER_STATE);
        }

        List<Member> lastShownList = model.getFilteredMemberList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_MEMBER_DISPLAYED_INDEX);
        }

        Member memberToDelete = lastShownList.get(targetIndex.getZeroBased());

        // The following line is technically not necessary since already guaranteed to be on member list,
        // but is there as a safety measure.
        model.setViewState(ViewState.MEMBER);
        model.deleteMember(memberToDelete);

        updateMemberInAllEvents(model.getFullEventList(), memberToDelete, null);

        return new CommandResult(String.format(MESSAGE_DELETE_MEMBER_SUCCESS, Messages.format(memberToDelete)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteMemberCommand)) {
            return false;
        }

        DeleteMemberCommand otherDeleteMemberCommand = (DeleteMemberCommand) other;
        return targetIndex.equals(otherDeleteMemberCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
