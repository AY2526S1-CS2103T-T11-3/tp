package seedu.club.logic.parser;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import seedu.club.commons.core.index.Index;
import seedu.club.commons.util.StringUtil;
import seedu.club.logic.parser.exceptions.ParseException;
import seedu.club.model.event.DateTime;
import seedu.club.model.member.Email;
import seedu.club.model.member.Phone;
import seedu.club.model.name.Name;
import seedu.club.model.role.EventRole;
import seedu.club.model.role.MemberRole;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String role} into a {@code MemberRole}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code role} is invalid.
     */
    public static MemberRole parseMemberRole(String role) throws ParseException {
        requireNonNull(role);
        String trimmedRole = role.trim();
        if (!MemberRole.isValidRoleName(trimmedRole)) {
            throw new ParseException(MemberRole.MESSAGE_CONSTRAINTS);
        }
        return new MemberRole(trimmedRole);
    }

    /**
     * Parses a {@code String event} with a {@code String role} into a {@code EventRole}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code role} is invalid.
     */
    public static EventRole parseEventRole(String role) throws ParseException {
        requireNonNull(role);
        String trimmedRole = role.trim();
        if (!EventRole.isValidRoleName(trimmedRole)) {
            throw new ParseException(EventRole.MESSAGE_CONSTRAINTS);
        }
        return new EventRole(trimmedRole);
    }

    /**
     * Parses {@code Collection<String> member roles} into a {@code Set<MemberRole>}.
     */
    public static Set<MemberRole> parseMemberRoles(Collection<String> roles) throws ParseException {
        requireNonNull(roles);
        final Set<MemberRole> memberRoleSet = new HashSet<>();
        for (String roleName : roles) {
            memberRoleSet.add(parseMemberRole(roleName));
        }
        return memberRoleSet;
    }

    /**
     * Parses a {@code String event} with {@code Collection<String> event roles} into a {@code Set<EventRole>}.
     */
    public static Set<EventRole> parseEventRoles(Collection<String> roles) throws ParseException {
        requireNonNull(roles);
        final Set<EventRole> eventRoleSet = new HashSet<>();
        for (String roleName : roles) {
            eventRoleSet.add(parseEventRole(roleName));
        }
        return eventRoleSet;
    }

    /**
     * Parses a {@code String date}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code datetime} is invalid.
     */
    public static DateTime parseDate(String datetime) throws ParseException {
        requireNonNull(datetime);
        String trimmedDateTime = datetime.trim();
        if (!DateTime.isValidDateTime(trimmedDateTime)) {
            throw new ParseException(DateTime.MESSAGE_CONSTRAINTS);
        }
        return new DateTime(trimmedDateTime);
    }

    /**
     * Formats the {@code DateTime} into a readable string.
     */
    public static String formatDate(DateTime date) {
        if (date == null) {
            return "";
        }
        return date.toString();
    }

    /**
     * Parses a {@code String detail}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code role} is invalid.
     */
    public static String parseDetail(String detail) throws ParseException {
        requireNonNull(detail);
        String trimmedDetail = detail.trim();
        if (trimmedDetail.length() > 500) {
            throw new ParseException("Detail should be shorter than 500 characters");
        }
        return trimmedDetail;
    }
}
