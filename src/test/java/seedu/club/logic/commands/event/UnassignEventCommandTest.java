package seedu.club.logic.commands.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.club.logic.Messages.MESSAGE_EVENT_NAME_NOT_EXIST;
import static seedu.club.logic.Messages.MESSAGE_MEMBER_NAME_NOT_EXIST;
import static seedu.club.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.club.logic.commands.event.UnassignEventCommand.MESSAGE_SUCCESS_EVENT;
import static seedu.club.testutil.TypicalClubBook.getTypicalClubBook;
import static seedu.club.testutil.TypicalIndexes.INDEX_FIRST_EVENT;
import static seedu.club.testutil.TypicalIndexes.INDEX_FIRST_MEMBER;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.club.logic.commands.exceptions.CommandException;
import seedu.club.model.Model;
import seedu.club.model.ModelManager;
import seedu.club.model.UserPrefs;
import seedu.club.model.event.Event;
import seedu.club.model.member.Member;
import seedu.club.model.name.Name;


public class UnassignEventCommandTest {
    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalClubBook(), new UserPrefs());
    }

    @Test
    public void execute_eventDoesNotExist_throwsCommandException() {
        Name memberName = new Name("Alice Pauline");
        Name eventName = new Name("Dinner");
        UnassignEventCommand unassignEventCommand = new UnassignEventCommand(eventName, memberName);
        assertCommandFailure(unassignEventCommand, model,
                String.format(MESSAGE_EVENT_NAME_NOT_EXIST, eventName));
    }

    @Test
    public void execute_memberDoesNotExist_throwsCommandException() {
        Name memberName = new Name("Alicee");
        Name eventName = new Name("Orientation");
        UnassignEventCommand unassignEventCommand = new UnassignEventCommand(eventName, memberName);
        assertCommandFailure(unassignEventCommand, model,
                String.format(MESSAGE_MEMBER_NAME_NOT_EXIST, memberName));
    }


    @Test
    public void equals_thisObject_success() {
        Name memberName = new Name("Benson");
        Name eventName = new Name("Orientation");
        UnassignEventCommand unassignEventCommand = new UnassignEventCommand(eventName, memberName);
        assert(unassignEventCommand.equals(unassignEventCommand));
    }

    @Test
    public void equals_sameNameAndEvent_success() {
        Name memberName = new Name("Alice");
        Name eventName = new Name("Orientation");
        UnassignEventCommand unassignEventCommandOne = new UnassignEventCommand(eventName, memberName);
        UnassignEventCommand unassignEventCommandTwo = new UnassignEventCommand(eventName, memberName);
        assert(unassignEventCommandOne.equals(unassignEventCommandTwo));
    }

    @Test
    public void equals_differentType_false() {
        Name memberName = new Name("Alice");
        Name eventName = new Name("Orientation");
        UnassignEventCommand unassignEventCommand = new UnassignEventCommand(eventName, memberName);
        assert(!unassignEventCommand.equals("Event"));
    }



    @Test
    public void execute_unassignEventWithNoEventRoles_success() throws CommandException {
        Name eventName = new Name("Orientation");
        Name memberName = new Name("Alice Pauline");

        Event eventToDelete = model.getFilteredEventList().get(INDEX_FIRST_EVENT.getZeroBased());
        Member member = model.getFilteredMemberList().get(0);
        eventToDelete.addMember(member);
        UnassignEventCommand unassignEventCommand = new UnassignEventCommand(eventName, memberName);
        String result = unassignEventCommand.execute(model).getFeedbackToUser();

        ModelManager expectedModel = new ModelManager(model.getClubBook(), new UserPrefs());
        expectedModel.getClubBook()
                .getMemberList().get(INDEX_FIRST_MEMBER.getOneBased())
                .removeEvent(eventToDelete);
        expectedModel.getClubBook()
                .getEventList().get(INDEX_FIRST_EVENT.getOneBased())
                        .removeMemberFromRoster(expectedModel.getClubBook()
                                .getMemberList().get(INDEX_FIRST_MEMBER.getOneBased()));

        expectedModel.updateFilteredMemberList(m -> true);
        expectedModel.updateFilteredEventList(e -> true);
        model.updateFilteredMemberList(m -> true);
        model.updateFilteredEventList(e -> true);
        assertEquals(MESSAGE_SUCCESS_EVENT, result);
        assertEquals(model, expectedModel);
    }
}
