package feed.excel.export;

import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

public class ExcelFeedDescriptor {

    private final Map<String, ExcelColumnDescriptor> columnsMap = new HashMap<>();
    private int columnsCount = 0;

    public ExcelColumnDescriptor addColumn(@NonNull String columnName, @NonNull ExcelColumnDescriptor.CellType type) {
        return addColumn(columnName, type, false);
    }

    public ExcelColumnDescriptor addColumn(@NonNull String columnName, @NonNull ExcelColumnDescriptor.CellType type, boolean required) {
        if (columnsMap.containsKey(columnName)) {
            return null;
        }
        ExcelColumnDescriptor descriptor = new ExcelColumnDescriptor(columnsCount++, type, columnName, required);
        columnsMap.put(columnName, descriptor);
        return descriptor;
    }

    public Map<String, ExcelColumnDescriptor> getColumnsMap() {
        return columnsMap;
    }

    public int getColumnsCount() {
        return columnsCount;
    }
}
