package feed.excel;

import com.depi.sale.entity.Sale;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.format.CellDateFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.DateFormatConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ExcelHelper {
    static String[] HEADERS = { "sale_id", "date", "customer_name", "amount" };
    static String SHEET = "Sales";

    public static List<Sale> excelToTutorials(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();

            List<Sale> sales = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();

                Sale sale = new Sale();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            sale.setId((long) currentCell.getNumericCellValue());
                            break;

//                        case 1:
//                            sale.setDate(((Date) currentCell.getRichStringCellValue()));
//                            break;

                        case 2:
                            sale.setCustomerName(currentCell.getStringCellValue());
                            break;

                        case 3:
                            sale.setAmount(BigDecimal.valueOf(currentCell.getNumericCellValue()));
                            break;

                        default:
                            break;
                    }

                    cellIdx++;
                }

                sales.add(sale);
            }

            workbook.close();

            return sales;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}
