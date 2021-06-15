package imports.analyzer.mapping.saleColumns;

import imports.ReportMessageSource;
import imports.analyzer.ColumnAnalyzer;
import imports.bean.ImportSaleColumnMappingBean;
import lombok.NonNull;

import java.util.List;
import java.util.Locale;

public interface SaleColumnKeyColumnsHeaderAnalyzerBuilder {

    @NonNull
    ColumnAnalyzer<List<String>, ImportSaleColumnMappingBean> buildAnalyzer(ReportMessageSource reportMessageSource, Locale fileLocale);
}
