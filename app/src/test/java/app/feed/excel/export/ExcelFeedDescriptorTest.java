package app.feed.excel.export;

import feed.excel.export.ExcelColumnDescriptor;
import feed.excel.export.ExcelFeedDescriptor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ExcelFeedDescriptorTest {

    @Test
    public void addColumn() {
        String columnName = "some column name ";
        ExcelFeedDescriptor descriptor = new ExcelFeedDescriptor();

        ExcelColumnDescriptor excelColumnDescriptor = descriptor.addColumn(columnName, ExcelColumnDescriptor.CellType.STRING);

        assertNotNull(excelColumnDescriptor);
        assertEquals(columnName, excelColumnDescriptor.getHeader());
        assertEquals(ExcelColumnDescriptor.CellType.STRING, excelColumnDescriptor.getCellType());
        assertEquals(0, excelColumnDescriptor.getNumber());
        assertEquals(1, descriptor.getColumnsCount());
        assertEquals(1, descriptor.getColumnsMap().size());
        assertEquals(excelColumnDescriptor, descriptor.getColumnsMap().get(columnName));
    }
}
