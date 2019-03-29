package seedu.project.model.tag;

import static seedu.project.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Set;

import seedu.project.model.Name;

public class GroupTag {
    public static final String MESSAGE_CONSTRAINTS = "GroupTag can take any names, and it should not be blank";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "[^\\s].*";

    public final Name name;
    public final Set<Tag> tags;

    /**
     * Constructs an {@code GroupTag}.
     *
     * @param groupTag A valid groupTag.
     */
    public GroupTag(Name name, Set<Tag> tags) {
        requireAllNonNull(name, tags);
        this.name = name;
        this.tags = tags;
    }

    public Name getName() {
        return name;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    /**
     * Returns true if both tasks of the same name have at least one other identity field that is the same.
     * This defines a weaker notion of equality between two tasks.
     */
    public boolean isSameGroupTag(GroupTag otherGroupTag) {
        if (otherGroupTag == this) {
            return true;
        }

        return otherGroupTag != null
                && otherGroupTag.getName().equals(getName())
                && otherGroupTag.getTags().equals(getTags());
    }

    @Override
    public String toString() {
        return name.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof GroupTag // instanceof handles nulls
                && name.equals(((GroupTag) other).name)); // state check
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
