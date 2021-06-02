package com.depi.sale.integration;

import com.depi.sale.SaleApplication;
import com.depi.sale.dto.SaleDTO;
import com.depi.sale.entity.Sale;
import com.depi.sale.exceptions.SaleNotFoundException;
import com.depi.sale.repository.SaleRepository;
import com.depi.sale.service.WebSaleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles({"test"})
@SpringBootTest(classes = {SaleApplication.class,}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SearchSaleTestIT {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private WebSaleService saleService;

    @Test
    void getById_when_invoked_then_returns_SaleDTO_object() {
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
    void getById_when_entity_not_found_then_throws_an_exception() {
        assertThrows(SaleNotFoundException.class, () -> saleService.getById(1L));
    }

    @Test
    void findAll_when_invoked_then_returns_a_page_with_two_entities() {
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

        assertEquals(2, sales1.getSize(), "findAll should return 2 sales because was inserted only 2 entities and page size is equal to 2");

        saleRepository.delete(sale);
        saleRepository.delete(sale1);
    }

    @Test
    void newSale_when_invoked_then_saves_new_entity() {
        Sale sale = new Sale();
        sale.setCustomerName("TESTED4");
        sale.setAmount(BigDecimal.ONE);

        SaleDTO returnedSaleDTO = saleService.newSale(sale);

        assertNotNull(returnedSaleDTO, "The saved saleDTO should not be null because an entity was posted ");
        assertEquals("TESTED4", returnedSaleDTO.getCustomerName(), "SaleDTO's customer name should be the same as sale's name because saleService must have returned sale entity");
        assertEquals(BigDecimal.ONE, returnedSaleDTO.getAmount(), "SaleDTO's amount should be the same as sale's amount because saleService must have returned sale entity");
        saleRepository.delete(sale);
    }

    @Test
    void findAll_when_invoked_then_returns_a_page_sorted_by_customerName_and_with_pageSize_10() {
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");

        for (int i = 0; i < 5; i++) {
            Sale sale = new Sale();
            sale.setCustomerName(list.get(i));
            sale.setAmount(BigDecimal.ONE);
            saleRepository.save(sale);
        }
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "customerName");
        Page<SaleDTO> saleDTOS = saleService.findAll(pageable);

        assertEquals(0, saleDTOS.getNumber(), "Page number should be 0");
        assertEquals(10, saleDTOS.getSize(), "Page size should be 10");

        assertEquals("A", saleDTOS.toList().get(0).getCustomerName());
        assertEquals("B", saleDTOS.toList().get(1).getCustomerName());
        assertEquals("C", saleDTOS.toList().get(2).getCustomerName());
        assertEquals("D", saleDTOS.toList().get(3).getCustomerName());
        assertEquals("E", saleDTOS.toList().get(4).getCustomerName());
    }

    @Test
    void findAll_when_invoked_then_returns_a_page_sorted_by_date_and_with_pageSize_10() {
        for (int i = 1; i <= 30; i++) {
            Sale sale = new Sale();
            sale.setCustomerName("TESTED");
            sale.setAmount(BigDecimal.ONE);
            saleRepository.save(sale);
        }
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "date");
        Page<SaleDTO> saleDTOS = saleService.findAll(pageable);

        assertEquals(0, saleDTOS.getNumber(), "Page number must be 0 because method was given a 0 parameter to it");
        assertEquals(10, saleDTOS.getSize(), "Page size must be 10 because method was given a 10 parameter to it");
        assertTrue(saleDTOS.toList().get(0).getDate()
                .compareTo(saleDTOS.toList().get(1).getDate()) < 0);
        assertTrue(saleDTOS.toList().get(1).getDate()
                .compareTo(saleDTOS.toList().get(2).getDate()) < 0);
        assertTrue(saleDTOS.toList().get(2).getDate()
                .compareTo(saleDTOS.toList().get(3).getDate()) < 0);
        assertTrue(saleDTOS.toList().get(3).getDate()
                .compareTo(saleDTOS.toList().get(4).getDate()) < 0);
    }
}
