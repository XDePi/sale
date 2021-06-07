package feed.excel.export;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static feed.excel.export.ExcelColumnDescriptor.CellType.FLOAT2;

public class ExcelMarshaller extends AbstractMarshaller {

    public static final int MAX_COLUMN_WIDTH = 16000;  //the width in units of 1/256th of a character width

    private static final int FIRST_DATA_ROW = 4;
    private static final int LAST_DATA_ROW = SpreadsheetVersion.EXCEL2007.getMaxRows() - 1;
    private static final short HEADER_HEIGHT = (short) 619;
    private static final short FONT_SIZE = (short) 11;
    public static final int ROW_MAX_HEIGHT = 5;
    private static final short HEADER_FONT_SIZE = (short) 12;

    protected final OutputStream outStream;

    private final SXSSFWorkbook book;
    private final Map<Integer, Integer> autoSize = new HashMap<>();
    private final Map<ExcelColumnDescriptor.CellType, CellStyle> cellStyleMap = new EnumMap<>(ExcelColumnDescriptor.CellType.class);
    private final File tempFile;

    public ExcelMarshaller() throws IOException {
        tempFile = File.createTempFile("exportMappingFile", ".xlsx");
        this.outStream = new FileOutputStream(tempFile);
        book = new SXSSFWorkbook(-1);
    }

    public ExcelMarshaller(File file) throws IOException {
        tempFile = file;
        this.outStream = new FileOutputStream(tempFile);
        book = new SXSSFWorkbook(-1);
    }

    @Override
    public File getFile() {
        return tempFile;
    }

    /**
     * Populate sheet with given name with data <br/>
     * if sheet exists - clean it before populating <br/>
     * <br/>
     * don't forget to call {@link #finalizeWritings()} to flush book closing into output stream <br/>
     * if sheet populated Ok it gets flushed to output stream and sequential attempt to write it will fail
     *
     * @param sheetName            Name of sheet to be filled
     * @param excelContentResolver Resolves data to marshall by Iterator's item
     * @param iterator             Iterates through items to be marshalled
     * @param <ObjectType>         Type of Iterator's items
     * @throws IOException if book finalizing failed
     */
    @Override
    public <ObjectType> void marshallSheet(String sheetName, ExcelContentResolver<ObjectType> excelContentResolver,
                                           Iterator<ObjectType> iterator) throws IOException {
        int rowCounter = 0;
        Sheet sheet = getOrCreateSheet(sheetName);
        ExcelFeedDescriptor descriptor = excelContentResolver.buildDescriptor();
        int columnCount = descriptor.getColumnsCount();
        for (int i = 0; i < columnCount; i++) {
            autoSize.put(i, -1);
        }
        marshalHeader(sheet, descriptor, rowCounter++);
        while (iterator.hasNext()) {
            ObjectType next = iterator.next();
            if (next == null) {
                continue;
            }
            rowCounter += marshalItem(sheet, rowCounter, descriptor, excelContentResolver, next);
        }
        finalizeSheet(sheet, columnCount);
    }

    /**
     * Prints excel document closing clauses to output
     *
     * @throws IOException - rethrows SXSS exception situation
     */
    @Override
    public void finalizeWritings() throws IOException {
        book.write(outStream);
    }

    @Override
    public void close() throws IOException {
        try {
            book.close();
            book.dispose();
        } finally {
            outStream.close();
        }
    }

    private Sheet getOrCreateSheet(String sheetName) {
        Sheet sheet = book.getSheet(sheetName);
        if (sheet == null) {
            return book.createSheet(sheetName);
        }
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            rowIterator.next();
            rowIterator.remove();
        }
        return sheet;
    }

    private CellStyle createHeaderCellStyle() {
        CellStyle headerRequired = createCellStyle();
        headerRequired.setWrapText(true);
        headerRequired.setLocked(true);
        Font font = createDocumentFont(HEADER_FONT_SIZE);
        font.setBold(true);
        font.setFontHeightInPoints(HEADER_FONT_SIZE);
        headerRequired.setFont(font);
        return headerRequired;
    }

    private Row addRow(Sheet sheet, int rowCounter) {
        return sheet.createRow(rowCounter);
    }

    /**
     * returns header cell
     */
    private Cell addColumnHeader(Row headerRow, int counter, CellStyle columnStyle, CellStyle cellStyle, String name) {
        Sheet sheet = headerRow.getSheet();
        sheet.setDefaultColumnStyle(counter, columnStyle);
        Cell cell = headerRow.createCell(counter, CellType.STRING);
        RichTextString richTextString = sheet.getWorkbook().getCreationHelper().createRichTextString(StringUtils.trimToEmpty(name));
        cell.setCellValue(richTextString);
        cell.setCellStyle(cellStyle);
        autoSize.put(counter, -1);
        return cell;
    }

    private CellStyle createCellStyle() {
        return book.createCellStyle();
    }

    private CreationHelper getCreationHelper() {
        return book.getCreationHelper();
    }

    private Font createDocumentFont(short fontSize) {
        Font font = book.createFont();
        font.setFontHeightInPoints(fontSize);
        return font;
    }

    private void marshalHeader(Sheet sheet, ExcelFeedDescriptor descriptor, int rowNumber) {
        Row row = addRow(sheet, rowNumber);
        row.setHeight(HEADER_HEIGHT);
        CellStyle headerCellStyle = createHeaderCellStyle();
        Font documentFont = createDocumentFont(FONT_SIZE);
        for (ExcelColumnDescriptor column : descriptor.getColumnsMap().values()) {
            validateColumn(sheet, column);
            CellStyle columnStyle = buildColumnStyle(column);
            columnStyle.setFont(documentFont);
            String headerName = column.getHeader();
            addColumnHeader(row, column.getNumber(), columnStyle, headerCellStyle, headerName);
        }
    }

    private <ObjectType> int marshalItem(Sheet sheet, int rowCounter, ExcelFeedDescriptor descriptor,
                                         ExcelContentResolver<ObjectType> excelContentResolver, @NonNull ObjectType o) {
        int row = rowCounter;
        List<Map<String, Object>> contentRows = excelContentResolver.resolveRow(o);
        for (Map<String, Object> content : contentRows) {
            marshallRow(sheet, row, descriptor, 1, content);
            row = row + 1;
        }
        return contentRows.size();
    }

    private void marshallRow(Sheet sheet, int rowCounter, ExcelFeedDescriptor descriptor, int contentHeight, Map<String, Object> content) {
        Row row = addRow(sheet, rowCounter);
        formRow(descriptor, row, content);
        row.setHeight((short) (Math.min(contentHeight, ROW_MAX_HEIGHT) * row.getSheet().getDefaultRowHeight()));
    }

    private void formRow(ExcelFeedDescriptor descriptor, Row row, Map<String, Object> content) {
        for (Map.Entry<String, Object> entry : content.entrySet()) {
            String key = entry.getKey();
            ExcelColumnDescriptor column = descriptor.getColumnsMap().get(key);
            if (column != null) {
                addCell(row, column.getNumber(), column.getCellType(), entry.getValue());
            }
        }
    }

    private void autoSize(SXSSFSheet xmlSheet, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            xmlSheet.trackAllColumnsForAutoSizing();
            xmlSheet.autoSizeColumn(i);
            Integer size = autoSize.get(i);
            if (size == null) {
                size = -1;
            }
            autoSize.put(i, Math.max(size, xmlSheet.getColumnWidth(i)));
        }
    }

    private void finalizeSheet(Sheet sheet, int columnCount) throws IOException {
        SXSSFSheet xmlSheet = (SXSSFSheet) sheet;
        autoSize(xmlSheet, columnCount);
        for (int i = 0; i < columnCount; i++) {
            if (autoSize.get(i) < MAX_COLUMN_WIDTH) {
                xmlSheet.setColumnWidth(i, autoSize.get(i));
            } else {
                xmlSheet.setColumnWidth(i, MAX_COLUMN_WIDTH);
            }
        }
        xmlSheet.flushRows();
    }

    private void addCell(Row row, int column, ExcelColumnDescriptor.CellType type, Object value) {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        switch (type) {
            case FLOAT2:
                if (value instanceof BigDecimal) {
                    addNumericCell(row, column, ((BigDecimal) value).doubleValue());
                } else {
                    addNumericCell(row, column, (Double) value);
                }
                break;
            case STRING:
                addStringCell(row, column, value);
                break;
            case DATE:
                String valueFormatted = simpleDateFormat.format(value);
                addStringCell(row, column, valueFormatted);
                break;
            case INTEGER:
                addIntegerCell(row, column, (Integer) value);
                break;
            case LONG:
                addLongCell(row, column, (Long) value);
                break;
        }
    }

    private Cell addDateCell(Row row, int counter, Date value) {
        if (value != null) {
            Cell cell = row.createCell(counter, CellType.STRING);

            CellStyle style = row.getSheet().getColumnStyle(counter);
            if (style != null) {
                cell.setCellStyle(style);
            }
            cell.setCellValue(value);
            return cell;
        } else {
            return null;
        }
    }

    private Cell addStringCell(Row row, int counter, Object value) {
        Cell cell = row.createCell(counter, CellType.STRING);
        CellStyle style = row.getSheet().getColumnStyle(counter);
        if (style != null) {
            cell.setCellStyle(style);
        }
        cell.setCellValue(row.getSheet().getWorkbook().getCreationHelper().createRichTextString(StringUtils.trimToEmpty(value.toString())));
        return cell;
    }

    private Cell addIntegerCell(Row row, int counter, Integer value) {
        Cell cell = row.createCell(counter, value == null ? CellType.BLANK : CellType.NUMERIC);
        CellStyle style = row.getSheet().getColumnStyle(counter);
        if (style != null) {
            cell.setCellStyle(style);
        }
        if (value != null) {
            cell.setCellValue(value);
        }
        return cell;
    }

    private Cell addLongCell(Row row, int counter, Long value) {
        Cell cell = row.createCell(counter, value == null ? CellType.BLANK : CellType.NUMERIC);
        CellStyle style = row.getSheet().getColumnStyle(counter);
        if (style != null) {
            cell.setCellStyle(style);
        }
        if (value != null) {
            cell.setCellValue(value);
        }
        return cell;
    }


    private Cell addNumericCell(Row row, int counter, Double value) {
        Cell cell = row.createCell(counter, value == null ? CellType.BLANK : CellType.NUMERIC);

        CellStyle style = row.getSheet().getColumnStyle(counter);
        if (style != null) {
            cell.setCellStyle(style);
        }
        if (value != null) {
            cell.setCellValue(value);
        }
        return cell;
    }

    private CellStyle buildColumnStyle(ExcelColumnDescriptor column) {
        if (!cellStyleMap.containsKey(column.getCellType())) {
            CellStyle style = createCellStyle();
            switch (column.getCellType()) {
                case FLOAT2:
                    style.setDataFormat(getCreationHelper().createDataFormat().getFormat("0.00"));
                    break;
                case STRING:
                    style.setDataFormat(getCreationHelper().createDataFormat().getFormat("text"));
                    style.setWrapText(true);
                    break;
                default:
                    style.setWrapText(true);
                    break;
            }
            cellStyleMap.put(column.getCellType(), style);
        }
        return cellStyleMap.get(column.getCellType());
    }

    private void validateColumn(Sheet sheet, ExcelColumnDescriptor column) {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        CellRangeAddressList addressList = getCellAddresses(column);
        DataValidationConstraint constraint = createConstraint(helper, column);
        if (constraint == null) {
            return;
        }
        DataValidation validation = helper.createValidation(constraint, addressList);
        if (validation != null) {
            validation.setSuppressDropDownArrow(false);
            validation.setShowErrorBox(true);
            sheet.addValidationData(validation);
        }
    }

    private CellRangeAddressList getCellAddresses(ExcelColumnDescriptor column) {
        int columnIndex = column.getNumber();
        return new CellRangeAddressList(FIRST_DATA_ROW, LAST_DATA_ROW, columnIndex, columnIndex);
    }

    private static DataValidationConstraint createConstraint(DataValidationHelper helper, ExcelColumnDescriptor column) {
        DataValidationConstraint constraint = null;
        if (column.getCellType() == FLOAT2) {
            constraint = helper.createNumericConstraint(DataValidationConstraint.ValidationType.DECIMAL,
                    DataValidationConstraint.OperatorType.BETWEEN,
                    "0.0",
                    String.valueOf(Float.MAX_VALUE));
        }
        return constraint;
    }
}
