package seedu.club.model.name;

import static java.util.Objects.requireNonNull;

/**
 * Represents an entry's name in the club book.
 */
public abstract class NamedEntity {
    protected final Name name;

    /**
     * Constructs a {@code NamedEntity}.
     *
     * @param name A valid {@code Name}
     */
    public NamedEntity(Name name) {
        requireNonNull(name);
        this.name = name;
    }

    public Name getName() {
        return name;
    };
}
