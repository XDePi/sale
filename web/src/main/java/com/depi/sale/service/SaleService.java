package com.depi.sale.service;

import com.depi.sale.DTO.SaleDTO;
import com.depi.sale.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SaleService {

    Page<SaleDTO> findAll(Pageable pageable);

    SaleDTO newSale(Sale sale);

    SaleDTO getById(Long id);

    SaleDTO replaceSale(Sale sale, Long id);

    void deleteSale(Long id);
}
