package imports.analyzer.mapping.saleColumns.analyzer;

import imports.ReportMessage;
import imports.ReportMessageSource;
import imports.analyzer.OneColumnAnalyzer;
import imports.bean.ImportSaleColumnMappingBean;
import imports.converter.BaseEntityConverter;
import imports.converter.EntityConverter;
import imports.reader.DateFlatReader;
import feed.SaleColumnKey;

import java.util.*;

public class SaleDateColumnAnalyzer extends OneColumnAnalyzer<List<String>, ImportSaleColumnMappingBean> {

    public SaleDateColumnAnalyzer(ReportMessageSource reportMessageSource, Locale fileLocale) {
        super(reportMessageSource, fileLocale);
    }

    @Override
    protected EntityConverter<List<String>, ImportSaleColumnMappingBean> buildConverter(String columnName, int columnIndex) {
        return new BaseEntityConverter<>(
                reportMessageSource, new DateFlatReader(columnIndex, columnName, reportMessageSource)
        ) {

            @Override
            protected Collection<ReportMessage> writeEntity(ImportSaleColumnMappingBean bean, Date date) {
                bean.setDate(date);
                return Collections.emptyList();
            }
        };
    }

    @Override
    protected String getSupportedColumnName() {
        return SaleColumnKey.DATE.getName();
    }
}
