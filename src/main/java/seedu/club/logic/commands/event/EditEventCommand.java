package seedu.club.logic.commands.event;

import static java.util.Objects.requireNonNull;
import static seedu.club.logic.Messages.MESSAGE_END_BEFORE_START_DATE;
import static seedu.club.logic.parser.CliSyntax.PREFIX_DETAIL;
import static seedu.club.logic.parser.CliSyntax.PREFIX_FROM;
import static seedu.club.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.club.logic.parser.CliSyntax.PREFIX_TO;
import static seedu.club.model.Model.PREDICATE_SHOW_ALL_EVENTS;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.club.commons.core.index.Index;
import seedu.club.commons.util.CollectionUtil;
import seedu.club.commons.util.ToStringBuilder;
import seedu.club.logic.Messages;
import seedu.club.logic.commands.Command;
import seedu.club.logic.commands.CommandResult;
import seedu.club.logic.commands.exceptions.CommandException;
import seedu.club.logic.parser.exceptions.ParseException;
import seedu.club.model.Model;
import seedu.club.model.ViewState;
import seedu.club.model.event.DateTime;
import seedu.club.model.event.Event;
import seedu.club.model.member.Member;
import seedu.club.model.name.Name;
import seedu.club.model.role.EventRole;

/**
 * Edits the details of an existing member in the club book.
 */
public class EditEventCommand extends Command {

    public static final String COMMAND_WORD = "editEvent";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the event identified "
            + "by the index number used in the displayed event list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_FROM + "FROM] "
            + "[" + PREFIX_TO + "TO] "
            + "[" + PREFIX_DETAIL + "DETAILS]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_NAME + "Meeting "
            + PREFIX_FROM + "291125 2030 "
            + PREFIX_TO + "291125 2230 "
            + PREFIX_DETAIL + "Bring writing materials ";

    public static final String MESSAGE_EDIT_EVENT_SUCCESS = "Edited Event: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_EVENT = "This event already exists in the club book.";

    private final Index index;
    private final EditEventDescriptor editEventDescriptor;

    /**
     * @param index of the event in the filtered event list to edit
     * @param editEventDescriptor details to edit the event with
     */
    public EditEventCommand(Index index, EditEventDescriptor editEventDescriptor) {
        requireNonNull(index);
        requireNonNull(editEventDescriptor);

        this.index = index;
        this.editEventDescriptor = new EditEventDescriptor(editEventDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        /* Ensure that event list is displaying first, so that user does not unintentionally edit
           an event if they are on event list instead.
         */
        if (model.getViewState() != ViewState.EVENT) {
            throw new CommandException(Messages.MESSAGE_NOT_EVENT_STATE);
        }

        List<Event> lastShownList = model.getFilteredEventList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
        }

        Event eventToEdit = lastShownList.get(index.getZeroBased());
        Event editedEvent;

        try {
            editedEvent = createEditedEvent(eventToEdit, editEventDescriptor);
        } catch (ParseException pe) {
            throw new CommandException(MESSAGE_END_BEFORE_START_DATE);
        }

        if (!eventToEdit.isSameEvent(editedEvent) && model.hasEvent(editedEvent)) {
            throw new CommandException(MESSAGE_DUPLICATE_EVENT);
        }
        // The following line is technically not necessary since already guaranteed to be on event list,
        // but is there as a safety measure.
        model.setViewState(ViewState.EVENT);
        model.setEvent(eventToEdit, editedEvent);
        model.updateFilteredEventList(PREDICATE_SHOW_ALL_EVENTS);

        // Update eventRoles list of members in roster with new eventRole objects
        Set<Member> roster = editedEvent.getRoster();
        Set<EventRole> eventRoles = editedEvent.getRoles();
        for (Member member: roster) {
            Member updatedMember = member.updateEditedEventRolesList(eventRoles, eventToEdit.getName(),
                    editedEvent.getName());
            model.setMember(member, updatedMember);
        }


        return new CommandResult(String.format(MESSAGE_EDIT_EVENT_SUCCESS, Messages.format(editedEvent)));
    }

    /**
     * Creates and returns a {@code Event} with the details of {@code eventToEdit}
     * edited with {@code editEventDescriptor}.
     */
    private static Event createEditedEvent(
            Event eventToEdit, EditEventDescriptor editEventDescriptor) throws ParseException {
        assert eventToEdit != null;


        Name updatedName = editEventDescriptor.getName().orElse(eventToEdit.getName());
        DateTime updatedFrom = editEventDescriptor.getFrom().orElse(eventToEdit.getFrom());
        DateTime updatedTo = editEventDescriptor.getTo().orElse(eventToEdit.getTo());
        String updatedDetails = editEventDescriptor.getDetails().orElse(eventToEdit.getDetail());
        Set<EventRole> eventRoles = eventToEdit.updateEventRolesAssignedTo(updatedName);
        Set<Member> roster = eventToEdit.getRoster();

        if (!updatedFrom.isBefore(updatedTo)) {
            throw new ParseException(String.format(MESSAGE_END_BEFORE_START_DATE));
        }

        return new Event(updatedName, updatedFrom, updatedTo, updatedDetails, eventRoles, roster);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditEventCommand)) {
            return false;
        }

        EditEventCommand otherEditEventCommand = (EditEventCommand) other;
        return index.equals(otherEditEventCommand.index)
                && editEventDescriptor.equals(otherEditEventCommand.editEventDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editEventDescriptor", editEventDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the event with. Each non-empty field value will replace the
     * corresponding field value of the event.
     */
    public static class EditEventDescriptor {
        private Name name;
        private DateTime from;
        private DateTime to;
        private String details;

        public EditEventDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code roles} is used internally.
         */
        public EditEventDescriptor(EditEventDescriptor toCopy) {
            setName(toCopy.name);
            setFrom(toCopy.from);
            setTo(toCopy.to);
            setDetails(toCopy.details);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, from, to, details);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setFrom(DateTime from) {
            this.from = from;
        }

        public Optional<DateTime> getFrom() {
            return Optional.ofNullable(from);
        }

        public void setTo(DateTime to) {
            this.to = to;
        }

        public Optional<DateTime> getTo() {
            return Optional.ofNullable(to);
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public Optional<String> getDetails() {
            return Optional.ofNullable(details);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditEventDescriptor otherEditEventDescriptor)) {
                return false;
            }

            return Objects.equals(name, otherEditEventDescriptor.name)
                    && Objects.equals(from, otherEditEventDescriptor.from)
                    && Objects.equals(to, otherEditEventDescriptor.to)
                    && Objects.equals(details, otherEditEventDescriptor.details);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("from", from)
                    .add("to", to)
                    .add("details", details)
                    .toString();
        }
    }
}
