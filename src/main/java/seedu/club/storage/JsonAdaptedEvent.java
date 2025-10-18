package seedu.club.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.club.commons.exceptions.IllegalValueException;
import seedu.club.model.event.Event;
import seedu.club.model.name.Name;
import seedu.club.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Event}.
 */
class JsonAdaptedEvent {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Event's %s field is missing!";

    private final String name;
    private final String from;
    private final String to;
    private final String details;
    private final List<JsonAdaptedTag> roles = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedEvent} with the given event details.
     */
    @JsonCreator
    public JsonAdaptedEvent(@JsonProperty("name") String name, @JsonProperty("from") String from,
                            @JsonProperty("to") String to, @JsonProperty("details") String details,
                            @JsonProperty("tags") List<JsonAdaptedTag> roles) {
        this.name = name;
        this.from = from;
        this.to = to;
        this.details = details;
        if (roles != null) {
            this.roles.addAll(roles);
        }
    }

    public JsonAdaptedEvent(Event source) {
        name = source.getName().fullName;
        from = source.getFrom();
        to = source.getTo();
        details = source.getDetail();
        roles.addAll(source.getRoles().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted event object into the model's {@code Event} object.
     *
     * @return Event
     * @throws IllegalValueException if there were any data constraints violated in adapted event.
     */
    public Event toModelType() throws IllegalValueException {
        final List<Tag> eventRoles = new ArrayList<>();
        for (JsonAdaptedTag role : roles) {
            eventRoles.add(role.toModelType());
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
        final String modelFrom = this.from;

        if (to == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, String.class.getSimpleName()));
        }
        final String modelTo = this.to;

        if (details == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, String.class.getSimpleName()));
        }
        final String modelDetails = this.details;

        final Set<Tag> modelRoles = new HashSet<>(eventRoles);
        return new Event(modelName, modelFrom, modelTo, modelDetails, modelRoles);
    }


}
