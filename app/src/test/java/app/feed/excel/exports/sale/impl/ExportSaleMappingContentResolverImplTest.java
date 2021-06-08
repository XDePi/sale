package app.feed.excel.exports.sale.impl;

import com.depi.sale.entity.Sale;
import feed.SaleColumnKey;
import feed.excel.export.ExcelFeedDescriptor;
import feed.exports.sale.ExportMerchantSaleKeyResolver;
import feed.exports.sale.impl.ExportSaleMappingContentResolverImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ExportSaleMappingContentResolverImplTest {

    @Mock
    private ExportMerchantSaleKeyResolver merchantSaleKeyResolver;

    private ExportSaleMappingContentResolverImpl resolver;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        resolver = new ExportSaleMappingContentResolverImpl();
    }

    @Test
    public void buildDescriptor_returns_descriptor_for_all_column_keys() {
        when(merchantSaleKeyResolver.buildDescriptor()).thenReturn(new ExcelFeedDescriptor());

        ExcelFeedDescriptor excelFeedDescriptor = resolver.buildDescriptor();
        List<String> descriptorColumnNames = excelFeedDescriptor.getColumnsMap().keySet().stream()
                .sorted().collect(Collectors.toList());
        List<String> expectedColumnNames =
                Arrays.stream(SaleColumnKey.values()).map(SaleColumnKey::getName)
                        .sorted().collect(Collectors.toList());

        assertEquals(expectedColumnNames, descriptorColumnNames);
    }

    @Test
    public void buildDescriptor_returns_descriptor_sale_id_column_if_configured() {
        resolver = new ExportSaleMappingContentResolverImpl();

        when(merchantSaleKeyResolver.buildDescriptor()).thenReturn(new ExcelFeedDescriptor());

        ExcelFeedDescriptor excelFeedDescriptor = resolver.buildDescriptor();
        Set<String> descriptorColumnNames = excelFeedDescriptor.getColumnsMap().keySet();

        assertThat("Sale_id column added", descriptorColumnNames,
                hasItems(SaleColumnKey.SALE_ID.getName()));
    }

    @Test
    public void buildDescriptor_returns_descriptor_date_column_if_configured() {
        resolver = new ExportSaleMappingContentResolverImpl();

        when(merchantSaleKeyResolver.buildDescriptor()).thenReturn(new ExcelFeedDescriptor());

        ExcelFeedDescriptor excelFeedDescriptor = resolver.buildDescriptor();
        Set<String> descriptorColumnNames = excelFeedDescriptor.getColumnsMap().keySet();

        assertThat("Date column added", descriptorColumnNames,
                hasItems(SaleColumnKey.DATE.getName()));
    }

    @Test
    public void buildDescriptor_returns_descriptor_customer_name_column_if_configured() {
        resolver = new ExportSaleMappingContentResolverImpl();

        when(merchantSaleKeyResolver.buildDescriptor()).thenReturn(new ExcelFeedDescriptor());

        ExcelFeedDescriptor excelFeedDescriptor = resolver.buildDescriptor();
        Set<String> descriptorColumnNames = excelFeedDescriptor.getColumnsMap().keySet();

        assertThat("Customer_name column added", descriptorColumnNames, hasItems(SaleColumnKey.CUSTOMER_NAME.getName()));
    }

    @Test
    public void buildDescriptor_returns_descriptor_amount_column_if_configured() {
        resolver = new ExportSaleMappingContentResolverImpl();

        when(merchantSaleKeyResolver.buildDescriptor()).thenReturn(new ExcelFeedDescriptor());

        ExcelFeedDescriptor excelFeedDescriptor = resolver.buildDescriptor();
        Set<String> descriptorColumnNames = excelFeedDescriptor.getColumnsMap().keySet();

        assertThat("Amount column added", descriptorColumnNames,
                hasItems(SaleColumnKey.AMOUNT.getName()));
    }

    @Test
    void resolveRow_check_all_data_written() {

        Sale item = new Sale();
        item.setId(1L);
        item.setDate(new Date());
        item.setCustomerName("TESTED");
        item.setAmount(BigDecimal.ONE);

        List<Map<String, Object>> rows = resolver.resolveRow(item);

        assertEquals(1, rows.size(), "Rows expected");
        Map<String, Object> row = rows.get(0);
        assertEquals(1L, row.get("sale_id"), "Expected content");
        assertEquals(item.getDate(), row.get("date"), "Expected content");
        assertEquals(item.getCustomerName(), row.get("customer_name"), "Expected content");
        assertEquals(item.getAmount(), row.get("amount"), "Expected content");
    }

    @Test
    void resolveRow_empty_sale_does_not_break_resolver() {
        Sale item = new Sale();

        List<Map<String, Object>> rows = resolver.resolveRow(item);

        assertEquals(1, rows.size(), "Rows expected");
        Map<String, Object> row = rows.get(0);
        assertEquals(4, row.size(), "Columns expected");
    }

    @Test
    public void resolveRow_returns_rows_content_with_sale_id_if_configured() {
        resolver = new ExportSaleMappingContentResolverImpl();

        Sale item = new Sale();
        item.setId(1L);

        List<Map<String, Object>> rows = resolver.resolveRow(item);

        assertEquals(1, rows.size());
        Map<String, Object> rowMap = rows.get(0);
        assertEquals(item.getId(), rowMap.get(SaleColumnKey.SALE_ID.getName()),
                "Sale id added");
    }

    @Test
    public void resolveRow_returns_rows_content_with_date_if_configured() {
        resolver = new ExportSaleMappingContentResolverImpl();

        Sale item = new Sale();
        item.setDate(new Date());

        List<Map<String, Object>> rows = resolver.resolveRow(item);

        assertEquals(1, rows.size());
        Map<String, Object> rowMap = rows.get(0);
        assertEquals(item.getDate(), rowMap.get(SaleColumnKey.DATE.getName()),
                "Date added");
    }

    @Test
    public void resolveRow_returns_rows_content_with_customer_name_if_configured() {
        resolver = new ExportSaleMappingContentResolverImpl();

        Sale item = new Sale();
        item.setCustomerName("TESTED");

        List<Map<String, Object>> rows = resolver.resolveRow(item);

        assertEquals(1, rows.size());
        Map<String, Object> rowMap = rows.get(0);
        assertEquals(item.getCustomerName(), rowMap.get(SaleColumnKey.CUSTOMER_NAME.getName()),
                "Customer name added");
    }

    @Test
    public void resolveRow_returns_rows_content_with_amount_if_configured() {
        resolver = new ExportSaleMappingContentResolverImpl();

        Sale item = new Sale();
        item.setAmount(BigDecimal.ONE);

        List<Map<String, Object>> rows = resolver.resolveRow(item);

        assertEquals(1, rows.size());
        Map<String, Object> rowMap = rows.get(0);
        assertEquals(item.getAmount(), rowMap.get(SaleColumnKey.AMOUNT.getName()),
                "Amount added");
    }

}
