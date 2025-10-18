package seedu.club.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.club.storage.JsonAdaptedEvent.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.club.testutil.Assert.assertThrows;
import static seedu.club.testutil.TypicalEvents.ORIENTATION;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.club.commons.exceptions.IllegalValueException;
import seedu.club.model.name.Name;

public class JsonAdaptedEventTest {
    private static final String INVALID_NAME = "@orientation";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = ORIENTATION.getName().toString();
    private static final String VALID_FROM = ORIENTATION.getFrom();
    private static final String VALID_TO = ORIENTATION.getTo();
    private static final String VALID_DETAILS = ORIENTATION.getDetail();
    private static final List<JsonAdaptedTag> VALID_ROLES = ORIENTATION.getRoles().stream()
                            .map(JsonAdaptedTag::new)
                            .collect(Collectors.toList());


    @Test
    public void toModelType_validEvent_returnsEvent() throws Exception {
        JsonAdaptedEvent event = new JsonAdaptedEvent(ORIENTATION);
        assertEquals(ORIENTATION, event.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedEvent event = new JsonAdaptedEvent(INVALID_NAME, VALID_FROM, VALID_TO, VALID_DETAILS, VALID_ROLES);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, event::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedEvent event = new JsonAdaptedEvent(null, VALID_FROM, VALID_TO, VALID_DETAILS, VALID_ROLES);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, event::toModelType);
    }

    @Test
    public void toModelType_nullFrom_throwsIllegalValueException() {
        JsonAdaptedEvent event = new JsonAdaptedEvent(VALID_NAME, null, VALID_TO, VALID_DETAILS, VALID_ROLES);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, String.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, event::toModelType);
    }

    @Test
    public void toModelType_nullTo_throwsIllegalValueException() {
        JsonAdaptedEvent event = new JsonAdaptedEvent(VALID_NAME, VALID_FROM, null, VALID_DETAILS, VALID_ROLES);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, String.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, event::toModelType);
    }

    @Test
    public void toModelType_nullDetails_throwsIllegalValueException() {
        JsonAdaptedEvent event = new JsonAdaptedEvent(VALID_NAME, VALID_FROM, VALID_TO, null, VALID_ROLES);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, String.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, event::toModelType);
    }

    @Test
    public void toModelType_invalidRoles_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidRoles = new ArrayList<>(VALID_ROLES);
        invalidRoles.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedEvent event = new JsonAdaptedEvent(VALID_NAME, VALID_FROM, VALID_TO, VALID_DETAILS, invalidRoles);
        assertThrows(IllegalValueException.class, event::toModelType);
    }
}
