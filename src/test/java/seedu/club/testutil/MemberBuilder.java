package seedu.club.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.club.model.member.Email;
import seedu.club.model.member.Member;
import seedu.club.model.member.Phone;
import seedu.club.model.name.Name;
import seedu.club.model.tag.Tag;
import seedu.club.model.util.SampleDataUtil;

/**
 * A utility class to help with building Member objects.
 */
public class MemberBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";

    private Name name;
    private Phone phone;
    private Email email;
    private Set<Tag> tags;

    /**
     * Creates a {@code MemberBuilder} with the default details.
     */
    public MemberBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        tags = new HashSet<>();
    }

    /**
     * Initializes the MemberBuilder with the data of {@code memberToCopy}.
     */
    public MemberBuilder(Member memberToCopy) {
        name = memberToCopy.getName();
        phone = memberToCopy.getPhone();
        email = memberToCopy.getEmail();
        tags = new HashSet<>(memberToCopy.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code Member} that we are building.
     */
    public MemberBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Member} that we are building.
     */
    public MemberBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Member} that we are building.
     */
    public MemberBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Member} that we are building.
     */
    public MemberBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    public Member build() {
        return new Member(name, phone, email, tags);
    }

}
