package seedu.club.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.club.logic.Messages.MESSAGE_MEMBERS_LISTED_OVERVIEW;
import static seedu.club.testutil.TypicalMembers.CARL;
import static seedu.club.testutil.TypicalMembers.ELLE;
import static seedu.club.testutil.TypicalMembers.FIONA;
import static seedu.club.testutil.TypicalMembers.getTypicalClubBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.club.model.Model;
import seedu.club.model.ModelManager;
import seedu.club.model.UserPrefs;
import seedu.club.model.member.Member;
import seedu.club.model.name.NameContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(getTypicalClubBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalClubBook(), new UserPrefs());

    @Test
    public void equals() {
        NameContainsKeywordsPredicate<Member> firstPredicate =
                new NameContainsKeywordsPredicate<>(Collections.singletonList("first"));
        NameContainsKeywordsPredicate<Member> secondPredicate =
                new NameContainsKeywordsPredicate<>(Collections.singletonList("second"));

        FindCommand findFirstCommand = new FindCommand(firstPredicate);
        FindCommand findSecondCommand = new FindCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different member -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noMemberFound() {
        String expectedMessage = String.format(MESSAGE_MEMBERS_LISTED_OVERVIEW, 0);
        NameContainsKeywordsPredicate<Member> predicate = preparePredicate(" ");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredMemberList(predicate);
        CommandTestUtil.assertMemberCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredMemberList());
    }

    @Test
    public void execute_multipleKeywords_multipleMembersFound() {
        String expectedMessage = String.format(MESSAGE_MEMBERS_LISTED_OVERVIEW, 3);
        NameContainsKeywordsPredicate<Member> predicate = preparePredicate("Kurz Elle Kunz");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredMemberList(predicate);
        CommandTestUtil.assertMemberCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredMemberList());
    }

    @Test
    public void toStringMethod() {
        NameContainsKeywordsPredicate<Member> predicate = new NameContainsKeywordsPredicate<>(Arrays.asList("keyword"));
        FindCommand findCommand = new FindCommand(predicate);
        String expected = FindCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private NameContainsKeywordsPredicate<Member> preparePredicate(String userInput) {
        return new NameContainsKeywordsPredicate<>(Arrays.asList(userInput.split("\\s+")));
    }
}
