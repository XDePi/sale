package imports;

import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Data
public class ImportMappingReport {

    private String stackTrace;
    private int rowsProcessed;
    private int mappingRowsCreated;
    private List<ReportMessage> reportMessages = new ArrayList<>();

    public int getMessagesCount(@NonNull ReportMessage.Level level) {
        int counter = 0;
        for (ReportMessage reportMessage : reportMessages) {
            if (level.equals(reportMessage.getLevel())) {
                counter = counter + 1;
            }
        }
        return counter;
    }

    public static ImportMappingReport create(int rowsProcessed, int mappingRowsCreated, List<ReportMessage> reportMessages) {
        ImportMappingReport report = new ImportMappingReport();
        report.setRowsProcessed(rowsProcessed);
        report.setMappingRowsCreated(mappingRowsCreated);
        report.setReportMessages(reportMessages);
        return report;
    }
}
