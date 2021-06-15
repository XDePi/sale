package imports.reader;

import imports.ReportMessage;
import imports.ReportMessageSource;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DateFlatReader extends BaseFlatReader<Date> {

    private final boolean isRequired;

    public DateFlatReader(int index, String columnName, ReportMessageSource reportMessageSource) {
        this(index, columnName, reportMessageSource, false);
    }

    public DateFlatReader(int index, String columnName, ReportMessageSource reportMessageSource, boolean isRequired) {
        super(index, columnName, reportMessageSource);
        this.isRequired = isRequired;
    }

    @Override
    public Date read(List<String> dataRow, List<ReportMessage> messages) {
        String value = readEntity(dataRow);
        if (StringUtils.isNotBlank(value))
            try {
                SimpleDateFormat format = new SimpleDateFormat();
                format.applyPattern("dd.MM.yyyy");
                return format.parse(value);
            } catch (ParseException e) {
                messages.add(reportMessageSource.getErrorMsg("read.error.flat.date.format", new String[]{columnName}));
            }
        if (isRequired)
            messages.add(reportMessageSource.getErrorMsg("read.error.string.required", new String[]{columnName}));
        return null;
    }
}
