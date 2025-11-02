package seedu.club.logic.commands.event;

import static java.util.Objects.requireNonNull;

import seedu.club.commons.util.ToStringBuilder;
import seedu.club.logic.Messages;
import seedu.club.logic.commands.Command;
import seedu.club.logic.commands.CommandResult;
import seedu.club.logic.commands.exceptions.CommandException;
import seedu.club.model.Model;
import seedu.club.model.ViewState;
import seedu.club.model.event.Event;
import seedu.club.model.name.NameContainsKeywordsPredicate;

/**
 * Finds and lists all events in club book whose name contains any of the argument keywords.
 * Keyword matching is case-insensitive.
 */
public class FindEventCommand extends Command {
    public static final String COMMAND_WORD = "findevent";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all events whose names contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " orientation movie";

    private final NameContainsKeywordsPredicate<Event> predicate;

    public FindEventCommand(NameContainsKeywordsPredicate<Event> predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        /* Ensure that event list is displaying first, so that the ClubBook does not unintentionally show no
           results if the user is on member list instead.
         */
        if (model.getViewState() != ViewState.EVENT) {
            throw new CommandException(Messages.MESSAGE_NOT_EVENT_STATE);
        }

        // The following line is there as a safety measure.
        model.setViewState(ViewState.EVENT);
        model.updateFilteredEventList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_EVENTS_LISTED_OVERVIEW, model.getFilteredEventList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindEventCommand)) {
            return false;
        }

        FindEventCommand otherFindEventCommand = (FindEventCommand) other;
        return predicate.equals(otherFindEventCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
