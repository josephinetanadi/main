package seedu.project.model.task;

import static java.util.Objects.requireNonNull;
import static seedu.project.commons.util.AppUtil.checkArgument;

/**
 * Represents a Task's deadline in the project.
 * Guarantees: immutable; is valid as declared in {@link #isValidDeadline(String)}
 */
public class Deadline {

    public static final String MESSAGE_CONSTRAINTS = "Deadline can take any values in the"
            + " form of DD-MM-YYYY and it should not be blank \n"
            + "DD -> 01 - 30 for Apr / Jun / Sep / Nov  \n"
            + "DD -> 01 - 31 for Jan / Mar / May / Jul / Aug / Oct / Dec\n"
            + "DD -> 01 - 29 for Feb 2020 \n"
            + "DD -> 01 - 28 for Feb 2019 / 2021\n"
            + "MM -> 01 - 12 \n"
            + "YYYY -> 2019 - 2021 \n";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX =
            "^(3[0]|[12][0-9]|0[1-9])-(11|0[469])-(20[1][9]|202[01])$|"
                    + "^(3[1]|[12][0-9]|0[1-9])-(1[02]|0[13578])-(20[1][9]|202[01])$|"
                    + "^(2[0-8]|[1][0-9]|0[1-9])-(02)-(2019|2021)$|"
                    + "^(2[0-9]|[1][0-9]|0[1-9])-(02)-(2020)$";

    public final String value;

    /**
     * Constructs an {@code Deadline}.
     *
     * @param deadline A valid deadline.
     */
    public Deadline(String deadline) {
        requireNonNull(deadline);
        checkArgument(isValidDeadline(deadline), MESSAGE_CONSTRAINTS);
        value = deadline;
    }

    /**
     * Returns true if a given string is a valid email.
     */
    public static boolean isValidDeadline(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Deadline // instanceof handles nulls
                && value.equals(((Deadline) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
