package seedu.club.model.event;

import static java.util.Objects.requireNonNull;
import static seedu.club.commons.util.AppUtil.checkArgument;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Represents an Event's start or end date and time in the club book.
 * This class uses Java standard library's {@link java.time.LocalDateTime}
 * <p></p>
 * Guarantees: immutable; is valid as declared in {@link #isValidDateTime(String)}
 * @see java.time.LocalDateTime
 */
public class DateTime {

    public static final String MESSAGE_CONSTRAINTS =
            "Date and time should be in the format DD/MM/YYYY HHMM in 24-hour format (no leading zeros required)";

    public static final DateTimeFormatter DATETIME_FORMAT = new DateTimeFormatterBuilder()
            .appendPattern("d/M/uuuu HHmm")
            .toFormatter()
            .withResolverStyle(ResolverStyle.STRICT); // Without this, Java adjusts invalid dates to nearest valid one.

    public final String value;

    /**
     * Constructs a {@code DateTime}.
     *
     * @param datetime A valid date and time.
     */
    public DateTime(String datetime) {
        requireNonNull(datetime);
        if (!datetime.isEmpty()) {
            checkArgument(isValidDateTime(datetime), MESSAGE_CONSTRAINTS);
        }
        value = datetime;
    }

    /**
     * Returns true if a given string is a valid date and time.
     */
    public static boolean isValidDateTime(String test) {
        try {
            LocalDateTime.parse(test, DATETIME_FORMAT);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }



    @Override
    public String toString() {
        if (value.isEmpty()) {
            return value;
        }
        return LocalDateTime.parse(value, DATETIME_FORMAT)
                .format(DateTimeFormatter.ofPattern("d MMM yyyy h:mma"));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DateTime)) {
            return false;
        }

        DateTime otherDateTime = (DateTime) other;
        return value.equals(otherDateTime.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
