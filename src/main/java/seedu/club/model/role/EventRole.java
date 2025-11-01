package seedu.club.model.role;

import java.util.Objects;

import seedu.club.model.name.Name;

/**
 * Represents the role of a member in a specific event for which that member is assigned to.
 */
public class EventRole extends Role {
    private Name assignedTo;
    private boolean isParticipant;

    /**
     * Constructs an {@code EventRole} with the specified role name and event assignment.
     *
     * @param roleName A valid role name.
     * @param assignedTo The event that the {@code EventRole} belongs to
     */
    public EventRole(String roleName, Name assignedTo) {
        super(roleName);
        this.assignedTo = assignedTo;
        this.isParticipant = false;
    }

    /**
     * Constructs an {@code EventRole} that is a "Participant"
     *
     * @param assignedTo The event that the {@code EventRole} belongs to
     */
    public EventRole(Name assignedTo) {
        super("Participant");
        this.assignedTo = assignedTo;
        this.isParticipant = true;
    }

    public Name getAssignedTo() {
        return assignedTo;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EventRole)) {
            return false;
        }

        EventRole otherRole = (EventRole) other;
        return roleName.equalsIgnoreCase(otherRole.roleName)
                && assignedTo.equals(otherRole.assignedTo)
                && isParticipant == otherRole.isParticipant;
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleName.toLowerCase(), assignedTo, isParticipant);
    }

    @Override
    public String toString() {
        if (isParticipant) {
            return assignedTo.toString(); // Just the event name since no role name
        }
        return assignedTo + ">" + roleName;
    }
}
