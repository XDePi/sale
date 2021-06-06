package com.depi.sale.service;

import com.depi.sale.dto.SaleDTO;
import com.depi.sale.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

public interface WebSaleService {

    /**
     * Get page of sales
     * @param pageable parameters of page number, sort options and page size
     * @return Page of existing sales
     */
    @NotNull
    Page<SaleDTO> findAll(@Nullable Pageable pageable);

    /**
     * Allows to POST new sale entity
     * @param sale An entity needed to post to DB
     * @return converted saleDTO class which goes to DB
     */
    @NotNull
    SaleDTO newSale(Sale sale);

    /**
     * Get Sale
     * @param id identifier of Sale
     * @return A single sale entity converted to DTO
     */
    SaleDTO getById(Long id);

    /**
     * Allows to use PUT method on DB with the help of which you can update an existing entity
     * @param sale Sale entity with new parameters you want to put in existing instance
     * @param id identifier of existing Sale
     * @return
     */
    @NotNull
    SaleDTO replaceSale(Sale sale);

    /**
     * Deletes an existing entity from the DB and representation
     * @param id identifier of existing Sale
     */
    void deleteSale(Long id);
}
