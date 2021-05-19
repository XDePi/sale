package com.depi.sale.service;

import com.depi.sale.dto.SaleDTO;
import com.depi.sale.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface WebSaleService {

    @NotNull
    Page<SaleDTO> findAll(Optional<String> sort, Optional<Integer> page, Optional<Integer> pageSize, Pageable pageable);

    @NotNull
    SaleDTO newSale(Sale sale);

    @NotNull
    SaleDTO getById(Long id);

    @NotNull
    SaleDTO replaceSale(Sale sale, Long id);

    @NotNull
    void deleteSale(Long id);
}
