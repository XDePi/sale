package imports.impl;

import imports.ReportMessageSource;
import imports.analyzer.ColumnAnalyzer;
import imports.analyzer.CompositeColumnAnalyzer;
import imports.analyzer.mapping.saleColumns.SaleColumnKeyColumnsHeaderAnalyzerBuilder;
import imports.analyzer.mapping.saleColumns.analyzer.SaleAmountColumnAnalyzer;
import imports.analyzer.mapping.saleColumns.analyzer.SaleCustomerNameColumnAnalyzer;
import imports.analyzer.mapping.saleColumns.analyzer.SaleDateColumnAnalyzer;
import imports.analyzer.mapping.saleColumns.analyzer.SaleIdColumnAnalyzer;
import imports.bean.ImportSaleColumnMappingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class SaleMappingReader extends BaseMappingReader<ImportSaleColumnMappingBean> {

    private final SaleColumnKeyColumnsHeaderAnalyzerBuilder columnsHeaderAnalyzerBuilder;

    public SaleMappingReader(SaleColumnKeyColumnsHeaderAnalyzerBuilder columnsHeaderAnalyzerBuilder) {
        this.columnsHeaderAnalyzerBuilder = columnsHeaderAnalyzerBuilder;
    }

    @Override
    protected ImportSaleColumnMappingBean createBean(int rowNumber) {
        return new ImportSaleColumnMappingBean(rowNumber);
    }

    public ColumnAnalyzer<List<String>, ImportSaleColumnMappingBean> buildAnalyzer(ReportMessageSource reportMessageSource, Locale fileLocale) {
        List<ColumnAnalyzer<List<String>, ImportSaleColumnMappingBean>> result = new ArrayList<>();
        result.add(new SaleIdColumnAnalyzer(reportMessageSource, fileLocale));
        result.add(new SaleDateColumnAnalyzer(reportMessageSource, fileLocale));
        result.add(new SaleCustomerNameColumnAnalyzer(reportMessageSource, fileLocale));
        result.add(new SaleAmountColumnAnalyzer(reportMessageSource, fileLocale));
        result.add(columnsHeaderAnalyzerBuilder.buildAnalyzer(reportMessageSource, fileLocale));
        return new CompositeColumnAnalyzer<>(result);
    }
}