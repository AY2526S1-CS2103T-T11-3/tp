package seedu.club.model.role;

import static java.util.Objects.requireNonNull;
import static seedu.club.commons.util.AppUtil.checkArgument;
import static seedu.club.commons.util.StringUtil.hasLessThanOrEqualLength;

/**
 * Represents a Role in the club book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidRoleName(String)}
 */
public abstract class Role {

    public static final int FIELD_LENGTH = 30;
    public static final String MESSAGE_CONSTRAINTS =
            "Role names should only contain alphanumeric characters and spaces, "
                    + "be within 30 characters, and should not be blank";
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String roleName;

    /**
     * Constructs a {@code Role}.
     *
     * @param roleName A valid role name.
     */
    public Role(String roleName) {
        requireNonNull(roleName);
        checkArgument(isValidRoleName(roleName), MESSAGE_CONSTRAINTS);
        this.roleName = roleName;
    }

    public abstract boolean equals(Object other);
    public abstract int hashCode();

    /**
     * Returns true if a given string is a valid role name.
     */
    public static boolean isValidRoleName(String test) {
        return test.matches(VALIDATION_REGEX) && hasLessThanOrEqualLength(test, FIELD_LENGTH);
    }

    /**
     * Format state as text for viewing.
     */
    @Override
    public String toString() {
        return roleName;
    }

}
