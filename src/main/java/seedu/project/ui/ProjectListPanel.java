package seedu.project.ui;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Logger;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.project.commons.core.LogsCenter;
import seedu.project.model.project.Project;
import seedu.project.model.task.Task;

/**
 * Panel containing the list of projects.
 */
public class ProjectListPanel extends UiPart<Region> {
    private static final String FXML = "ProjectListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(TaskListPanel.class);

    @FXML
    private ListView<Project> projectListView;

    public ProjectListPanel(ObservableList<Project> projectList, ObservableValue<Project> selectedProject,
                         Consumer<Project> onSelectedProjectChange) {
        super(FXML);
        projectListView.setItems(projectList);
        projectListView.setCellFactory(listView -> new ProjectListViewCell());
        projectListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            logger.fine("Selection in project list panel changed to : '" + newValue + "'");
            onSelectedProjectChange.accept(newValue);
        });
        selectedProject.addListener((observable, oldValue, newValue) -> {
            logger.fine("Selected project changed to: " + newValue);

            // Don't modify selection if we are already selecting the selected project,
            // otherwise we would have an infinite loop.
            if (Objects.equals(projectListView.getSelectionModel().getSelectedItem(), newValue)) {
                return;
            }

            if (newValue == null) {
                projectListView.getSelectionModel().clearSelection();
            } else {
                int index = projectListView.getItems().indexOf(newValue);
                projectListView.scrollTo(index);
                projectListView.getSelectionModel().clearAndSelect(index);
                //taskListView.setItems(selectedProject.getValue().getTaskList());
            }
        });
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Project} using a {@code ProjectCard}.
     */
    class ProjectListViewCell extends ListCell<Project> {
        @Override
        protected void updateItem(Project project, boolean empty) {
            super.updateItem(project, empty);

            if (empty || project == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new ProjectCard(project, getIndex() + 1).getRoot());
            }
        }
    }

}
