package seedu.project.ui;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.BrowserPanelHandle;

public class BrowserPanelTest extends GuiUnitTest {

    private BrowserPanel browserPanel;
    private BrowserPanelHandle browserPanelHandle;

    @Before
    public void setUp() {
        browserPanel = new BrowserPanel();
        uiPartRule.setUiPart(browserPanel);

        browserPanelHandle = new BrowserPanelHandle(getChildNode(browserPanel.getRoot(),
                BrowserPanelHandle.BROWSER_ID));
    }

    @Test
    public void display() {
        // default result text
        guiRobot.pauseForHuman();
        assertEquals("", browserPanelHandle.getText());

        // new result received
        guiRobot.interact(() -> browserPanel.setTaskDetails("Dummy task details"));
        guiRobot.pauseForHuman();
        assertEquals("Dummy task details", browserPanelHandle.getText());
    }
}
