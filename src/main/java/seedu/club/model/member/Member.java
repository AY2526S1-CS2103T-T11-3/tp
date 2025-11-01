package seedu.club.model.member;

import static seedu.club.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.club.commons.util.ToStringBuilder;
import seedu.club.model.event.Event;
import seedu.club.model.name.Name;
import seedu.club.model.name.NamedEntity;
import seedu.club.model.role.EventRole;
import seedu.club.model.role.MemberRole;

/**
 * Represents a Member in the club book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Member extends NamedEntity {

    // Identity fields
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Set<MemberRole> memberRoles = new HashSet<>();
    private final Set<EventRole> eventRoles = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Member(Name name, Phone phone, Email email, Set<MemberRole> memberRoles) {
        super(name);
        requireAllNonNull(phone, email, memberRoles);
        this.phone = phone;
        this.email = email;
        this.memberRoles.addAll(memberRoles);
    }

    /**
     * Constructs a member with every field given
     */
    public Member(Name name, Phone phone, Email email, Set<MemberRole> memberRoles, Set<EventRole> eventRoles) {
        this(name, phone, email, memberRoles);
        requireAllNonNull(eventRoles);
        this.eventRoles.addAll(eventRoles);
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    /**
     * Returns an immutable member role set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<MemberRole> getMemberRoles() {
        return Collections.unmodifiableSet(memberRoles);
    }

    /**
     * Returns an immutable event role set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<EventRole> getEventRoles() {
        return Collections.unmodifiableSet(eventRoles);
    }

    /**
     * Removes specified EventRoles from the member's roles
     */
    public void removeEventRole(Set<EventRole> roleSet) {
        eventRoles.removeAll(roleSet);
    }

    /**
     * Removes all EventRoles assigned to an event from the member's roles
     */
    public void removeEvent(Event event) {
        eventRoles.removeIf(role -> event.getName().equals(role.getAssignedTo()));
    }

    /**
     * Adds a set of EventRoles to a member's roles
     */
    public void addEventRoles(Set<EventRole> roles) {
        eventRoles.addAll(roles);
    }

    /**
     * Iterate through the given set of event roles. Removes event roles that belong to
     * the old event name in the member's event list and replaces them.
     */
    public Member updateEditedEventRolesList(Set<EventRole> updatedRoles, Name oldName, Name updatedName) {
        Set<EventRole> newRoles = new HashSet<>();
        for (EventRole role : eventRoles) {
            if (role.getAssignedTo().equals(oldName)) {
                handleParticipant(newRoles, updatedName, role);
                updatedRoles.stream()
                        .filter(r -> r.roleName.equals(role.roleName))
                        .findFirst()
                        .ifPresent(newRoles::add);
            } else {
                newRoles.add(role);
            }
        }

        eventRoles.clear();
        eventRoles.addAll(newRoles);

        return this;
    }

    private void handleParticipant(Set<EventRole> newRoles, Name updatedName, EventRole role) {
        if (role.isParticipant()) {
            newRoles.add(new EventRole(updatedName));
        }
    }

    /**
     * Returns true if both members have the same name.
     * This defines a weaker notion of equality between two members.
     */
    public boolean isSameMember(Member otherMember) {
        if (otherMember == this) {
            return true;
        }

        return otherMember != null
                && otherMember.getName().equals(getName());
    }

    /**
     * Returns true if both members have the same identity and data fields.
     * This defines a stronger notion of equality between two members.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Member)) {
            return false;
        }

        Member otherMember = (Member) other;
        // does not include eventRoles as it is mutable
        return name.equals(otherMember.name)
                && phone.equals(otherMember.phone)
                && email.equals(otherMember.email)
                && memberRoles.equals(otherMember.memberRoles);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, memberRoles);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("memberRoles", memberRoles)
                .add("eventRoles", eventRoles)
                .toString();
    }

}
