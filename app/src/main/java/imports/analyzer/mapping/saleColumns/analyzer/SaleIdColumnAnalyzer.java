package imports.analyzer.mapping.saleColumns.analyzer;

import imports.ReportMessage;
import imports.ReportMessageSource;
import imports.analyzer.OneColumnAnalyzer;
import imports.bean.ImportSaleColumnMappingBean;
import imports.converter.BaseEntityConverter;
import imports.converter.EntityConverter;
import imports.reader.LongFlatReader;
import feed.SaleColumnKey;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class SaleIdColumnAnalyzer extends OneColumnAnalyzer<List<String>, ImportSaleColumnMappingBean> {

    public SaleIdColumnAnalyzer(ReportMessageSource reportMessageSource, Locale fileLocale) {
        super(reportMessageSource, fileLocale);
    }

    @Override
    protected EntityConverter<List<String>, ImportSaleColumnMappingBean> buildConverter(String columnName, int columnIndex) {
        return new BaseEntityConverter<>(
                reportMessageSource, new LongFlatReader(columnIndex, columnName, reportMessageSource)
        ) {

            @Override
            protected Collection<ReportMessage> writeEntity(ImportSaleColumnMappingBean bean, Long saleId) {
                bean.setSaleId(saleId);
                return Collections.emptyList();
            }
        };
    }

    @Override
    protected String getSupportedColumnName() {
        return SaleColumnKey.SALE_ID.getName();
    }
}
