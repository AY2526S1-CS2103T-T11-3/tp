package seedu.club.logic.commands.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.club.logic.Messages.MESSAGE_EVENTROLE_NAME_NOT_EXIST;
import static seedu.club.logic.Messages.MESSAGE_EVENT_NAME_NOT_EXIST;
import static seedu.club.logic.Messages.MESSAGE_MEMBER_NAME_NOT_EXIST;
import static seedu.club.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.club.logic.commands.event.UnassignEventCommand.MESSAGE_SUCCESS_EVENT;
import static seedu.club.logic.commands.event.UnassignEventCommand.MESSAGE_SUCCESS_EVENT_ROLE;
import static seedu.club.testutil.TypicalEventsWithEventRoles.getTypicalClubBookWithEventRoles;
import static seedu.club.testutil.TypicalIndexes.INDEX_FIRST_EVENT;
import static seedu.club.testutil.TypicalIndexes.INDEX_FIRST_MEMBER;

import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.club.logic.commands.exceptions.CommandException;
import seedu.club.model.Model;
import seedu.club.model.ModelManager;
import seedu.club.model.UserPrefs;
import seedu.club.model.event.Event;
import seedu.club.model.name.Name;
import seedu.club.model.role.EventRole;


public class UnassignEventCommandTest {
    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalClubBookWithEventRoles(), new UserPrefs());
    }

    @Test
    public void execute_eventDoesNotExist_throwsCommandException() {
        Name memberName = new Name("John");
        Name eventName = new Name("Dinner");
        UnassignEventCommand unassignEventCommand = new UnassignEventCommand(eventName, memberName,
                Collections.emptySet());
        assertCommandFailure(unassignEventCommand, model,
                String.format(MESSAGE_EVENT_NAME_NOT_EXIST, eventName));
    }

    @Test
    public void execute_memberDoesNotExist_throwsCommandException() {
        Name memberName = new Name("Johnnyn");
        Name eventName = new Name("Orientation");
        UnassignEventCommand unassignEventCommand = new UnassignEventCommand(eventName, memberName,
                Collections.emptySet());
        assertCommandFailure(unassignEventCommand, model,
                String.format(MESSAGE_MEMBER_NAME_NOT_EXIST, memberName));
    }

    @Test
    public void execute_eventRoleDoesNotExist_throwsCommandException() {
        Name memberName = new Name("John");
        Name eventName = new Name("Orientation");
        Set<EventRole> roles = Set.of(new EventRole("Publicity"));
        UnassignEventCommand unassignEventCommand = new UnassignEventCommand(eventName, memberName,
                roles);
        assertCommandFailure(unassignEventCommand, model,
                String.format(MESSAGE_EVENTROLE_NAME_NOT_EXIST, roles.toString()));
    }

    @Test
    public void equals_thisObject_success() {
        Name memberName = new Name("Jane");
        Name eventName = new Name("Orientation");
        UnassignEventCommand unassignEventCommand = new UnassignEventCommand(eventName, memberName,
                Collections.emptySet());
        assert(unassignEventCommand.equals(unassignEventCommand));
    }

    @Test
    public void equals_differentType_false() {
        Name memberName = new Name("Jane");
        Name eventName = new Name("Orientation");
        UnassignEventCommand unassignEventCommand = new UnassignEventCommand(eventName, memberName,
                Collections.emptySet());
        assert(!unassignEventCommand.equals("Event"));
    }

    @Test
    public void equals_sameNameEventAndRoles_success() {
        Name memberName = new Name("Jane");
        Name eventName = new Name("Orientation");
        Set<EventRole> eventRoles = Set.of(new EventRole("Facilitator"),
                new EventRole("SafetyOfficer"));
        UnassignEventCommand unassignEventCommandOne = new UnassignEventCommand(eventName, memberName,
                eventRoles);
        UnassignEventCommand unassignEventCommandTwo = new UnassignEventCommand(eventName, memberName,
                eventRoles);
        assert(unassignEventCommandOne.equals(unassignEventCommandTwo));
    }
    @Test
    public void execute_unassignEventWithEventRoles_success() throws CommandException {
        EventRole roleToDelete = model
                .getFilteredMemberList()
                .get(INDEX_FIRST_MEMBER.getZeroBased())
                .getEventRoles()
                .iterator().next();

        Name eventName = new Name("Orientation");
        Name memberName = new Name("John");

        UnassignEventCommand unassignEventCommand = new UnassignEventCommand(eventName, memberName,
                Set.of(roleToDelete));
        boolean bool = model
                .getFilteredMemberList()
                .get(INDEX_FIRST_MEMBER.getZeroBased()).getEventRoles().containsAll(Set.of(roleToDelete));
        ModelManager expectedModel = new ModelManager(model.getClubBook(), new UserPrefs());
        String result = unassignEventCommand.execute(model).getFeedbackToUser();
        expectedModel.getClubBook().getMemberList()
                .get(INDEX_FIRST_MEMBER.getZeroBased())
                .removeEventRole(Set.of(roleToDelete));
        expectedModel.updateFilteredMemberList(m -> true);
        expectedModel.updateFilteredEventList(e -> true);
        model.updateFilteredMemberList(m -> true);
        model.updateFilteredEventList(e -> true);
        assertEquals(MESSAGE_SUCCESS_EVENT_ROLE, result);
        assertEquals(model, expectedModel);
    }

    @Test
    public void execute_unassignEventWithNoEventRoles_success() throws CommandException {
        Name eventName = new Name("Movie Night");
        Name memberName = new Name("Jane");

        Event eventToDelete = model.getFilteredEventList().get(INDEX_FIRST_EVENT.getZeroBased());


        UnassignEventCommand unassignEventCommand = new UnassignEventCommand(eventName, memberName,
                Collections.emptySet());
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
