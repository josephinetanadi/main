package seedu.project.ui;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.util.Date;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.project.logic.LogicManager;
import seedu.project.model.project.ReadOnlyProject;

/**
 * A ui for the status bar that is displayed at the footer of the application.
 */
public class StatusBarFooter extends UiPart<Region> {

    public static final String SYNC_STATUS_INITIAL = "Not updated yet in this session";
    public static final String SYNC_STATUS_UPDATED = "Last Updated: %s";
    public static final String TOTAL_TASKS_STATUS = "%d task(s) total";

    /**
     * Used to generate time stamps.
     *
     * TODO: change clock to an instance variable. We leave it as a static variable
     * because manual dependency injection will require passing down the clock
     * reference all the way from MainApp, but it should be easier once we have
     * factories/DI frameworks.
     */
    private static Clock clock = Clock.systemDefaultZone();

    private static final String FXML = "StatusBarFooter.fxml";

    @FXML
    private Label syncStatus;
    @FXML
    private Label saveLocationStatus;
    @FXML
    private Label totalTasksStatus;

    // private Logic logic;

    public StatusBarFooter(Path saveLocation, ReadOnlyProject project, int totalTasks) {
        super(FXML);
        project.addListener(observable -> updateSyncStatus(project.getTaskList().size()));
        syncStatus.setText(SYNC_STATUS_INITIAL);
        saveLocationStatus.setText(Paths.get(".").resolve(saveLocation).toString());
        if(LogicManager.getState()) { setTotalTasks(totalTasks); }
    }

    private void setTotalTasks(int totalTasks) {
        Platform.runLater(() -> totalTasksStatus.setText(String.format(TOTAL_TASKS_STATUS, totalTasks)));
    }

    /**
     * Sets the clock used to determine the current time.
     */
    public static void setClock(Clock clock) {
        StatusBarFooter.clock = clock;
    }

    /**
     * Returns the clock currently in use.
     */
    public static Clock getClock() {
        return clock;
    }

    /**
     * Updates "last updated" status to the current time.
     */
    private void updateSyncStatus(int tasksListSize) {
        long now = clock.millis();
        String lastUpdated = new Date(now).toString();
        syncStatus.setText(String.format(SYNC_STATUS_UPDATED, lastUpdated));
        setTotalTasks(tasksListSize);
    }

}
