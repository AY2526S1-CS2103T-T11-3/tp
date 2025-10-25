package seedu.club.logic.commands.event;

import static java.util.Objects.requireNonNull;
import static seedu.club.logic.parser.CliSyntax.PREFIX_DETAIL;
import static seedu.club.logic.parser.CliSyntax.PREFIX_FROM;
import static seedu.club.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.club.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.club.logic.parser.CliSyntax.PREFIX_TO;

import seedu.club.commons.util.ToStringBuilder;
import seedu.club.logic.Messages;
import seedu.club.logic.commands.Command;
import seedu.club.logic.commands.CommandResult;
import seedu.club.logic.commands.exceptions.CommandException;
import seedu.club.model.Model;
import seedu.club.model.ViewState;
import seedu.club.model.event.Event;

/**
 * Adds an event to the club event list.
 */
public class AddEventCommand extends Command {

    public static final String COMMAND_WORD = "addEvent";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an event to the event list. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_FROM + "FROM "
            + "[" + PREFIX_TO + "TO] "
            + "[" + PREFIX_DETAIL + "DETAILS] "
            + "[" + PREFIX_ROLE + "ROLE]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Coding Workshop "
            + PREFIX_FROM + "30/9/2025 1300 "
            + PREFIX_TO + "30/9/2025 1700 "
            + PREFIX_DETAIL + "Bring gloves and trash bags "
            + PREFIX_ROLE + "Publicity "
            + PREFIX_ROLE + "ProjectDirector ";

    public static final String MESSAGE_SUCCESS = "New event added: %1$s";
    public static final String MESSAGE_DUPLICATE_EVENT = "This event already exists in the event list";

    private final Event toAdd;

    /**
     * Creates an AddEventCommand to add the specified {@code Event}
     */
    public AddEventCommand(Event event) {
        requireNonNull(event);
        toAdd = event;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasEvent(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_EVENT);
        }

        model.setViewState(ViewState.EVENT);
        model.addEvent(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddEventCommand)) {
            return false;
        }

        AddEventCommand otherAddEventCommand = (AddEventCommand) other;
        return toAdd.equals(otherAddEventCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
