package guitests.guihandles;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javafx.scene.Node;
import javafx.scene.control.ListView;
import seedu.project.model.project.Project;

/**
 * Provides a handle for {@code ProjectListPanel} containing the list of {@code ProjectCard}.
 */
public class ProjectListPanelHandle extends NodeHandle<ListView<Project>> {
    public static final String PROJECT_LIST_VIEW_ID = "#projectListView";

    private static final String CARD_PANE_ID = "#projectCardPane";

    private Optional<Project> lastRememberedSelectedProjectCard;

    public ProjectListPanelHandle(ListView<Project> projectListPanelNode) {
        super(projectListPanelNode);
    }

    /**
     * Returns a handle to the selected {@code ProjectCardHandle}.
     * A maximum of 1 item can be selected at any time.
     * @throws AssertionError if no card is selected, or more than 1 card is selected.
     * @throws IllegalStateException if the selected card is currently not in the scene graph.
     */
    public ProjectCardHandle getHandleToSelectedCard() {
        List<Project> selectedProjectList = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedProjectList.size() != 1) {
            throw new AssertionError("Project list size expected 1.");
        }

        return getAllCardNodes().stream()
                .map(ProjectCardHandle::new)
                .filter(handle -> handle.equals(selectedProjectList.get(0)))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    /**
     * Returns the index of the selected card.
     */
    public int getSelectedCardIndex() {
        return getRootNode().getSelectionModel().getSelectedIndex();
    }

    /**
     * Returns true if a card is currently selected.
     */
    public boolean isAnyCardSelected() {
        List<Project> selectedCardsList = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedCardsList.size() > 1) {
            throw new AssertionError("Card list size expected 0 or 1.");
        }

        return !selectedCardsList.isEmpty();
    }

    /**
     * Navigates the listview to display {@code Project}.
     */
    public void navigateToCard(Project project) {
        if (!getRootNode().getItems().contains(project)) {
            throw new IllegalArgumentException("Project does not exist.");
        }

        guiRobot.interact(() -> {
            getRootNode().scrollTo(project);
        });
        guiRobot.pauseForHuman();
    }

    /**
     * Navigates the listview to {@code index}.
     */
    public void navigateToCard(int index) {
        if (index < 0 || index >= getRootNode().getItems().size()) {
            throw new IllegalArgumentException("Index is out of bounds.");
        }

        guiRobot.interact(() -> {
            getRootNode().scrollTo(index);
        });
        guiRobot.pauseForHuman();
    }

    /**
     * Selects the {@code ProjectCard} at {@code index} in the list.
     */
    public void select(int index) {
        getRootNode().getSelectionModel().select(index);
    }

    /**
     * Returns the Project card handle of a Project associated with the {@code index} in the list.
     * @throws IllegalStateException if the selected card is currently not in the scene graph.
     */
    public ProjectCardHandle getProjectCardHandle(int index) {
        return getAllCardNodes().stream()
                .map(ProjectCardHandle::new)
                .filter(handle -> handle.equals(getProject(index)))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    private Project getProject(int index) {
        return getRootNode().getItems().get(index);
    }

    /**
     * Returns all card nodes in the scene graph.
     * Card nodes that are visible in the listview are definitely in the scene graph, while some nodes that are not
     * visible in the listview may also be in the scene graph.
     */
    private Set<Node> getAllCardNodes() {
        return guiRobot.lookup(CARD_PANE_ID).queryAll();
    }

    /**
     * Remembers the selected {@code ProjectCard} in the list.
     */
    public void rememberSelectedProjectCard() {
        List<Project> selectedItems = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedItems.size() == 0) {
            lastRememberedSelectedProjectCard = Optional.empty();
        } else {
            lastRememberedSelectedProjectCard = Optional.of(selectedItems.get(0));
        }
    }

    /**
     * Returns true if the selected {@code ProjectCard} is different from the value remembered by the most recent
     * {@code rememberSelectedProjectCard()} call.
     */
    public boolean isSelectedProjectCardChanged() {
        List<Project> selectedItems = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedItems.size() == 0) {
            return lastRememberedSelectedProjectCard.isPresent();
        } else {
            return !lastRememberedSelectedProjectCard.isPresent()
                    || !lastRememberedSelectedProjectCard.get().equals(selectedItems.get(0));
        }
    }

    /**
     * Returns the size of the list.
     */
    public int getListSize() {
        return getRootNode().getItems().size();
    }
}
