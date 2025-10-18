package seedu.club.model.name;

import java.util.List;
import java.util.function.Predicate;

import seedu.club.commons.util.StringUtil;
import seedu.club.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Member}'s {@code Name} matches any of the keywords given.
 */
public class NameContainsKeywordsPredicate<T extends NamedEntity> implements Predicate<T> {
    private final List<String> keywords;

    public NameContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(T entry) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(entry.getName().fullName, keyword));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NameContainsKeywordsPredicate<?> otherNameContainsKeywordsPredicate)) {
            return false;
        }

        return keywords.equals(otherNameContainsKeywordsPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
