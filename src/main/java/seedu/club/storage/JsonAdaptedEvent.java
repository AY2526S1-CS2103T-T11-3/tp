package seedu.club.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javafx.collections.ObservableList;
import seedu.club.commons.exceptions.IllegalValueException;
import seedu.club.logic.Messages;
import seedu.club.model.event.DateTime;
import seedu.club.model.event.Event;
import seedu.club.model.member.Member;
import seedu.club.model.name.Name;
import seedu.club.model.role.EventRole;

/**
 * Jackson-friendly version of {@link Event}.
 */
class JsonAdaptedEvent {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Event's %s field is missing!";
    public static final String MISSING_MEMBER_MESSAGE_FORMAT = "Event's member %s that is in the roster is missing!";

    private final String name;
    private final String from;
    private final String to;
    private final String details;
    private final List<JsonAdaptedEventRole> roles = new ArrayList<>();
    private final List<String> roster = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedEvent} with the given event details.
     */
    @JsonCreator
    public JsonAdaptedEvent(@JsonProperty("name") String name, @JsonProperty("from") String from,
                            @JsonProperty("to") String to, @JsonProperty("details") String details,
                            @JsonProperty("roles") List<JsonAdaptedEventRole> roles,
                            @JsonProperty("roster") List<String> roster) {
        this.name = name;
        this.from = from;
        this.to = to;
        this.details = details;
        if (roles != null) {
            this.roles.addAll(roles);
        }
        if (roster != null) {
            this.roster.addAll(roster);
        }
    }

    /**
     * Converts a given {@code Event} into this class for Jackson use.
     */
    public JsonAdaptedEvent(Event source) {
        name = source.getName().fullName;
        from = source.getFrom().value;
        to = source.getTo().value;
        details = source.getDetail();
        roles.addAll(source.getRoles().stream()
                .map(JsonAdaptedEventRole::new)
                .collect(Collectors.toList()));
        roster.addAll(source.getRoster().stream()
                .map(m -> m.getName().toString())
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted event object into the model's {@code Event} object.
     *
     * @return Event
     * @throws IllegalValueException if there were any data constraints violated in adapted event.
     */
    public Event toModelType(ObservableList<Member> memberList) throws IllegalValueException {
        final List<EventRole> eventRoles = new ArrayList<>();
        for (JsonAdaptedEventRole role : roles) {
            eventRoles.add(role.toModelType());
        }

        // Map the roster names to existing members
        final Set<Member> modelRoster = new HashSet<>();
        for (String name : roster) {
            Member member = memberList.stream()
                    .filter(m -> m.getName().toString().equals(name))
                    .findFirst()
                    .orElse(null);
            if (member == null) {
                throw new IllegalValueException(String.format(MISSING_MEMBER_MESSAGE_FORMAT, name));
            }
            modelRoster.add(member);
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (from == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, String.class.getSimpleName()));
        }
        final DateTime modelFrom;
        try {
            modelFrom = new DateTime(from);
        } catch (IllegalArgumentException e) {
            throw new IllegalValueException(e.getMessage());
        }

        if (to == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, String.class.getSimpleName()));
        }
        final DateTime modelTo;
        try {
            modelTo = new DateTime(to);
        } catch (IllegalArgumentException e) {
            throw new IllegalValueException(e.getMessage());
        }

        if (!modelFrom.isBefore(modelTo)) {
            throw new IllegalValueException(Messages.MESSAGE_END_BEFORE_START_DATE);
        }

        if (details == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, String.class.getSimpleName()));
        }
        final String modelDetails = this.details;

        final Set<EventRole> modelRoles = new HashSet<>(eventRoles);
        return new Event(modelName, modelFrom, modelTo, modelDetails, modelRoles, modelRoster);
    }
}
