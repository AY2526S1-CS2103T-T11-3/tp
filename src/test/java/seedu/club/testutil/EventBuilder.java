package seedu.club.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.club.model.event.Event;
import seedu.club.model.name.Name;
import seedu.club.model.tag.Tag;
import seedu.club.model.util.SampleDataUtil;

/**
 * A utility class to help with building Event objects.
 */
public class EventBuilder {

    public static final String DEFAULT_NAME = "Orientation";
    public static final String DEFAULT_FROM = "Monday";
    public static final String DEFAULT_TO = "Tuesday";
    public static final String DEFAULT_DETAIL = "NIL";

    private Name name;
    private String from;
    private String to;
    private String detail;
    private Set<Tag> roles;

    /**
     * Creates a {@code EventBuilder} with the default detail
     */
    public EventBuilder() {
        name = new Name(DEFAULT_NAME);
        from = DEFAULT_FROM;
        to = DEFAULT_TO;
        detail = DEFAULT_DETAIL;
        roles = new HashSet<>();
    }

    /**
     * Initializes the EventBuilder with the data of {@code eventToCopy}
     * @param eventToCopy event to be copied
     */
    public EventBuilder(Event eventToCopy) {
        name = eventToCopy.getName();
        from = eventToCopy.getFrom();
        to = eventToCopy.getTo();
        detail = DEFAULT_DETAIL;
        roles = new HashSet<>(eventToCopy.getRoles());
    }

    /**
     * Sets the {@code Name} of the {@code Event} that we are building.
     *
     * @param name to be set
     * @return EventBuilder
     */
    public EventBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code roles} into a {@code Set<Tag>} and set it to the {@code Event} that we are building.
     *
     * @param roles to be set
     * @return EventBuilder
     */
    public EventBuilder withRoles(String ... roles) {
        this.roles = SampleDataUtil.getTagSet(roles);
        return this;
    }

    /**
     * Sets the {@code From} of the {@code Event} that we are building.
     *
     * @param from to be set
     * @return EventBuilder
     */
    public EventBuilder withFrom(String from) {
        this.from = from;
        return this;
    }

    /**
     * Sets the {@code To} of the {@code Event} that we are building.
     *
     * @param to to be set
     * @return EventBuilder
     */
    public EventBuilder withTo(String to) {
        this.to = to;
        return this;
    }

    /**
     * Sets the {@code Detail} of the {@code Event} that we are building.
     *
     * @param detail to be set
     * @return EventBuilder
     */
    public EventBuilder withDetail(String detail) {
        this.detail = detail;
        return this;
    }

    public Event build() {
        return new Event(name, from, to, detail, roles);
    }

}
