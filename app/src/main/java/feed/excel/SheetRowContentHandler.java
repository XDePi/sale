package feed.excel;

import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SheetRowContentHandler implements XSSFSheetXMLHandler.SheetContentsHandler {

    private List<String> content;

    @Override
    public void startRow(int rowNum) {
        this.content = new LinkedList<>();
    }

    @Override
    public void endRow(int rowNum) {
        // empty
    }

    @Override
    public void cell(String cellReference, String formattedValue, XSSFComment xssfComment) {
        if (content == null) {
            return;
        }
        int index = new CellReference(cellReference).getCol();
        while (content.size() <= index) {
            content.add(null);
        }
        content.set(index, formattedValue);
    }

    @Override
    public void headerFooter(String text, boolean isHeader, String tagName) {
        // empty
    }

    public List<String> getContent() {
        return Collections.unmodifiableList(content);
    }

    public void clear() {
        content = null;
    }
}
