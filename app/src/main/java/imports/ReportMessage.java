package imports;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.Collection;
import java.util.List;

@EqualsAndHashCode
@ToString
public class ReportMessage {

    public enum Level {
        ERROR,
        WARNING,
        INFO
    }

    @Getter
    private String message;
    private int level;

    public static String combineErrorMessages(Collection<ReportMessage> reportMessages) {
        return combineMessagesByLevel(reportMessages, Level.ERROR);
    }

    public static String combineWarningMessages(List<ReportMessage> reportMessages) {
        return combineMessagesByLevel(reportMessages, Level.WARNING);
    }

    public static String combineInfoMessages(List<ReportMessage> reportMessages) {
        return combineMessagesByLevel(reportMessages, Level.INFO);
    }

    public static boolean hasErrors(@NonNull List<ReportMessage> messages) {
        return hasMessageWithLevel(messages, Level.ERROR);
    }

    public static boolean hasWarnings(@NonNull List<ReportMessage> messages) {
        return hasMessageWithLevel(messages, Level.WARNING);
    }

    public static boolean hasInfos(@NonNull List<ReportMessage> messages) {
        return hasMessageWithLevel(messages, Level.INFO);
    }

    private static boolean hasMessageWithLevel(@NonNull List<ReportMessage> messages, @NonNull Level level) {
        for (ReportMessage reportMessage : messages) {
            if (level.equals(reportMessage.getLevel())) {
                return true;
            }
        }
        return false;
    }

    public Level getLevel() {
        return Level.values()[this.level];
    }

    public ReportMessage(String message, Level level) {
        this.message = message;
        this.level = level.ordinal();
    }

    public ReportMessage(String message) {
        this(message, Level.ERROR);
    }

    public ReportMessage() {

    }

    private static String combineMessagesByLevel(Collection<ReportMessage> reportMessages, Level level) {
        StringBuilder errors = new StringBuilder();
        for (ReportMessage reportMessage : reportMessages) {
            if (level.equals(reportMessage.getLevel())) {
                if (errors.length() > 0) {
                    errors.append(", ");
                }
                errors.append(reportMessage.getMessage());
            }
        }
        return errors.toString();
    }
}
