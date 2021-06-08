package feed.excel;

import feed.excel.export.XSSFReaderBean;
import org.apache.kafka.common.utils.CloseableIterator;

import java.util.List;
import java.util.NoSuchElementException;

public class XSSFIterator implements CloseableIterator<List<String>> {

    private final XSSFReaderBean reader;
    private List<String> row;
    private final int sheetNumber;

    public XSSFIterator(XSSFReaderBean reader) {
        this.reader = reader;
        this.sheetNumber = reader.getSelectedSheetIndex();
        row = reader.readDataRow(this.sheetNumber);
    }

    @Override
    public void close() {
        try {
            reader.close(sheetNumber);
        } catch (Exception ignored) {
        }
    }

    @Override
    public boolean hasNext() {
        return row != null;
    }

    @Override
    public List<String> next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        List<String> old = row;
        row = reader.readDataRow(sheetNumber);
        return old;
    }
}
