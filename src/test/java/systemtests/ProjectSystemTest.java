package systemtests;

import static guitests.guihandles.WebViewUtil.waitUntilBrowserLoaded;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.project.ui.StatusBarFooter.SYNC_STATUS_INITIAL;
import static seedu.project.ui.StatusBarFooter.SYNC_STATUS_UPDATED;
import static seedu.project.ui.testutil.GuiTestAssert.assertListMatching;

import guitests.guihandles.ProjectListPanelHandle;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;

import guitests.guihandles.BrowserPanelHandle;
import guitests.guihandles.CommandBoxHandle;
import guitests.guihandles.MainMenuHandle;
import guitests.guihandles.MainWindowHandle;
import guitests.guihandles.ResultDisplayHandle;
import guitests.guihandles.StatusBarFooterHandle;
import guitests.guihandles.TaskListPanelHandle;
import seedu.project.TestApp;
import seedu.project.commons.core.index.Index;
import seedu.project.logic.commands.ClearCommand;
import seedu.project.logic.commands.FindCommand;
import seedu.project.logic.commands.ListCommand;
import seedu.project.logic.commands.SelectCommand;
import seedu.project.model.Model;
import seedu.project.model.ProjectList;
import seedu.project.model.project.Project;
import seedu.project.testutil.TypicalTasks;
import seedu.project.ui.BrowserPanel;
import seedu.project.ui.CommandBox;

/**
 * A system test class for Project, which provides access to handles of GUI components and helper methods
 * for test verification.
 */
public abstract class ProjectSystemTest {
    @ClassRule
    public static ClockRule clockRule = new ClockRule();

    private static final List<String> COMMAND_BOX_DEFAULT_STYLE = Arrays.asList("text-input", "text-field");
    private static final List<String> COMMAND_BOX_ERROR_STYLE =
            Arrays.asList("text-input", "text-field", CommandBox.ERROR_STYLE_CLASS);

    private MainWindowHandle mainWindowHandle;
    private TestApp testApp;
    private SystemTestSetupHelper setupHelper;

    @BeforeClass
    public static void setupBeforeClass() {
        SystemTestSetupHelper.initialize();
    }

    @Before
    public void setUp() {
        setupHelper = new SystemTestSetupHelper();
        testApp = setupHelper.setupApplication(this::getInitialProjectList, getProjectListSaveLocation());
        mainWindowHandle = setupHelper.setupMainWindowHandle();

        waitUntilBrowserLoaded(getBrowserPanel());
        assertApplicationStartingStateIsCorrect();
    }

    @After
    public void tearDown() {
        setupHelper.tearDownStage();
    }

    /**
     * Returns the data to be loaded into the file in {@link #getProjectListSaveLocation()}.
     */
    protected ProjectList getInitialProjectList() {
        return TypicalTasks.getTypicalProjectList();
    }

    /**
     * Returns the directory of the project list file.
     */
    protected Path getProjectListSaveLocation() {
        return TestApp.SAVE_LOCATION_FOR_TESTING;
    }

    public MainWindowHandle getMainWindowHandle() {
        return mainWindowHandle;
    }

    public CommandBoxHandle getCommandBox() {
        return mainWindowHandle.getCommandBox();
    }

    public ProjectListPanelHandle getProjectListPanel() { return mainWindowHandle.getProjectListPanel(); }

    public TaskListPanelHandle getTaskListPanel() {
        return mainWindowHandle.getTaskListPanel();
    }

    public MainMenuHandle getMainMenu() {
        return mainWindowHandle.getMainMenu();
    }

    public BrowserPanelHandle getBrowserPanel() {
        return mainWindowHandle.getBrowserPanel();
    }

    public StatusBarFooterHandle getStatusBarFooter() {
        return mainWindowHandle.getStatusBarFooter();
    }

    public ResultDisplayHandle getResultDisplay() {
        return mainWindowHandle.getResultDisplay();
    }

    /**
     * Executes {@code command} in the application's {@code CommandBox}.
     * Method returns after UI components have been updated.
     */
    protected void executeCommand(String command) {
        rememberStates();
        // Injects a fixed clock before executing a command so that the time stamp shown in the status bar
        // after each command is predictable and also different from the previous command.
        clockRule.setInjectedClockToCurrentTime();

        mainWindowHandle.getCommandBox().run(command);

        mainWindowHandle.refreshMainWindow();

        waitUntilBrowserLoaded(getBrowserPanel());
    }

    /**
     * Displays all tasks in the project.
     */
    protected void showAllTasks() {
        executeCommand(ListCommand.COMMAND_WORD);
        assertEquals(getModel().getProject().getTaskList().size(), getModel().getFilteredTaskList().size());
    }

    /**
     * Displays all tasks with any parts of their names matching {@code keyword} (case-insensitive).
     */
    protected void showTasksWithName(String keyword) {
        executeCommand(FindCommand.COMMAND_WORD + " " + keyword);
        assertTrue(getModel().getFilteredTaskList().size() < getModel().getProject().getTaskList().size());
    }

    /**
     * Selects the task at {@code index} of the displayed list.
     */
    protected void selectProject(Index index) {
        executeCommand(SelectCommand.COMMAND_WORD + " " + index.getOneBased());
        assertEquals(index.getZeroBased(), getProjectListPanel().getSelectedCardIndex());
    }

    /**
     * Selects the task at {@code index} of the displayed list.
     */
    protected void selectTask(Index index) {
        executeCommand(SelectCommand.COMMAND_WORD + " " + index.getOneBased());
        assertEquals(index.getZeroBased(), getTaskListPanel().getSelectedCardIndex());
    }

    /**
     * Deletes all tasks in the project.
     */
    protected void deleteAllTasks() {
        executeCommand(ClearCommand.COMMAND_WORD);
        assertEquals(0, getModel().getProject().getTaskList().size());
    }

    /**
     * Asserts that the {@code CommandBox} displays {@code expectedCommandInput}, the {@code ResultDisplay} displays
     * {@code expectedResultMessage}, the storage contains the same task objects as {@code expectedModel}
     * and the task list panel displays the tasks in the model correctly.
     */
    protected void assertApplicationDisplaysExpected(String expectedCommandInput, String expectedResultMessage,
                                                            Model expectedModel) {
        assertEquals(expectedCommandInput, getCommandBox().getInput());
        assertEquals(expectedResultMessage, getResultDisplay().getText());
        assertEquals(new ProjectList(expectedModel.getProjectList()), testApp.readStorageProjectList());
        assertListMatching(getTaskListPanel(), expectedModel.getFilteredTaskList());
    }

    /**
     * Calls {@code BrowserPanelHandle}, {@code TaskListPanelHandle} and {@code StatusBarFooterHandle} to remember
     * their current state.
     */
    private void rememberStates() {
        StatusBarFooterHandle statusBarFooterHandle = getStatusBarFooter();
        getBrowserPanel().rememberUrl();
        statusBarFooterHandle.rememberSaveLocation();
        statusBarFooterHandle.rememberSyncStatus();
        getTaskListPanel().rememberSelectedTaskCard();
    }

    /**
     * Asserts that the previously selected card is now deselected and the browser's url is now displaying the
     * default page.
     * @see BrowserPanelHandle#isUrlChanged()
     */
    protected void assertSelectedCardDeselected() {
        assertEquals(BrowserPanel.DEFAULT_PAGE, getBrowserPanel().getLoadedUrl());
        assertFalse(getTaskListPanel().isAnyCardSelected());
    }

    /**
     * Asserts that the browser's url is changed to display the details of the task in the task list panel at
     * {@code expectedSelectedCardIndex}, and only the card at {@code expectedSelectedCardIndex} is selected.
     * @see BrowserPanelHandle#isUrlChanged()
     * @see TaskListPanelHandle#isSelectedTaskCardChanged()
     */
    protected void assertSelectedCardChanged(Index expectedSelectedCardIndex) {
        getTaskListPanel().navigateToCard(getTaskListPanel().getSelectedCardIndex());
        String selectedCardName = getTaskListPanel().getHandleToSelectedCard().getName();
        URL expectedUrl;
        try {
            expectedUrl = new URL(BrowserPanel.SEARCH_PAGE_URL + selectedCardName.replaceAll(" ", "%20"));
        } catch (MalformedURLException mue) {
            throw new AssertionError("URL expected to be valid.", mue);
        }
        assertEquals(expectedUrl, getBrowserPanel().getLoadedUrl());

        assertEquals(expectedSelectedCardIndex.getZeroBased(), getTaskListPanel().getSelectedCardIndex());
    }

    /**
     * Asserts that the browser's url and the selected card in the task list panel remain unchanged.
     * @see BrowserPanelHandle#isUrlChanged()
     * @see TaskListPanelHandle#isSelectedTaskCardChanged()
     */
    protected void assertSelectedCardUnchanged() {
        assertFalse(getBrowserPanel().isUrlChanged());
        assertFalse(getTaskListPanel().isSelectedTaskCardChanged());
    }

    /**
     * Asserts that the command box's shows the default style.
     */
    protected void assertCommandBoxShowsDefaultStyle() {
        assertEquals(COMMAND_BOX_DEFAULT_STYLE, getCommandBox().getStyleClass());
    }

    /**
     * Asserts that the command box's shows the error style.
     */
    protected void assertCommandBoxShowsErrorStyle() {
        assertEquals(COMMAND_BOX_ERROR_STYLE, getCommandBox().getStyleClass());
    }

    /**
     * Asserts that the entire status bar remains the same.
     */
    protected void assertStatusBarUnchanged() {
        StatusBarFooterHandle handle = getStatusBarFooter();
        assertFalse(handle.isSaveLocationChanged());
        assertFalse(handle.isSyncStatusChanged());
    }

    /**
     * Asserts that only the sync status in the status bar was changed to the timing of
     * {@code ClockRule#getInjectedClock()}, while the save location remains the same.
     */
    protected void assertStatusBarUnchangedExceptSyncStatus() {
        StatusBarFooterHandle handle = getStatusBarFooter();
        String timestamp = new Date(clockRule.getInjectedClock().millis()).toString();
        String expectedSyncStatus = String.format(SYNC_STATUS_UPDATED, timestamp);
        assertEquals(expectedSyncStatus, handle.getSyncStatus());
        assertFalse(handle.isSaveLocationChanged());
    }

    /**
     * Asserts that the starting state of the application is correct.
     */
    private void assertApplicationStartingStateIsCorrect() {
        assertEquals("", getCommandBox().getInput());
        assertEquals("", getResultDisplay().getText());
        assertListMatching(getProjectListPanel(), getModel().getFilteredProjectList());
        assertListMatching(getTaskListPanel(), getModel().getFilteredTaskList());
        assertEquals(BrowserPanel.DEFAULT_PAGE, getBrowserPanel().getLoadedUrl());
        assertEquals(Paths.get(".").resolve(testApp.getProjectListSaveLocation()).toString(),
                getStatusBarFooter().getSaveLocation());
        assertEquals(SYNC_STATUS_INITIAL, getStatusBarFooter().getSyncStatus());
    }

    /**
     * Returns a defensive copy of the current model.
     */
    protected Model getModel() {
        return testApp.getModel();
    }
}
