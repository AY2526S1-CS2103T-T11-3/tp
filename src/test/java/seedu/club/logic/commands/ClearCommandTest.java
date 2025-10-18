package seedu.club.logic.commands;

import static seedu.club.testutil.TypicalMembers.getTypicalClubBook;

import org.junit.jupiter.api.Test;

import seedu.club.model.ClubBook;
import seedu.club.model.Model;
import seedu.club.model.ModelManager;
import seedu.club.model.UserPrefs;

public class ClearCommandTest {

    @Test
    public void execute_emptyAddressBook_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        CommandTestUtil.assertMemberCommandSuccess(
                new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyAddressBook_success() {
        Model model = new ModelManager(getTypicalClubBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalClubBook(), new UserPrefs());
        expectedModel.setClubBook(new ClubBook());

        CommandTestUtil.assertMemberCommandSuccess(
                new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

}
