package seedu.club.logic.commands.member;

import static java.util.Objects.requireNonNull;
import static seedu.club.model.Model.PREDICATE_SHOW_ALL_MEMBERS;

import seedu.club.logic.commands.Command;
import seedu.club.logic.commands.CommandResult;
import seedu.club.model.Model;
import seedu.club.model.ViewState;

/**
 * Lists all members in the club to the user.
 */
public class ListMemberCommand extends Command {

    public static final String COMMAND_WORD = "listmembers";

    public static final String MESSAGE_SUCCESS = "Listed all members";
    public static final String MESSAGE_EMPTY_LIST = "There are currently no members in the club";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setViewState(ViewState.MEMBER);
        model.updateFilteredMemberList(PREDICATE_SHOW_ALL_MEMBERS);

        if (!model.getFilteredMemberList().isEmpty()) {
            return new CommandResult(MESSAGE_SUCCESS);
        } else {
            return new CommandResult(MESSAGE_EMPTY_LIST);
        }
    }
}
