package exporter;

import com.depi.sale.entity.Sale;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class SaleExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Sale> sales;

    public SaleExcelExporter(List<Sale> sales) {
        this.sales = sales;
        workbook = new XSSFWorkbook();
    }
    
    private void writeHeaderLine() {
        sheet = workbook.createSheet("Sales");

        Row row = sheet.createRow(0);

        createCell(row, 0, "Sale ID");
        createCell(row, 1, "Date");
        createCell(row, 2, "Customer name");
        createCell(row, 3, "Amount");
    }

    private void createCell(Row row, int columnCount, Object value) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Long)
            cell.setCellValue((Long) value);
        else if (value instanceof Date)
            cell.setCellValue(value.toString());
        else if (value instanceof String)
            cell.setCellValue((String) value);
        else
            cell.setCellValue(String.valueOf(value));
    }

    private void writeDataLines() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (Sale s : sales) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, s.getId());
            createCell(row, columnCount++, s.getDate());
            createCell(row, columnCount++, s.getCustomerName());
            createCell(row, columnCount++, s.getAmount());
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }
}
