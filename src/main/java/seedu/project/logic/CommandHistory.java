package seedu.project.logic;

import static java.util.Objects.requireNonNull;

import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.project.commons.core.LogsCenter;


/**
 * Stores the history of commands executed.
 */
public class CommandHistory {

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final ObservableList<String> userInputHistory = FXCollections.observableArrayList();
    private final ObservableList<String> unmodifiableUserInputHistory =
            FXCollections.unmodifiableObservableList(userInputHistory);
    private final ObservableList<String> userInputHistoryTaskId = FXCollections.observableArrayList();
    private final ObservableList<String> unmodifiableUserInputHistoryTaskId =
            FXCollections.unmodifiableObservableList(userInputHistoryTaskId);

    public CommandHistory() {}

    public CommandHistory(CommandHistory commandHistory) {
        userInputHistory.addAll(commandHistory.userInputHistory);
        userInputHistoryTaskId.addAll(commandHistory.userInputHistoryTaskId);
    }

    /**
     * Appends {@code userInput} to the list of user input entered.
     */
    public void add(String userInput) {
        requireNonNull(userInput);
        userInputHistory.add(userInput);
    }

    /**
     * Appends {@code userInput} to the list of user input entered.
     */
    public void addHistoryTaskId(String userInput) {
        requireNonNull(userInput);

        int historySize = userInputHistory.size();
        int historyTaskIdSize = userInputHistoryTaskId.size();

        while (historyTaskIdSize < historySize) {
            userInputHistoryTaskId.add("0");
            historyTaskIdSize = userInputHistoryTaskId.size();
        }

        userInputHistoryTaskId.add(userInput);
    }

    /**
     * Clears userInputHistory & userInputHistoryTaskId
     * Used during switching of projects
     */
    public void clearHistory() {
        logger.info("INFO: History cleared");
        userInputHistoryTaskId.clear();
        userInputHistory.clear();
        unmodifiableUserInputHistory.clear();
        unmodifiableUserInputHistoryTaskId.clear();
    }

    /**
     * Returns an unmodifiable view of {@code userInputHistory}.
     */
    public ObservableList<String> getHistory() {
        return unmodifiableUserInputHistory;
    }

    /**
     * Returns an unmodifiable view of {@code userInputHistoryTaskId}.
     */
    public ObservableList<String> getHistoryTaskId() {
        return unmodifiableUserInputHistoryTaskId;
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof CommandHistory)) {
            return false;
        }

        // state check
        CommandHistory other = (CommandHistory) obj;
        return userInputHistory.equals(other.userInputHistory);
    }

    @Override
    public int hashCode() {
        return userInputHistory.hashCode();
    }
}
