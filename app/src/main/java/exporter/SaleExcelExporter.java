package exporter;

import com.depi.sale.entity.Sale;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class SaleExcelExporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private Page<Sale> sales;

    private void writeHeaderRow() {
        Row row = sheet.createRow(0);

        Cell cell = row.createCell(0);
        cell.setCellValue("Sale ID");

        cell = row.createCell(1);
        cell.setCellValue("Date");

        cell = row.createCell(2);
        cell.setCellValue("Customer name");

        cell = row.createCell(3);
        cell.setCellValue("Amount");
    }

    private void writeDataRows() {
        int rowCount = 1;

        for (Sale s : sales) {
            Row row = sheet.createRow(rowCount);

            Cell cell = row.createCell(0);
            cell.setCellValue(s.getId());

            cell = row.createCell(1);
            cell.setCellValue(s.getDate().toString());

            cell = row.createCell(2);
            cell.setCellValue(s.getCustomerName());

            cell = row.createCell(3);
            cell.setCellValue(s.getAmount().toString());
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderRow();
        writeDataRows();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    public SaleExcelExporter(Page<Sale> sales) {
        this.sales = sales;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Sales");
    }
}
