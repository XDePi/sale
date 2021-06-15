package imports.reader;

import imports.ReportMessage;
import imports.ReportMessageSource;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class StringFlatReader extends BaseFlatReader<String> {

    private final boolean isRequired;

    public StringFlatReader(int index, String columnName, ReportMessageSource reportMessageSource) {
        this(index, columnName, reportMessageSource, false);
    }

    public StringFlatReader(int index, String columnName, ReportMessageSource reportMessageSource, boolean isRequired) {
        super(index, columnName, reportMessageSource);
        this.isRequired = isRequired;
    }

    @Override
    public String read(List<String> dataRow, List<ReportMessage> messages) {
        String value = readEntity(dataRow);
        if (StringUtils.isNotBlank(value))
            return value.trim();
        if (isRequired)
            messages.add(reportMessageSource.getErrorMsg("read.error.string.required", new String[]{columnName}));
        return null;
    }
}
