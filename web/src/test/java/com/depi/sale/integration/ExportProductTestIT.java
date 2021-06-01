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
import org.springframework.data.domain.*;
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

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private WebSaleService saleService;

    @Test
    @DisplayName("Test findById Success")
    void testFindById() {
        Sale sale = new Sale();
        sale.setCustomerName("TESTED1");
        sale.setAmount(BigDecimal.valueOf(1.00));
        saleRepository.save(sale);
        Sale sale1 = saleRepository.findByCustomerName(sale.getCustomerName());

        Optional<SaleDTO> returnedSaleDTO = Optional.ofNullable(saleService.getById(sale1.getId()));

        assertTrue(returnedSaleDTO.isPresent(), "Sale not found");
        assertEquals(returnedSaleDTO.get().getId(), sale1.getId(), "The SaleDTO returned not the same");

        saleRepository.delete(sale);
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
        sale.setCustomerName("TESTED2");
        sale.setAmount(BigDecimal.TEN);
        saleRepository.save(sale);

        Sale sale1 = new Sale();
        sale1.setCustomerName("TESTED3");
        sale1.setAmount(BigDecimal.TEN);
        saleRepository.save(sale1);

        Pageable pageable = PageRequest.of(0, 2);

        Page<SaleDTO> sales1 = saleService.findAll(pageable);

        assertEquals(2, sales1.getSize(), "findAll should return 2 sales");

        saleRepository.delete(sale);
        saleRepository.delete(sale1);
    }

    @Test
    @DisplayName("Test save Sale")
    void testSave() {
        Sale sale = new Sale();
        sale.setCustomerName("TESTED4");
        sale.setAmount(BigDecimal.ONE);

        SaleDTO returnedSaleDTO = saleService.newSale(sale);

        assertNotNull(returnedSaleDTO, "The saved saleDTO should not be null");
        assertEquals(4, returnedSaleDTO.getId(), "SaleDTO id should be 1");
        saleRepository.delete(sale);
    }

    @Test
    @DisplayName("Test searchByName")
    void testSearchByName() {
        for (int i = 1; i <= 30; i++) {
            Sale sale = new Sale();
            sale.setCustomerName("TESTED" + i);
            sale.setAmount(BigDecimal.ONE);
            saleRepository.save(sale);
        }
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "customerName");
        Page<SaleDTO> saleDTOS = saleService.findAll(pageable);

        assertEquals(0, saleDTOS.getNumber(), "Page number should be 0");
        assertEquals(10, saleDTOS.getSize(), "Page size should be 10");
        assertTrue(saleDTOS.getSort().isSorted(), "Page must be sorted by name");
    }

    @Test
    @DisplayName("Test searchByDate")
    void testSearchByDate() {
        for (int i = 1; i <= 30; i++) {
            Sale sale = new Sale();
            sale.setCustomerName("TESTED" + i);
            sale.setAmount(BigDecimal.ONE);
            saleRepository.save(sale);
        }
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "date");
        Page<SaleDTO> saleDTOS = saleService.findAll(pageable);

        assertEquals(0, saleDTOS.getNumber(), "Page number should be 0");
        assertEquals(10, saleDTOS.getSize(), "Page size should be 10");
        assertTrue(saleDTOS.getSort().isSorted(), "Page must be sorted by date");
    }
}
