package seedu.club.model.name;

import static java.util.Objects.requireNonNull;
import static seedu.club.commons.util.AppUtil.checkArgument;
import static seedu.club.commons.util.StringUtil.hasLessThanOrEqualLength;

/**
 * Represents the name of a {@code NamedEntity}.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    public static final int FIELD_LENGTH = 50;
    public static final String MESSAGE_CONSTRAINTS =
            "Names should only contain alphanumeric characters and spaces, "
                    + "be within 50 characters, and should not be blank";

    /*
     * The first character of the name must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String fullName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        requireNonNull(name);
        checkArgument(isValidName(name), MESSAGE_CONSTRAINTS);
        fullName = name;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return test.matches(VALIDATION_REGEX) && hasLessThanOrEqualLength(test, FIELD_LENGTH);
    }

    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Name)) {
            return false;
        }

        Name otherName = (Name) other;
        return fullName.equalsIgnoreCase(otherName.fullName);
    }

    @Override
    public int hashCode() {
        return fullName.toLowerCase().hashCode();
    }

}
