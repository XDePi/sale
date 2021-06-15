package imports.reader;

import imports.ReportMessage;
import imports.ReportMessageSource;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;

public class BigDecimalFlatReader extends BaseFlatReader<BigDecimal> {

    private final boolean isRequired;

    public BigDecimalFlatReader(int index, String columnName, ReportMessageSource reportMessageSource) {
        this(index, columnName, reportMessageSource, false);
    }

    public BigDecimalFlatReader(int index, String columnName, ReportMessageSource reportMessageSource, boolean isRequired) {
        super(index, columnName, reportMessageSource);
        this.isRequired = isRequired;
    }

    @Override
    public BigDecimal read(List<String> dataRow, List<ReportMessage> messages) {
        String value = readEntity(dataRow);
        if (StringUtils.isNotBlank(value)) {
            try {
                double doubleValue = Double.parseDouble(value);
                return BigDecimal.valueOf(doubleValue);
            } catch (NumberFormatException e) {
                messages.add(reportMessageSource.getErrorMsg("read.error.flat.long.format", new String[]{columnName}));
            }
        }
        if (isRequired) {
            messages.add(reportMessageSource.getErrorMsg("read.error.string.required", new String[]{columnName}));
        }
        return null;
    }
}