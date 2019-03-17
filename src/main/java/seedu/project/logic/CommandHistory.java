package seedu.project.logic;

import static java.util.Objects.requireNonNull;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Stores the history of commands executed.
 */
public class CommandHistory {
    private final ObservableList<String> userInputHistory = FXCollections.observableArrayList();
    private final ObservableList<String> unmodifiableUserInputHistory =
            FXCollections.unmodifiableObservableList(userInputHistory);
    private final ObservableList<String> userInputHistoryTaskId = FXCollections.observableArrayList();
    private final ObservableList<String> unmodifiableUserInputHistoryTaskId =
            FXCollections.unmodifiableObservableList(userInputHistoryTaskId);

    public CommandHistory() {}

    public CommandHistory(CommandHistory commandHistory) {
        userInputHistory.addAll(commandHistory.userInputHistory);
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

        while (historyTaskIdSize <= historySize) {
            userInputHistoryTaskId.add("0");
            historyTaskIdSize = userInputHistoryTaskId.size();
        }

        userInputHistoryTaskId.add(userInput);
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
