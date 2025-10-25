package seedu.club.model.util;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.club.model.ClubBook;
import seedu.club.model.ReadOnlyClubBook;
import seedu.club.model.event.DateTime;
import seedu.club.model.event.Event;
import seedu.club.model.member.Email;
import seedu.club.model.member.Member;
import seedu.club.model.member.Phone;
import seedu.club.model.name.Name;
import seedu.club.model.role.EventRole;
import seedu.club.model.role.MemberRole;

/**
 * Contains utility methods for populating {@code ClubBook} with sample data.
 */
public class SampleDataUtil {
    public static Member[] getSampleMembers() {
        return new Member[] {
            new Member(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                getMemberRoleSet("friends")),
            new Member(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                getMemberRoleSet("colleagues", "friends")),
            new Member(new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"),
                getMemberRoleSet("neighbours")),
            new Member(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                getMemberRoleSet("family")),
            new Member(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                getMemberRoleSet("classmates")),
            new Member(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                getMemberRoleSet("colleagues"))
        };
    }

    public static Event[] getSampleEvents() {
        return new Event[] {
            new Event(new Name("Orientation"), new DateTime("15/10/2025 1200"),
                    Optional.of(new DateTime("17/10/2025 1500")), "For freshmen",
                    getEventRoleSet("facilitator", "gamemaster")),
            new Event(new Name("Movie Night"), new DateTime("20/10/2025 1800"),
                    Optional.of(new DateTime("20/10/2025 2000")), "Showing The Shining",
                    getEventRoleSet("FoodIC", "OIC")),
            new Event(new Name("Beach Day"), new DateTime("25/10/2025 0900"),
                    Optional.of(new DateTime("25/10/2025 1300")), "At Sentosa",
                    getEventRoleSet("Gamemaster", "SafetyOfficer"))
        };
    }

    public static ReadOnlyClubBook getSampleClubBook() {
        ClubBook sampleAb = new ClubBook();
        for (Member sampleMember : getSampleMembers()) {
            sampleAb.addMember(sampleMember);
        }
        for (Event sampleEvent : getSampleEvents()) {
            sampleAb.addEvent(sampleEvent);
        }
        return sampleAb;
    }

    /**
     * Returns a member role set containing the list of strings given.
     */
    public static Set<MemberRole> getMemberRoleSet(String... strings) {
        return Arrays.stream(strings)
                .map(MemberRole::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns an event role set containing the list of strings given.
     */
    public static Set<EventRole> getEventRoleSet(String... strings) {
        return Arrays.stream(strings)
                .map(EventRole::new)
                .collect(Collectors.toSet());
    }

}
