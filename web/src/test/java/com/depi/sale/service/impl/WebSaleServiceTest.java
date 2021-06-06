package com.depi.sale.service.impl;

import com.depi.sale.dto.SaleDTO;
import com.depi.sale.entity.Sale;
import com.depi.sale.repository.SaleRepository;
import com.depi.sale.service.impl.WebSaleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class WebSaleServiceTest {

    private static final Long TEST_SALE_ID = 1L;

    private WebSaleServiceImpl saleService;

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        saleService = new WebSaleServiceImpl(saleRepository, modelMapper);
    }

    @Test
    void getById_when_invoked_then_returns_saleDTO_entity_with_id_given() {

        Sale sale = new Sale();
        sale.setId(TEST_SALE_ID);
        sale.setDate(new Date());
        sale.setCustomerName("TESTED");
        sale.setAmount(BigDecimal.ONE);

        SaleDTO saleDTO = new SaleDTO();
        saleDTO.setId(TEST_SALE_ID);
        saleDTO.setDate(new Date());
        saleDTO.setCustomerName("TESTED");
        saleDTO.setAmount(BigDecimal.ONE);

        when(saleRepository.findById(TEST_SALE_ID)).thenReturn(Optional.of(sale));
        when(saleService.getById(TEST_SALE_ID)).thenReturn(saleDTO);
        SaleDTO actual = saleService.getById(TEST_SALE_ID);

        assertNotNull(actual, "Actual was returned");
        assertEquals(saleDTO.getId(), actual.getId(), "Actual assigned to sale id");
        assertEquals(saleDTO.getDate(), actual.getDate(), "Actual date assigned to sale date");
        assertEquals(saleDTO.getCustomerName(), actual.getCustomerName(), "Actual name assigned to sale name");
        assertEquals(saleDTO.getAmount(), actual.getAmount(), "Actual amount assigned to sale amount");
    }

    @Test
    void newSale_when_invoked_then_saleService_saves_new_entity_to_DB() {
        Sale sale = new Sale();
        sale.setId(TEST_SALE_ID);
        sale.setDate(new Date());
        sale.setCustomerName("TESTED");
        sale.setAmount(BigDecimal.ONE);

        SaleDTO saleDTO = new SaleDTO();
        saleDTO.setId(TEST_SALE_ID);
        saleDTO.setDate(new Date());
        saleDTO.setCustomerName("TESTED");
        saleDTO.setAmount(BigDecimal.ONE);

        when(saleRepository.save(sale)).thenReturn(sale);
        when(saleService.newSale(sale)).thenReturn(saleDTO);

        SaleDTO actual = saleService.newSale(sale);
        assertNotNull(actual, "Actual was returned");
        assertEquals(sale.getId(), actual.getId(), "Actual assigned to sale id");
        assertEquals(sale.getDate(), actual.getDate(), "Actual date assigned to sale date");
        assertEquals(sale.getCustomerName(), actual.getCustomerName(), "Actual name assigned to sale name");
        assertEquals(sale.getAmount(), actual.getAmount(), "Actual amount assigned to sale amount");

    }

//    @Test
    void findAll_when_invoked_then_returns_page_of_two_entities_with_size_2_and_number_0() {
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

        SaleDTO saleDTO = new SaleDTO();
        saleDTO.setId(1);
        saleDTO.setDate(new Date());
        saleDTO.setCustomerName("TESTED");
        saleDTO.setAmount(BigDecimal.TEN);

        SaleDTO saleDTO2 = new SaleDTO();
        saleDTO2.setId(2);
        saleDTO2.setDate(new Date());
        saleDTO2.setCustomerName("TESTED2");
        saleDTO2.setAmount(BigDecimal.TEN);

        Pageable pageable = PageRequest.of(0, 2);
        List<Sale> sales = new ArrayList<>();
        sales.add(sale);
        sales.add(sale1);
        Page<Sale> salesPage = new PageImpl<>(sales, pageable, sales.size());

        List<SaleDTO> saleDTOS = new ArrayList<>();
        saleDTOS.add(saleDTO);
        saleDTOS.add(saleDTO2);
        Page<SaleDTO> saleDTOPage = new PageImpl<>(saleDTOS, pageable, saleDTOS.size());

        when(saleRepository.findAll(pageable)).thenReturn(salesPage);
        when(saleService.findAll(pageable)).thenReturn(saleDTOPage);

        Page<SaleDTO> actual = saleService.findAll(pageable);

        assertNotNull(actual);
        assertEquals(salesPage.getTotalPages(), actual.getTotalPages());
        assertEquals(salesPage.getTotalElements(), actual.getTotalElements());
        assertEquals(salesPage.getNumber(), actual.getNumber());
        assertEquals(salesPage.get().findFirst().get().getId(), actual.get().findFirst().get().getId(),"salesPage first element must be equal to actual first element because these must be the same elements");
    }

    @Test
    void deleteSale_when_invoked_then_deletes_sale_entity_from_DB() {
        Sale sale = new Sale();
        sale.setId(TEST_SALE_ID);
        sale.setDate(new Date());
        sale.setCustomerName("TESTED");
        sale.setAmount(BigDecimal.ONE);

        SaleDTO saleDTO = new SaleDTO();
        saleDTO.setId(TEST_SALE_ID);
        saleDTO.setDate(new Date());
        saleDTO.setCustomerName("TESTED");
        saleDTO.setAmount(BigDecimal.ONE);

        saleService.deleteSale(TEST_SALE_ID);
        verify(saleRepository, times(1)).deleteById(TEST_SALE_ID);

    }
}
