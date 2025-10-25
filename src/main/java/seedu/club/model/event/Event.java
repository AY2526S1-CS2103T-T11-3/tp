package seedu.club.model.event;

import static seedu.club.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.club.commons.util.ToStringBuilder;
import seedu.club.model.member.Member;
import seedu.club.model.name.Name;
import seedu.club.model.name.NamedEntity;
import seedu.club.model.role.EventRole;

/**
 * Represents an Event in the club book.
 * Guarantees: detail is present and not null, field values are validated, immutable.
 */
public class Event extends NamedEntity {

    // Identity fields
    private final DateTime from;
    private final Optional<DateTime> to;
    private final String detail;

    // Data fields
    private final Set<EventRole> roles = new HashSet<>();
    private final Set<Member> roster = new HashSet<>();

    /**
     * Creates an Event containing no participating members
     * Every field must be present and not null.
     */
    public Event(Name name, DateTime from, Optional<DateTime> to,
                 String detail, Set<EventRole> roles) {
        super(name);
        requireAllNonNull(from, to, detail, roles);
        this.from = from;
        this.to = to;
        this.detail = detail;
        this.roles.addAll(roles);

        // assign each eventRole to this event
        for (EventRole role : this.roles) {
            role.setAssignedTo(this);
        }
    }

    /**
     * Creates an Event with the given roster
     * Every field must be present and not null.
     */
    public Event(Name name, DateTime from, Optional<DateTime> to,
                 String detail, Set<EventRole> roles, Set<Member> roster) {
        super(name);
        requireAllNonNull(from, to, detail, roles, roster);
        this.from = from;
        this.to = to;
        this.detail = detail;
        this.roles.addAll(roles);
        this.roster.addAll(roster);

        // assign each eventRole to this event
        for (EventRole role : this.roles) {
            role.setAssignedTo(this);
        }
    }

    public DateTime getFrom() {
        return from;
    }

    public DateTime getTo() {
        return to.orElse(null);
    }

    public String getDetail() {
        return detail;
    }

    /**
     * Returns an immutable role set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<EventRole> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    /**
     * Returns an immutable member set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Member> getRoster() {
        return Collections.unmodifiableSet(roster);
    }

    /**
     * Removes specified member from the event's roster
     */
    public void removeMemberFromRoster(Member member) {
        roster.remove(member);
    }

    /**
     * Returns true if a member with the same identity as {@code member} exists in the event roster.
     */
    public boolean hasMember(Member member) {
        return getRoster().contains(member);
    }

    /**
     * Adds the given member to the event roster.
     */
    public void addMember(Member member) {
        roster.add(member);
    }

    /**
     * Returns true if both events have the same name, from date time and to date time.
     * This defines a weaker notion of equality between two events.
     */
    public boolean isSameEvent(Event otherEvent) {
        if (otherEvent == this) {
            return true;
        }

        return otherEvent != null
                && otherEvent.getName().equals(getName())
                && otherEvent.getFrom().equals(getFrom())
                && otherEvent.getTo().equals(getTo());
    }

    /**
     * Returns true if both events have the same identity and data fields.
     * This defines a stronger notion of equality between two events.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Event)) {
            return false;
        }

        Event otherEvent = (Event) other;
        return name.equals(otherEvent.name)
                && from.equals(otherEvent.from)
                && to.equals(otherEvent.to)
                && detail.equals(otherEvent.detail)
                && roles.equals(otherEvent.roles)
                && roster.equals(otherEvent.roster);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, from, to, detail, roles, roster);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("from", from)
                .add("to", to)
                .add("detail", detail)
                .add("roles", roles)
                .add("roster", roster)
                .toString();
    }

}
