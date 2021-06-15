package com.depi.sale.integration;

import com.depi.sale.SaleApplication;
import com.depi.sale.entity.Sale;
import feed.SaleColumnKey;
import feed.exports.sale.impl.ExportSaleMappingContentResolverImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles({"test"})
@SpringBootTest(classes = {SaleApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExportSaleTestIT extends BaseIT {

    @BeforeEach
    void init() {
        saleRepository.deleteAll();
    }

    private ExportSaleMappingContentResolverImpl resolver;

    @Test
    void exportMapping_when_only_one_record_in_persistance_then_it_is_exported() throws IOException {
        resolver = new ExportSaleMappingContentResolverImpl();
        Sale sale = new Sale();
        sale.setDate(new Date());
        sale.setCustomerName("DENIS");
        sale.setAmount(BigDecimal.valueOf(1.55));
        saleRepository.save(sale);

        File file = appExportService.exportMappings();
        assertNotNull(file);

        List<Map<String, Object>> rows = resolver.resolveRow(sale);

        Map<String, Object> rowMap = rows.get(0);
        assertEquals(sale.getId(), rowMap.get(SaleColumnKey.SALE_ID.getName()),
                "Id must be 1 because sale entity was saved with the id = 1");
        assertEquals(sale.getDate(), rowMap.get(SaleColumnKey.DATE.getName()),
                "Date added");
        assertEquals(sale.getCustomerName(), rowMap.get(SaleColumnKey.CUSTOMER_NAME.getName()),
                "Name must be DENIS because sale entity was saved with this name");
        assertEquals(sale.getAmount(), rowMap.get(SaleColumnKey.AMOUNT.getName()),
                "Amount must be 1.00 because sale entity was saved with this amount");
    }
}
