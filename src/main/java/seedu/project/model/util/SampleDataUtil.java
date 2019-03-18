package seedu.project.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.project.model.ProjectList;
import seedu.project.model.ReadOnlyProjectList;
import seedu.project.model.project.Project;
import seedu.project.model.tag.Tag;
import seedu.project.model.task.Deadline;
import seedu.project.model.task.Description;
import seedu.project.model.task.Name;
import seedu.project.model.task.Task;

/**
 * Contains utility methods for populating {@code Project} with sample data.
 */
public class SampleDataUtil {
    public static Task[] getSampleTasks() {
        return new Task[] {
            new Task(new Name("Submit report"), new Description("submit report to teacher"), new Deadline("1-1-2011"),
                getTagSet("CS2101")),
            new Task(new Name("Submit tutorial"), new Description("submit tutorial 4"), new Deadline("1-1-2011"),
                getTagSet("CS2101")),
            new Task(new Name("Submit assignment"), new Description("submit assignment 2"), new Deadline("1-1-2011"),
                getTagSet("CS2101")),
            new Task(new Name("Submit feedback"), new Description("submit tutor feedback"), new Deadline("1-1-2011"),
                getTagSet("CP2106")),
            new Task(new Name("Submit quiz"), new Description("do quiz at portal"), new Deadline("1-1-2011"),
                getTagSet("CP2106")),
            new Task(new Name("Submit test"), new Description("do test at portal"), new Deadline("1-1-2011"),
                getTagSet("CP2106"))
        };
    }

    public static Project getSampleProject() {
        Project sampleProject = new Project();
        sampleProject.setName("CS2101 Project");
        for (Task sampleTask : getSampleTasks()) {
            sampleProject.addTask(sampleTask);
        }
        return sampleProject;
    }

    public static ReadOnlyProjectList getSampleProjectList() {
        ProjectList sampleProjectList = new ProjectList();
        sampleProjectList.addProject(getSampleProject());
        return sampleProjectList;
    }

    /**
     * Returns a task set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
