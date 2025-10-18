package seedu.club.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.club.model.ClubBook;
import seedu.club.model.ReadOnlyClubBook;
import seedu.club.model.event.Event;
import seedu.club.model.member.Email;
import seedu.club.model.member.Member;
import seedu.club.model.member.Phone;
import seedu.club.model.name.Name;
import seedu.club.model.tag.Tag;

/**
 * Contains utility methods for populating {@code ClubBook} with sample data.
 */
public class SampleDataUtil {
    public static Member[] getSampleMembers() {
        return new Member[] {
            new Member(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                getTagSet("friends")),
            new Member(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                getTagSet("colleagues", "friends")),
            new Member(new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"),
                getTagSet("neighbours")),
            new Member(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                getTagSet("family")),
            new Member(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                getTagSet("classmates")),
            new Member(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                getTagSet("colleagues"))
        };
    }

    public static Event[] getSampleEvents() {
        return new Event[] {
            new Event(new Name("Orientation"), "15/10/2025", "17/10/2025", "For freshmen",
                    getTagSet("facilitator", "gamemaster")),
            new Event(new Name("Movie Night"), "20/10/2025 1800", "20/10/2025 2000", "Showing The Shining",
                    getTagSet("FoodIC", "OIC")),
            new Event(new Name("Beach Day"), "25/10/2025", "25/10/2025", "At Sentosa",
                    getTagSet("Gamemaster", "SafetyOfficer"))
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
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
