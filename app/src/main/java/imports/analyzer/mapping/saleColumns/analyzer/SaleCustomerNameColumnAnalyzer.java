package imports.analyzer.mapping.saleColumns.analyzer;

import imports.ReportMessage;
import imports.ReportMessageSource;
import imports.analyzer.OneColumnAnalyzer;
import imports.bean.ImportSaleColumnMappingBean;
import imports.converter.BaseEntityConverter;
import imports.converter.EntityConverter;
import imports.reader.StringFlatReader;
import feed.SaleColumnKey;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class SaleCustomerNameColumnAnalyzer extends OneColumnAnalyzer<List<String>, ImportSaleColumnMappingBean> {

    public SaleCustomerNameColumnAnalyzer(ReportMessageSource reportMessageSource, Locale fileLocale) {
        super(reportMessageSource, fileLocale);
    }

    @Override
    protected EntityConverter<List<String>, ImportSaleColumnMappingBean> buildConverter(String columnName, int columnIndex) {
        return new BaseEntityConverter<>(
                reportMessageSource, new StringFlatReader(columnIndex, columnName, reportMessageSource)
        ) {

            @Override
            protected Collection<ReportMessage> writeEntity(ImportSaleColumnMappingBean bean, String customerName) {
                bean.setCustomerName(customerName);
                return Collections.emptyList();
            }
        };
    }

    @Override
    protected String getSupportedColumnName() {
        return SaleColumnKey.CUSTOMER_NAME.getName();
    }

}
