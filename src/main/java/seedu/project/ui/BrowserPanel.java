package seedu.project.ui;

import static java.util.Objects.requireNonNull;

import java.net.URL;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.web.WebView;
import seedu.project.MainApp;
import seedu.project.commons.core.LogsCenter;
import seedu.project.logic.LogicManager;
import seedu.project.model.project.Project;
import seedu.project.model.task.Task;

/**
 * The Browser Panel of the App.
 */
public class BrowserPanel extends UiPart<Region> {

    public static final URL DEFAULT_PAGE =
            requireNonNull(MainApp.class.getResource(FXML_FILE_FOLDER + "default.html"));
    public static final String SEARCH_PAGE_URL = "https://se-education.org/dummy-search-page/?name=";

    private static final String FXML = "BrowserPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    @FXML
    private WebView browser;

    public BrowserPanel(ObservableValue<Project> selectedProject, ObservableValue<Task> selectedTask) {
        super(FXML);

        // To prevent triggering events for typing inside the loaded Web page.
        getRoot().setOnKeyPressed(Event::consume);

        if(!LogicManager.getState()) {
            // Load project page when selected task changes.
            selectedProject.addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    loadDefaultPage();
                    return;
                }
                loadProjectPage(newValue);
            });
        } else {
            // Load task page when selected task changes.
            selectedTask.addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    loadDefaultPage();
                    return;
                }
                loadTaskPage(newValue);
            });
        }

        loadDefaultPage();
    }

    private void loadTaskPage(Task task) {
        loadPage(SEARCH_PAGE_URL + task.getName().fullName);
    }

    private void loadProjectPage(Project project) {
        loadPage(SEARCH_PAGE_URL + project.getName().fullName);
    }

    public void loadPage(String url) {
        Platform.runLater(() -> browser.getEngine().load(url));
    }

    /**
     * Loads a default HTML file with a background that matches the general theme.
     */
    private void loadDefaultPage() {
        loadPage(DEFAULT_PAGE.toExternalForm());
    }

}
