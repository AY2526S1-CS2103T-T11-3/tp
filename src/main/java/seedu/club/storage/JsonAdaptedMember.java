package seedu.club.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.club.commons.exceptions.IllegalValueException;
import seedu.club.model.member.Email;
import seedu.club.model.member.Member;
import seedu.club.model.member.Phone;
import seedu.club.model.name.Name;
import seedu.club.model.role.MemberRole;

/**
 * Jackson-friendly version of {@link Member}.
 */
class JsonAdaptedMember {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Member's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final List<JsonAdaptedMemberRole> roles = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedMember} with the given member details.
     */
    @JsonCreator
    public JsonAdaptedMember(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
            @JsonProperty("email") String email, @JsonProperty("roles") List<JsonAdaptedMemberRole> roles) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        if (roles != null) {
            this.roles.addAll(roles);
        }
    }

    /**
     * Converts a given {@code Member} into this class for Jackson use.
     */
    public JsonAdaptedMember(Member source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        roles.addAll(source.getRoles().stream()
                .map(JsonAdaptedMemberRole::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted member object into the model's {@code Member} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted member.
     */
    public Member toModelType() throws IllegalValueException {
        final List<MemberRole> memberRoles = new ArrayList<>();
        for (JsonAdaptedMemberRole role : roles) {
            memberRoles.add(role.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        final Set<MemberRole> modelRoles = new HashSet<>(memberRoles);
        return new Member(modelName, modelPhone, modelEmail, modelRoles);
    }

}
