package seedu.club.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.club.logic.parser.ParserUtil;
import seedu.club.model.event.Event;

/**
 * A UI component that displays information of a {@code Event}.
 */
public class EventCard extends UiPart<Region> {

    private static final String FXML = "EventListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Event event;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label from;
    @FXML
    private Label to;
    @FXML
    private Label details;
    @FXML
    private FlowPane eventRoles;

    /**
     * Creates a {@code EventCode} with the given {@code Event} and index to display.
     */
    public EventCard(Event event, int displayedIndex) {
        super(FXML);
        this.event = event;
        id.setText(displayedIndex + ". ");
        if (displayedIndex == -1) {
            id.setText("");
        }
        name.setText(event.getName().fullName);
        from.setText(ParserUtil.formatDate(event.getFrom()));
        to.setText(ParserUtil.formatDate(event.getTo()));
        details.setText(event.getDetail());
        event.getRoles().stream()
                .sorted(Comparator.comparing(eventRole -> eventRole.roleName))
                .forEach(eventRole -> eventRoles.getChildren().add(new Label(eventRole.roleName)));
    }

    public void setEventRoles(FlowPane eventRoles) {
        this.eventRoles = eventRoles;
    }
}
