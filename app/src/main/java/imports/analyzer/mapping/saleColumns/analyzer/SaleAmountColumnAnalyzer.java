package imports.analyzer.mapping.saleColumns.analyzer;

import imports.ReportMessage;
import imports.ReportMessageSource;
import imports.analyzer.OneColumnAnalyzer;
import imports.bean.ImportSaleColumnMappingBean;
import imports.converter.BaseEntityConverter;
import imports.converter.EntityConverter;
import imports.reader.BigDecimalFlatReader;
import feed.SaleColumnKey;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class SaleAmountColumnAnalyzer extends OneColumnAnalyzer<List<String>, ImportSaleColumnMappingBean> {

    public SaleAmountColumnAnalyzer(ReportMessageSource reportMessageSource, Locale fileLocale) {
        super(reportMessageSource, fileLocale);
    }

    @Override
    protected EntityConverter<List<String>, ImportSaleColumnMappingBean> buildConverter(String columnName, int columnIndex) {
        return new BaseEntityConverter<>(
                reportMessageSource, new BigDecimalFlatReader(columnIndex, columnName, reportMessageSource)
        ) {

            @Override
            protected Collection<ReportMessage> writeEntity(ImportSaleColumnMappingBean bean, BigDecimal amount) {
                bean.setAmount(amount);
                return Collections.emptyList();
            }
        };
    }

    @Override
    protected String getSupportedColumnName() {
        return SaleColumnKey.AMOUNT.getName();
    }

}

