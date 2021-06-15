package imports.reader;

import imports.ReportMessage;
import imports.ReportMessageSource;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class LongFlatReader extends BaseFlatReader<Long> {

    private final boolean isRequired;

    public LongFlatReader(int index, String columnName, ReportMessageSource reportMessageSource) {
        this(index, columnName, reportMessageSource, false);
    }

    public LongFlatReader(int index, String columnName, ReportMessageSource reportMessageSource, boolean isRequired) {
        super(index, columnName, reportMessageSource);
        this.isRequired = isRequired;
    }

    @Override
    public Long read(List<String> dataRow, List<ReportMessage> messages) {
        String value = readEntity(dataRow);
        if (StringUtils.isNotBlank(value)) {
            try {
                return Long.valueOf(value);
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