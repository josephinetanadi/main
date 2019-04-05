package guitests.guihandles;

import javafx.scene.control.TextArea;

/**
 * A handler for the {@code BrowserPanel} of the UI
 */
public class BrowserPanelHandle extends NodeHandle<TextArea> {

    public static final String BROWSER_ID = "#browserPanel";

    public BrowserPanelHandle(TextArea browserPanelNode) {
        super(browserPanelNode);
    }

    /**
     * Returns the text in the result display.
     */
    public String getText() {
        return getRootNode().getText();
    }
}
