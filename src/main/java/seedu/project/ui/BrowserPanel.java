package seedu.project.ui;

import static java.util.Objects.requireNonNull;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import seedu.project.commons.core.LogsCenter;

/**
 * The Browser Panel of the App.
 */
public class BrowserPanel extends UiPart<Region> {

    private static final String FXML = "BrowserPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    @FXML
    private TextArea browserPanel;

    public BrowserPanel() {
        super(FXML);
    }

    public void setTaskDetails(String toDisplay) {
        requireNonNull(toDisplay);
        browserPanel.setWrapText(true);
        browserPanel.setText(toDisplay);
    }
}
