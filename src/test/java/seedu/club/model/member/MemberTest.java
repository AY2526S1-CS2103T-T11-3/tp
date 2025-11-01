package seedu.club.model.member;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.club.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.club.logic.commands.CommandTestUtil.VALID_MEMBER_NAME_BOB;
import static seedu.club.logic.commands.CommandTestUtil.VALID_MEMBER_ROLE_PRESIDENT;
import static seedu.club.logic.commands.CommandTestUtil.VALID_MEMBER_ROLE_TREASURER;
import static seedu.club.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.club.testutil.Assert.assertThrows;
import static seedu.club.testutil.TypicalClubBook.ALICE;
import static seedu.club.testutil.TypicalMembers.BOB;

import org.junit.jupiter.api.Test;

import seedu.club.testutil.MemberBuilder;

public class MemberTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Member member = new MemberBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> member.getMemberRoles().remove(0));
    }

    @Test
    public void isSameMember() {
        // same object -> returns true
        assertTrue(ALICE.isSameMember(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSameMember(null));

        // same name, all other attributes different -> returns true
        Member editedAlice = new MemberBuilder(ALICE).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withMemberRoles(VALID_MEMBER_ROLE_PRESIDENT).build();
        assertTrue(ALICE.isSameMember(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new MemberBuilder(ALICE).withName(VALID_MEMBER_NAME_BOB).build();
        assertFalse(ALICE.isSameMember(editedAlice));

        // name differs in case, all other attributes same -> returns false
        Member editedBob = new MemberBuilder(BOB).withName(VALID_MEMBER_NAME_BOB.toLowerCase()).build();
        assertTrue(BOB.isSameMember(editedBob));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_MEMBER_NAME_BOB + " ";
        editedBob = new MemberBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(BOB.isSameMember(editedBob));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Member aliceCopy = new MemberBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different member -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Member editedAlice = new MemberBuilder(ALICE).withName(VALID_MEMBER_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new MemberBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new MemberBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different roles -> returns false
        editedAlice = new MemberBuilder(ALICE).withMemberRoles(VALID_MEMBER_ROLE_TREASURER).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        String expected = Member.class.getCanonicalName() + "{name=" + ALICE.getName() + ", phone=" + ALICE.getPhone()
                + ", email=" + ALICE.getEmail() + ", memberRoles=" + ALICE.getMemberRoles()
                + ", eventRoles=" + ALICE.getEventRoles() + "}";
        assertEquals(expected, ALICE.toString());
    }
}
