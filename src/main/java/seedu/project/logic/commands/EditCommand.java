package seedu.project.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.project.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.project.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.project.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.project.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.project.model.Model.PREDICATE_SHOW_ALL_PROJECTS;
import static seedu.project.model.Model.PREDICATE_SHOW_ALL_TASKS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.project.commons.core.Messages;
import seedu.project.commons.core.index.Index;
import seedu.project.commons.util.CollectionUtil;
import seedu.project.logic.CommandHistory;
import seedu.project.logic.LogicManager;
import seedu.project.logic.commands.exceptions.CommandException;
import seedu.project.model.Model;
import seedu.project.model.Name;
import seedu.project.model.project.Project;
import seedu.project.model.tag.Tag;
import seedu.project.model.task.Deadline;
import seedu.project.model.task.Description;
import seedu.project.model.task.Task;

/**
 * Edits the details of an existing task in the project.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";
    public static final String COMMAND_ALIAS = "e";

    public static final String PROJECT_MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the project identified "
            + "by the index number used in the displayed project list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) " + "[" + PREFIX_NAME + "NAME]...\n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_NAME + "CS2113T Project";
    public static final String TASK_MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the task identified "
            + "by the index number used in the displayed task list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) " + "[" + PREFIX_NAME + "NAME] " + "["
            + PREFIX_DESCRIPTION + "DESCRIPTION] " + "[" + PREFIX_DEADLINE + "DEADLINE] " + "[" + PREFIX_TAG
            + "TAG]...\n" + "Example: " + COMMAND_WORD + " 1 " + PREFIX_DESCRIPTION + "Report submission "
            + PREFIX_DEADLINE + "1/1/2011";


    public static final String MESSAGE_EDIT_PROJECT_SUCCESS = "Edited Project: %1$s";
    public static final String MESSAGE_DUPLICATE_PROJECT = "This project already exists in the project list.";
    public static final String MESSAGE_EDIT_TASK_SUCCESS = "Edited Task: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the project.";

    private final Index index;
    private final EditTaskDescriptor editTaskDescriptor;
    private final EditProjectDescriptor editProjectDescriptor;

    /**
     * @param index              of the task in the filtered project list to edit
     * @param editObjectDescriptor details to edit the task with
     */
    public EditCommand(Index index, Object editObjectDescriptor) {
        requireNonNull(index);
        requireNonNull(editObjectDescriptor);

        this.index = index;
        if(editObjectDescriptor instanceof EditProjectDescriptor) {
            this.editTaskDescriptor = null;
            this.editProjectDescriptor = new EditProjectDescriptor((EditProjectDescriptor) editObjectDescriptor);
        } else {
            this.editProjectDescriptor = null;
            this.editTaskDescriptor = new EditTaskDescriptor((EditTaskDescriptor) editObjectDescriptor);
        }
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        if(editProjectDescriptor != null) {
            List<Project> lastShownList = model.getFilteredProjectList();

            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PROJECT_DISPLAYED_INDEX);
            }

            Project projectToEdit = lastShownList.get(index.getZeroBased());
            Project editedProject = createEditedProject(projectToEdit, editProjectDescriptor);

            if (!projectToEdit.isSameProject(editedProject) && model.hasProject(editedProject)) {
                throw new CommandException(MESSAGE_DUPLICATE_PROJECT);
            }

            model.setProject(projectToEdit, editedProject);
            model.updateFilteredProjectList(PREDICATE_SHOW_ALL_PROJECTS);
            model.commitProjectList();
            return new CommandResult(String.format(MESSAGE_EDIT_PROJECT_SUCCESS, editedProject));
        } else {
            List<Task> lastShownList = model.getFilteredTaskList();

            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
            }

            Task taskToEdit = lastShownList.get(index.getZeroBased());
            Task editedTask = createEditedTask(taskToEdit, editTaskDescriptor);

            if (!taskToEdit.isSameTask(editedTask) && model.hasTask(editedTask)) {
                throw new CommandException(MESSAGE_DUPLICATE_TASK);
            }

            model.setTask(taskToEdit, editedTask);
            model.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
            model.commitProject();
            //this will not work if user clicks on a different project while on task level??? lock UI at prev panel
            model.setProject(model.getSelectedProject(), (Project) model.getProject()); //sync project list
            model.commitProjectList();
            return new CommandResult(String.format(MESSAGE_EDIT_TASK_SUCCESS, editedTask));
        }
    }

    /**
     * Creates and returns a {@code Task} with the details of {@code taskToEdit}
     * edited with {@code editTaskDescriptor}.
     */
    private static Project createEditedProject(Project projectToEdit, EditProjectDescriptor editProjectDescriptor) {
        assert projectToEdit != null;

        Name updatedName = editProjectDescriptor.getName().orElse(projectToEdit.getName());
        List<Task> updatedTasks = editProjectDescriptor.getTasks().orElse(projectToEdit.getTaskList());
        Project newProject = new Project(updatedName, updatedTasks);
        return newProject;
    }

    /**
     * Creates and returns a {@code Task} with the details of {@code taskToEdit}
     * edited with {@code editTaskDescriptor}.
     */
    private static Task createEditedTask(Task taskToEdit, EditTaskDescriptor editTaskDescriptor) {
        assert taskToEdit != null;

        Name updatedName = editTaskDescriptor.getName().orElse(taskToEdit.getName());
        Description updatedDescription = editTaskDescriptor.getDescription().orElse(taskToEdit.getDescription());
        Deadline updatedDeadline = editTaskDescriptor.getDeadline().orElse(taskToEdit.getDeadline());
        Set<Tag> updatedTags = editTaskDescriptor.getTags().orElse(taskToEdit.getTags());
        int taskId = taskToEdit.getTaskId();
        Task newTask = new Task(updatedName, updatedDescription, updatedDeadline, updatedTags);
        newTask.updateTaskId(taskId);

        return newTask;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        // state check
        EditCommand e = (EditCommand) other;
        return index.equals(e.index) && editTaskDescriptor.equals(e.editTaskDescriptor);
    }

    /**
     * Stores the details to edit the project with. Each non-empty field value will
     * replace the corresponding field value of the task.
     */
    public static class EditProjectDescriptor {
        private Name name;
        private List<Task> tasks;

        public EditProjectDescriptor() {
        }

        /**
         * Copy constructor.
         */
        public EditProjectDescriptor(EditProjectDescriptor toCopy) {
            setName(toCopy.name);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        /**
         * Sets {@code tasks} to this object's {@code tasks}. A defensive copy of
         * {@code tasks} is used internally.
         */
        public void setTasks(Set<Task> tasks) {
            this.tasks = (tasks != null) ? new ArrayList<>(tasks) : null;
        }

        /**
         * Returns an unmodifiable task set, which throws
         * {@code UnsupportedOperationException} if modification is attempted. Returns
         * {@code Optional#empty()} if {@code tasks} is null.
         */
        public Optional<List<Task>> getTasks() {
            return (tasks != null) ? Optional.of(Collections.unmodifiableList(tasks)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditTaskDescriptor)) {
                return false;
            }

            // state check
            EditProjectDescriptor e = (EditProjectDescriptor) other;
            return getName().equals(e.getName()) && getTasks().equals(e.getTasks());
        }
    }

    /**
     * Stores the details to edit the task with. Each non-empty field value will
     * replace the corresponding field value of the task.
     */
    public static class EditTaskDescriptor {
        private Name name;
        private Description description;
        private Deadline deadline;
        private Set<Tag> tags;

        public EditTaskDescriptor() {
        }

        /**
         * Copy constructor. A defensive copy of {@code tags} is used internally.
         */
        public EditTaskDescriptor(EditTaskDescriptor toCopy) {
            setName(toCopy.name);
            setDescription(toCopy.description);
            setDeadline(toCopy.deadline);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, description, deadline, tags);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setDescription(Description description) {
            this.description = description;
        }

        public Optional<Description> getDescription() {
            return Optional.ofNullable(description);
        }

        public void setDeadline(Deadline deadline) {
            this.deadline = deadline;
        }

        public Optional<Deadline> getDeadline() {
            return Optional.ofNullable(deadline);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}. A defensive copy of
         * {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws
         * {@code UnsupportedOperationException} if modification is attempted. Returns
         * {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditTaskDescriptor)) {
                return false;
            }

            // state check
            EditTaskDescriptor e = (EditTaskDescriptor) other;

            return getName().equals(e.getName()) && getDescription().equals(e.getDescription())
                    && getDeadline().equals(e.getDeadline()) && getTags().equals(e.getTags());
        }
    }
}
