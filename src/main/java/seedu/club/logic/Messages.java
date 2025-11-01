package seedu.club.logic;

import static java.util.stream.Collectors.joining;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.club.logic.parser.ParserUtil;
import seedu.club.logic.parser.Prefix;
import seedu.club.model.event.Event;
import seedu.club.model.member.Member;
import seedu.club.model.role.EventRole;
import seedu.club.model.role.MemberRole;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_MEMBER_DISPLAYED_INDEX = "The member index provided is invalid";
    public static final String MESSAGE_INVALID_EVENT_DISPLAYED_INDEX = "The event index provided is invalid";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";

    public static final String MESSAGE_MEMBERS_LISTED_OVERVIEW = "%1$d members listed!";
    public static final String MESSAGE_EVENTS_LISTED_OVERVIEW = "%1$d events listed!";

    public static final String MESSAGE_NOT_EVENT_STATE =
            "The current page is not an event list! Switch to event list first using \"listEvents\"";
    public static final String MESSAGE_NOT_MEMBER_STATE =
            "The current page is not a member list! Switch to member list first using \"listMembers\"";

    public static final String MESSAGE_EVENT_NAME_NOT_EXIST = "The event with the name provided does not exist: %1$s";
    public static final String MESSAGE_MEMBER_NAME_NOT_EXIST = "The member with the name provided does not exist: %1$s";
    public static final String MESSAGE_EVENTROLE_NAME_NOT_EXIST = "The event role(s) belonging to %1$s with the name(s)"
            + " provided does not exist: %2$s";
    public static final String MESSAGE_MEMBER_NAME_NOT_IN_EVENT_ROSTER = "The member %1$s is not "
            + "assigned to the event: %2$s";

    public static final String MESSAGE_END_BEFORE_START_DATE = "Starting date/time should be before ending date/time";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code member} for display to the user.
     */
    public static String format(Member member) {
        final StringBuilder builder = new StringBuilder();
        builder.append(member.getName())
                .append("; Phone: ")
                .append(member.getPhone())
                .append("; Email: ")
                .append(member.getEmail());

        Set<MemberRole> roles = member.getMemberRoles();
        if (roles != null && !roles.isEmpty()) {
            builder.append("; Member Roles: ");
            String result = roles.stream().map(MemberRole::toString).sorted()
                    .collect(joining(", "));
            builder.append(result);
        }

        return builder.toString();
    }

    /**
     * Formats the {@code event} for display to the user.
     */
    public static String format(Event event) {
        final StringBuilder builder = new StringBuilder();
        builder.append(event.getName())
                .append("; From: ")
                .append(ParserUtil.formatDateTime(event.getFrom()))
                .append("; To: ")
                .append(ParserUtil.formatDateTime(event.getTo()));

        String detail = event.getDetail();
        if (detail != null && !detail.isEmpty()) {
            builder.append("; Detail: ").append(detail);
        }

        Set<EventRole> roles = event.getRoles();
        if (roles != null && !roles.isEmpty()) {
            builder.append("; Roles: ");
            String result = roles.stream().map(EventRole::toString).sorted()
                    .collect(joining(", "));
            builder.append(result);
        }

        Set<Member> roster = event.getRoster();
        if (roster != null && !roster.isEmpty()) {
            builder.append("; Members Assigned: ");
            String result = roster.stream().map(x -> x.getName().toString())
                    .collect(joining(", "));
            builder.append(result);
        }

        return builder.toString();
    }

    /**
     * Formats the assigning of a {@code Member} to an {@code EventRole} in the {@code Event}
     */
    public static String formatAssignRole(Member member, Event event, Set<EventRole> roles) {
        final StringBuilder builder = new StringBuilder();
        builder.append("Assigned ")
                .append(member.getName())
                .append(" to ")
                .append(event.getName());

        if (roles != null && !roles.isEmpty()) {
            builder.append(" with event role(s): ");
            String result = roles.stream().map(r -> r.roleName).sorted()
                    .collect(joining(", "));
            builder.append(result);
        }

        return builder.toString();
    }
}
