package seedu.project.ui;

import static guitests.guihandles.WebViewUtil.waitUntilBrowserLoaded;
import static org.junit.Assert.assertEquals;
import static seedu.project.testutil.TypicalTasks.CS2101_MILESTONE;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.BrowserPanelHandle;
import javafx.beans.property.SimpleObjectProperty;
import seedu.project.model.project.Project;
import seedu.project.model.task.Task;

public class BrowserPanelTest extends GuiUnitTest {
    private SimpleObjectProperty<Project> selectedProject = new SimpleObjectProperty<>();
    private SimpleObjectProperty<Task> selectedTask = new SimpleObjectProperty<>();
    private BrowserPanel browserPanel;
    private BrowserPanelHandle browserPanelHandle;

    @Before
    public void setUp() {
        guiRobot.interact(() -> browserPanel = new BrowserPanel(selectedProject, selectedTask));
        uiPartRule.setUiPart(browserPanel);

        browserPanelHandle = new BrowserPanelHandle(browserPanel.getRoot());
    }

    @Test
    public void display() throws Exception {
        // default web page
        assertEquals(BrowserPanel.DEFAULT_PAGE, browserPanelHandle.getLoadedUrl());

        // associated web page of a task
        guiRobot.interact(() -> selectedTask.set(CS2101_MILESTONE));
        URL expectedTaskUrl = new URL(BrowserPanel.DEFAULT_PAGE.toString());

        waitUntilBrowserLoaded(browserPanelHandle);
        assertEquals(expectedTaskUrl, browserPanelHandle.getLoadedUrl());
    }
}
