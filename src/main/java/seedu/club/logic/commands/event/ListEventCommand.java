package seedu.club.logic.commands.event;

import static java.util.Objects.requireNonNull;
import static seedu.club.model.Model.PREDICATE_SHOW_ALL_EVENTS;

import seedu.club.logic.commands.Command;
import seedu.club.logic.commands.CommandResult;
import seedu.club.model.Model;
import seedu.club.model.ViewState;

/**
 * Lists all upcoming events in the club to the user.
 */
public class ListEventCommand extends Command {

    public static final String COMMAND_WORD = "listevents";

    public static final String MESSAGE_SUCCESS = "Listed all events";
    public static final String MESSAGE_EMPTY_LIST = "There are currently no events";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setViewState(ViewState.EVENT);
        model.updateFilteredEventList(PREDICATE_SHOW_ALL_EVENTS);

        if (!model.getFilteredEventList().isEmpty()) {
            return new CommandResult(MESSAGE_SUCCESS);
        } else {
            return new CommandResult(MESSAGE_EMPTY_LIST);
        }
    }
}
