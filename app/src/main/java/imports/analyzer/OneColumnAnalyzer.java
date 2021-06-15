package imports.analyzer;

import imports.ReportMessageSource;
import imports.converter.EntityConverter;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

@AllArgsConstructor
public abstract class OneColumnAnalyzer<DataRow, Delta> implements ColumnAnalyzer<DataRow, Delta> {

    private static final String REQUIRED_COLUMN_POSTFIX = "*";

    protected final ReportMessageSource reportMessageSource;
    protected final Locale fileLocale;

    @NonNull
    @Override
    public final List<EntityConverter<DataRow, Delta>> analyze(@NonNull List<String> columnNames) {
        for (int i = 0; i < columnNames.size(); i++) {
            String columnName = columnNames.get(i);
            if (columnNameMatches(columnName)) {
                EntityConverter<DataRow, Delta> converter = buildConverter(columnName, i);
                if (converter != null) {
                    return Collections.singletonList(converter);
                }
            }
        }
        return Collections.emptyList();
    }

    protected abstract EntityConverter<DataRow, Delta> buildConverter(String columnName, int columnIndex);

    protected abstract String getSupportedColumnName();

    protected boolean columnNameMatches(String columnName) {
        return normalizeColumnName(columnName).equals(getSupportedColumnName());
    }

    protected String normalizeColumnName(String columnName) {
        if (StringUtils.isBlank(columnName)) {
            return StringUtils.EMPTY;
        }
        columnName = columnName.trim();
        if (columnName.endsWith(REQUIRED_COLUMN_POSTFIX)) {
            columnName = columnName.substring(0, columnName.length() - REQUIRED_COLUMN_POSTFIX.length()).trim();
        }
        return columnName.toLowerCase(fileLocale);
    }
}
