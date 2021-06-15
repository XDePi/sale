package imports.impl;

import imports.ReportMessage;
import imports.ReportMessageSource;
import imports.analyzer.ColumnAnalyzer;
import imports.bean.BaseImportMappingBean;
import imports.converter.CompositeEntityConverter;
import imports.converter.EntityConverter;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.utils.CloseableIterator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class BaseMappingReader<T extends BaseImportMappingBean> {

    public List<T> readMappings(ReportMessageSource reportMessageSource, Locale fileLocale,
                                CloseableIterator<List<String>> iterator, List<ReportMessage> reportMessages) {
        EntityConverter<List<String>, T> converter = new CompositeEntityConverter<>(
                buildAnalyzer(reportMessageSource, fileLocale).analyze(iterator.next()));

        int rowNumber = 1;
        List<T> result = new ArrayList<>();
        while (iterator.hasNext()) {
            List<String> row = iterator.next();
            rowNumber++;
            if (row == null || row.isEmpty() || rowContainsOnlyBlanks(row)) {
                continue;
            }
            T bean = createBean(rowNumber);
            List<ReportMessage> convertMessages = converter.convert(row, bean);
            result.add(bean);
            if (!convertMessages.isEmpty()) {
                reportMessages.addAll(buildReportMessage(convertMessages));
            }
        }
        return result;
    }

    protected boolean rowContainsOnlyBlanks(List<String> row) {
        for (String value : row) {
            if (StringUtils.isNotBlank(value)) {
                return false;
            }
        }
        return true;
    }

    private List<ReportMessage> buildReportMessage(List<ReportMessage> messages) {
        List<ReportMessage> result = new ArrayList<>();
        if (ReportMessage.hasErrors(messages)) {
            result.add(new ReportMessage(ReportMessage.combineErrorMessages(messages), ReportMessage.Level.ERROR));
        }
        if (ReportMessage.hasWarnings(messages)) {
            result.add(new ReportMessage(ReportMessage.combineWarningMessages(messages), ReportMessage.Level.WARNING));
        }
        if (ReportMessage.hasInfos(messages)) {
            result.add(new ReportMessage(ReportMessage.combineInfoMessages(messages), ReportMessage.Level.INFO));
        }
        return result;
    }

    protected abstract T createBean(int rowNumber);

    protected abstract ColumnAnalyzer<List<String>, T> buildAnalyzer(ReportMessageSource reportMessageSource, Locale fileLocale);
}
