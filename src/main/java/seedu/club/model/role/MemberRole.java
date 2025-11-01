package seedu.club.model.role;

/**
 * Represents the role of a member in the club book.
 */
public class MemberRole extends Role {
    /**
     * Constructs a {@code Role}.
     *
     * @param roleName A valid role name.
     */
    public MemberRole(String roleName) {
        super(roleName);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MemberRole)) {
            return false;
        }

        MemberRole otherRole = (MemberRole) other;
        return roleName.equalsIgnoreCase(otherRole.roleName);
    }

    @Override
    public int hashCode() {
        return roleName.toLowerCase().hashCode();
    }
}
