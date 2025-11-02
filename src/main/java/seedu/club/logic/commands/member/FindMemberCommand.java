package seedu.club.logic.commands.member;

import static java.util.Objects.requireNonNull;

import seedu.club.commons.util.ToStringBuilder;
import seedu.club.logic.Messages;
import seedu.club.logic.commands.Command;
import seedu.club.logic.commands.CommandResult;
import seedu.club.logic.commands.exceptions.CommandException;
import seedu.club.model.Model;
import seedu.club.model.ViewState;
import seedu.club.model.member.Member;
import seedu.club.model.name.NameContainsKeywordsPredicate;

/**
 * Finds and lists all members in club book whose name contains any of the argument keywords.
 * Keyword matching is case-insensitive.
 */
public class FindMemberCommand extends Command {

    public static final String COMMAND_WORD = "findmember";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all members whose names contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice bob charlie";

    private final NameContainsKeywordsPredicate<Member> predicate;

    public FindMemberCommand(NameContainsKeywordsPredicate<Member> predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        /* Ensure that member list is displaying first, so that the ClubBook does not unintentionally show no
           results if the user is on event list instead.
         */
        if (model.getViewState() != ViewState.MEMBER) {
            throw new CommandException(Messages.MESSAGE_NOT_MEMBER_STATE);
        }

        // The following line is technically not necessary since already guaranteed to be on member list,
        // but is there as a safety measure.
        model.setViewState(ViewState.MEMBER);
        model.updateFilteredMemberList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_MEMBERS_LISTED_OVERVIEW, model.getFilteredMemberList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindMemberCommand)) {
            return false;
        }

        FindMemberCommand otherFindMemberCommand = (FindMemberCommand) other;
        return predicate.equals(otherFindMemberCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
