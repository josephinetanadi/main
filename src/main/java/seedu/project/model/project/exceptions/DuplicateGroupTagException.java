package seedu.project.model.project.exceptions;


/**
 * Signals that the operation will result in duplicate Projects (Projects are considered duplicates if they have the
 * same identity).
 */
public class DuplicateGroupTagException extends RuntimeException {
    public DuplicateGroupTagException() {
        super("Operation would result in duplicate group tags");
    }
}
