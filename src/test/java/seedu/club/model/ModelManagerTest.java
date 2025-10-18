package seedu.club.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.club.model.Model.PREDICATE_SHOW_ALL_EVENTS;
import static seedu.club.model.Model.PREDICATE_SHOW_ALL_MEMBERS;
import static seedu.club.testutil.Assert.assertThrows;
import static seedu.club.testutil.TypicalEvents.MOVIE_NIGHT;
import static seedu.club.testutil.TypicalEvents.ORIENTATION;
import static seedu.club.testutil.TypicalMembers.ALICE;
import static seedu.club.testutil.TypicalMembers.BENSON;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.club.commons.core.GuiSettings;
import seedu.club.model.name.NameContainsKeywordsPredicate;
import seedu.club.testutil.ClubBookBuilder;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new ClubBook(), new ClubBook(modelManager.getClubBook()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setClubBookFilePath(Paths.get("club/book/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setClubBookFilePath(Paths.get("new/club/book/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setClubBookFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setClubBookFilePath(null));
    }

    @Test
    public void setClubBookFilePath_validPath_setsClubBookFilePath() {
        Path path = Paths.get("club/book/file/path");
        modelManager.setClubBookFilePath(path);
        assertEquals(path, modelManager.getClubBookFilePath());
    }

    //=========== Member =============================================================
    @Test
    public void hasMember_nullMember_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasMember(null));
    }

    @Test
    public void hasMember_memberNotInClubBook_returnsFalse() {
        assertFalse(modelManager.hasMember(ALICE));
    }

    @Test
    public void hasMember_memberInClubBook_returnsTrue() {
        modelManager.addMember(ALICE);
        assertTrue(modelManager.hasMember(ALICE));
    }

    //=========== Filtered List =============================================================
    @Test
    public void getFilteredList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredMemberList().remove(0));
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredEventList().remove(0));
    }

    @Test
    public void member_equals() {
        ClubBook clubBook = new ClubBookBuilder().withMember(ALICE).withMember(BENSON).build();
        ClubBook differentClubBook = new ClubBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(clubBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(clubBook, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different clubBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentClubBook, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredMemberList(new NameContainsKeywordsPredicate<>(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(clubBook, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredMemberList(PREDICATE_SHOW_ALL_MEMBERS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setClubBookFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(clubBook, differentUserPrefs)));
    }

    //=========== Event =============================================================
    @Test
    public void hasEvent_nullEvent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasEvent(null));
    }

    @Test
    public void hasEvent_eventNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasEvent(ORIENTATION));
    }

    @Test
    public void hasEvent_eventInAddressBook_returnsTrue() {
        modelManager.addEvent(ORIENTATION);
        assertTrue(modelManager.hasEvent(ORIENTATION));
    }

    @Test
    public void deleteEvent_eventNotInAddressBook_returnsTrue() {
        modelManager.addEvent(ORIENTATION);
        assertTrue(modelManager.hasEvent(ORIENTATION));

        modelManager.deleteEvent(ORIENTATION);
        assertFalse(modelManager.hasEvent(ORIENTATION));
    }

    @Test
    public void event_equals() {
        ClubBook clubBook = new ClubBookBuilder().withEvent(ORIENTATION).withEvent(MOVIE_NIGHT).build();
        ClubBook differentClubBook = new ClubBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(clubBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(clubBook, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentClubBook, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ORIENTATION.getName().fullName.split("\\s+");
        modelManager.updateFilteredEventList(new NameContainsKeywordsPredicate<>(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(clubBook, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredEventList(PREDICATE_SHOW_ALL_EVENTS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setClubBookFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(clubBook, differentUserPrefs)));
    }
}
