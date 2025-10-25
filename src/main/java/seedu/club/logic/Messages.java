package seedu.club.logic;

import static java.util.stream.Collectors.joining;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.club.logic.parser.ParserUtil;
import seedu.club.logic.parser.Prefix;
import seedu.club.model.event.DateTime;
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
    public static final String MESSAGE_MEMBERS_LISTED_OVERVIEW = "%1$d members listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_NOT_EVENT_STATE =
            "The current list is a member list! Switch to event list first using \"listEvents\"";
    public static final String MESSAGE_NOT_MEMBER_STATE =
            "The current list is an event list! Switch to member list first using \"listMembers\"";
    public static final String MESSAGE_EVENT_NAME_NOT_EXIST = "The event with the name provided does not exist: %1$s";
    public static final String MESSAGE_MEMBER_NAME_NOT_EXIST = "The member with the name provided does not exist: %1$s";
    public static final String MESSAGE_EVENTROLE_NAME_NOT_EXIST = "The event role with the name(s) "
            + "provided does not exist: %1$s";

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
                .append(member.getEmail())
                .append("; Member Roles: ");
        String result = member.getMemberRoles().stream().map(MemberRole::toString)
                .collect(joining(", "));
        builder.append(result);
        return builder.toString();
    }

    /**
     * Formats the {@code event} for display to the user.
     */
    public static String format(Event event) {
        final StringBuilder builder = new StringBuilder();
        builder.append(event.getName())
                .append("; From: ")
                .append(ParserUtil.formatDate(event.getFrom()));

        DateTime to = event.getTo();
        if (to != null) {
            builder.append("; To: ").append(ParserUtil.formatDate(to));
        }

        String detail = event.getDetail();
        if (detail != null && !detail.isEmpty()) {
            builder.append("; Detail: ").append(detail);
        }

        Set<EventRole> roles = event.getRoles();
        if (roles != null && !roles.isEmpty()) {
            builder.append("; Roles: ");
            roles.forEach(builder::append);
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
}
