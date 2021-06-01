package com.depi.sale.integration;

import com.depi.sale.SaleApplication;
import com.depi.sale.dto.SaleDTO;
import com.depi.sale.entity.Sale;
import com.depi.sale.exceptions.SaleNotFoundException;
import com.depi.sale.repository.SaleRepository;
import com.depi.sale.service.WebSaleService;
import feed.exports.AppExportService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doReturn;

@ActiveProfiles({"test"})
@SpringBootTest(classes = {SaleApplication.class,}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExportProductTestIT {

    @MockBean
    private SaleRepository saleRepository;

    @Autowired
    private WebSaleService saleService;

    @Test
    @DisplayName("Test findById Success")
    void testFindById() {
        Sale sale = new Sale();
        sale.setId(1);
        sale.setDate(new Date());
        sale.setCustomerName("TESTED");
        sale.setAmount(BigDecimal.valueOf(1.00));
        doReturn(Optional.of(sale)).when(saleRepository).findById(sale.getId());

        Optional<SaleDTO> returnedSaleDTO = Optional.ofNullable(saleService.getById(sale.getId()));

        assertTrue(returnedSaleDTO.isPresent(), "Sale not found");
        assertSame(returnedSaleDTO.get().getId(), sale.getId(), "The SaleDTO returned not the same");
    }

    @Test
    @DisplayName("Test findById Not Found")
    void testFindByIdNotFound() {
        assertThrows(SaleNotFoundException.class, () -> saleService.getById(1L));
    }

    @Test
    @DisplayName("Test findAll")
    void testFindAll() {
        Sale sale = new Sale();
        sale.setId(1);
        sale.setDate(new Date());
        sale.setCustomerName("TESTED");
        sale.setAmount(BigDecimal.TEN);

        Sale sale1 = new Sale();
        sale1.setId(2);
        sale1.setDate(new Date());
        sale1.setCustomerName("TESTED2");
        sale1.setAmount(BigDecimal.TEN);

        Pageable pageable = PageRequest.of(0, 2);
        List<Sale> sales = new ArrayList<>();
        sales.add(sale);
        sales.add(sale1);
        Page<Sale> salesPage = new PageImpl<>(sales);

        doReturn(salesPage).when(saleRepository).findAll(pageable);

        Page<SaleDTO> sales1 = saleService.findAll(pageable);

        assertEquals(2, sales1.getTotalElements(), "findAll should return 2 sales");
    }
}
