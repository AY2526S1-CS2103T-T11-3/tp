package seedu.club.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.club.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.club.logic.parser.CliSyntax.PREFIX_FROM;
import static seedu.club.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.club.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.club.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.club.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.club.logic.parser.CliSyntax.PREFIX_TO;
import static seedu.club.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.club.commons.core.index.Index;
import seedu.club.logic.commands.exceptions.CommandException;
import seedu.club.model.ClubBook;
import seedu.club.model.Model;
import seedu.club.model.event.Event;
import seedu.club.model.member.Member;
import seedu.club.model.name.NameContainsKeywordsPredicate;
import seedu.club.testutil.EditMemberDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_MEMBER_NAME_AMY = "Amy Bee";
    public static final String VALID_MEMBER_NAME_BOB = "Bob Choo";
    public static final String VALID_PHONE_AMY = "11111111";
    public static final String VALID_PHONE_BOB = "22222222";
    public static final String VALID_EMAIL_AMY = "amy@example.com";
    public static final String VALID_EMAIL_BOB = "bob@example.com";
    public static final String VALID_TAG_HUSBAND = "husband";
    public static final String VALID_TAG_FRIEND = "friend";

    public static final String VALID_EVENT_NAME_BEACHDAY = "Beach Day";
    public static final String VALID_EVENT_NAME_ORIENTATION = "Orientation";
    public static final String VALID_FROM_BEACHDAY = "25/10/2025";
    public static final String VALID_FROM_ORIENTATION = "15/10/2025";
    public static final String VALID_TO_BEACHDAY = "25/10/2025";
    public static final String VALID_TO_ORIENTATION = "17/10/2025";
    public static final String VALID_ROLE_FACILITATOR = "facilitator";
    public static final String VALID_ROLE_FOODIC = "FoodIC";

    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_MEMBER_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_MEMBER_NAME_BOB;
    public static final String PHONE_DESC_AMY = " " + PREFIX_PHONE + VALID_PHONE_AMY;
    public static final String PHONE_DESC_BOB = " " + PREFIX_PHONE + VALID_PHONE_BOB;
    public static final String EMAIL_DESC_AMY = " " + PREFIX_EMAIL + VALID_EMAIL_AMY;
    public static final String EMAIL_DESC_BOB = " " + PREFIX_EMAIL + VALID_EMAIL_BOB;
    public static final String TAG_DESC_FRIEND = " " + PREFIX_TAG + VALID_TAG_FRIEND;
    public static final String TAG_DESC_HUSBAND = " " + PREFIX_TAG + VALID_TAG_HUSBAND;

    public static final String NAME_DESC_BEACHDAY = " " + PREFIX_NAME + VALID_EVENT_NAME_BEACHDAY;
    public static final String NAME_DESC_ORIENTATION = " " + PREFIX_NAME + VALID_EVENT_NAME_ORIENTATION;
    public static final String FROM_DESC_BEACHDAY = " " + PREFIX_FROM + VALID_FROM_BEACHDAY;
    public static final String FROM_DESC_ORIENTATION = " " + PREFIX_FROM + VALID_FROM_ORIENTATION;
    public static final String TO_DESC_BEACHDAY = " " + PREFIX_TO + VALID_TO_BEACHDAY;
    public static final String TO_DESC_ORIENTATION = " " + PREFIX_TO + VALID_TO_ORIENTATION;
    public static final String ROLE_DESC_FACILITATOR = " " + PREFIX_ROLE + VALID_ROLE_FACILITATOR;
    public static final String ROLE_DESC_FOODIC = " " + PREFIX_ROLE + VALID_ROLE_FOODIC;

    public static final String INVALID_MEMBER_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "911a"; // 'a' not allowed in phones
    public static final String INVALID_EMAIL_DESC = " " + PREFIX_EMAIL + "bob!yahoo"; // missing '@' symbol
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "hubby*"; // '*' not allowed in tags

    public static final String INVALID_EVENT_NAME_DESC = " " + PREFIX_NAME + "Orientation&"; // '&' not allowed in names
    /*
     DATETIME not implemented yet
     public static final String INVALID_FROM_DESC = " " + PREFIX_FROM + "911a"; // 'a' not allowed in phones
     public static final String INVALID_EMAIL_DESC = " " + PREFIX_EMAIL + "bob!yahoo"; // missing '@' symbol
     */
    public static final String INVALID_ROLE_DESC = " " + PREFIX_ROLE + "facilitator*"; // '*' not allowed in roles

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditCommand.EditMemberDescriptor DESC_AMY;
    public static final EditCommand.EditMemberDescriptor DESC_BOB;

    static {
        DESC_AMY = new EditMemberDescriptorBuilder().withName(VALID_MEMBER_NAME_AMY)
                .withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        DESC_BOB = new EditMemberDescriptorBuilder().withName(VALID_MEMBER_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandResult expectedCommandResult,
            Model expectedModel) {
        try {
            CommandResult result = command.execute(actualModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertMemberCommandSuccess(Command command, Model actualModel, String expectedMessage,
                                                  Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertEventCommandSuccess(Command command, Model actualModel, String expectedMessage,
                                                 Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, true);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the club book, filtered member list and selected member in {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        ClubBook expectedClubBook = new ClubBook(actualModel.getClubBook());
        List<Member> expectedFilteredList = new ArrayList<>(actualModel.getFilteredMemberList());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedClubBook, actualModel.getClubBook());
        assertEquals(expectedFilteredList, actualModel.getFilteredMemberList());
    }
    /**
     * Updates {@code model}'s filtered list to show only the member at the given {@code targetIndex} in the
     * {@code model}'s club book.
     */
    public static void showMemberAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredMemberList().size());

        Member member = model.getFilteredMemberList().get(targetIndex.getZeroBased());
        final String[] splitName = member.getName().fullName.split("\\s+");
        model.updateFilteredMemberList(new NameContainsKeywordsPredicate<>(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredMemberList().size());
    }

    /**
     * Updates {@code model}'s filtered list to show only the event at the given {@code targetIndex} in the
     * {@code model}'s club book.
     */
    public static void showEventAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredEventList().size());

        Event event = model.getFilteredEventList().get(targetIndex.getZeroBased());
        final String[] splitName = event.getName().fullName.split("\\s+");
        model.updateFilteredEventList(new NameContainsKeywordsPredicate<>(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredEventList().size());
    }

}
