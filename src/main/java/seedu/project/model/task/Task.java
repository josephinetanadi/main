package seedu.project.model.task;

import static seedu.project.commons.util.CollectionUtil.requireAllNonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Date;
import java.util.Set;

import seedu.project.model.Name;
import seedu.project.model.tag.Tag;

/**
 * Represents a Task in the project.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Task implements Comparable<Task> {

    private static int numberOfTask = 0;

    // Identity fields
    private final Name name;
    private final Description description;
    private final Deadline deadline;

    // Data fields
    private Set<Tag> tags = new HashSet<>();

    private int taskId;

    /**
     * Every field must be present and not null.
     */
    public Task(Name name, Description description, Deadline deadline, Set<Tag> tags) {
        requireAllNonNull(name, description, deadline, tags);
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.tags.addAll(tags);

        //numberOfTask++;
        this.taskId = hashCode();
    }

    /**
     * Converts strings to the date type
     */
    private Date stringToDate(String s) {

        Date result = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY");
            result = dateFormat.parse(s);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Comparator function for tasks
     */
    public int compareTo(Task anotherTask) {
        String thisDeadline = (this.getDeadline()).toString();
        String compareDeadline = (anotherTask.getDeadline()).toString();

        Date thisTaskDate = stringToDate(thisDeadline);
        Date compareTaskDate = stringToDate(compareDeadline);

        if (thisTaskDate.after(compareTaskDate)) {
            return 1;
        }
        else if (compareTaskDate.after(thisTaskDate)) {
            return -1;
        }
        else {
            return 0;
        }
    }

    public int getTaskId() {
        return taskId;
    }

    public void updateTaskId(int newTaskId) {
        this.taskId = newTaskId;
    }

    public int getNumberOfTask() {
        return numberOfTask;
    }

    public Name getName() {
        return name;
    }

    public Description getDescription() {
        return description;
    }

    public Deadline getDeadline() {
        return deadline;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

    /**
     * Returns true if both tasks of the same name have at least one other identity field that is the same.
     * This defines a weaker notion of equality between two tasks.
     */
    public boolean isSameTask(Task otherTask) {
        if (otherTask == this) {
            return true;
        }

        return otherTask != null
                && otherTask.getName().equals(getName()) && (otherTask.getDescription().equals(getDescription())
                || otherTask.getDeadline().equals(getDeadline()));
    }

    /**
     * Returns true if both tasks have the same identity and data fields.
     * This defines a stronger notion of equality between two tasks.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Task)) {
            return false;
        }

        Task otherTask = (Task) other;
        return otherTask.getName().equals(getName())
                && otherTask.getDescription().equals(getDescription())
                && otherTask.getDeadline().equals(getDeadline())
                && otherTask.getTags().equals(getTags());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, description, deadline, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Description: ")
                .append(getDescription())
                .append(" Deadline: ")
                .append(getDeadline())
                .append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }

}
