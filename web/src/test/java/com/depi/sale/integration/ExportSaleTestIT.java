package com.depi.sale.integration;

import com.depi.sale.SaleApplication;
import com.depi.sale.entity.Sale;
import com.poiji.bind.Poiji;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles({"test"})
@SpringBootTest(classes = {SaleApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExportSaleTestIT extends BaseIT {

    @BeforeEach
    void init() {
        saleRepository.deleteAll();
    }

    @Test
    void exportMapping_when_only_one_record_in_persistance_then_it_is_exported() throws IOException {
        Sale sale = new Sale();
        sale.setDate(new Date());
        sale.setCustomerName("DENIS");
        sale.setAmount(BigDecimal.valueOf(1.55));
        saleRepository.save(sale);

        File file = appExportService.exportMappings();
        assertNotNull(file);

        List<Sale> sale1 = Poiji.fromExcel(file, Sale.class);

        assertEquals(1, sale1.get(0).getId(), "Id must be 1 because sale entity was saved with the id = 1");
        assertEquals("DENIS", sale1.get(0).getCustomerName(), "Name must be DENIS because sale entity was saved with this name");
        assertEquals(BigDecimal.valueOf(1.55).doubleValue(), sale1.get(0).getAmount().doubleValue(), "Amount must be 1.00 because sale entity was saved with this amount");
    }
}
