package feed.excel.export;

import lombok.NonNull;

public class ExcelColumnDescriptor {
    private final int number;
    private final String header;
    private final boolean required;
    private final CellType cellType;
    public ExcelColumnDescriptor(int number, @NonNull CellType cellType, @NonNull String header, boolean required) {
        this.number = number;
        this.cellType = cellType;
        this.header = header;
        this.required = required;
    }

    public CellType getCellType() {
        return cellType;
    }

    public boolean isRequired() {
        return required;
    }

    public int getNumber() {
        return number;
    }

    public String getHeader() {
        return header;
    }

    public enum CellType {
        STRING,
        /**
         * Float number with two decimal digits
         */
        FLOAT2,
        DATE,
        INTEGER,
        LONG,
    }
}
