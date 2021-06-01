package com.depi.sale.repository;

import com.depi.sale.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
public interface SaleRepository extends PagingAndSortingRepository<Sale, Long> {

    /**
     * Get page of sales
     * @param pageable parameters of page number, sort options and page size
     * @return Page of existing sales
     */
    Page<Sale> findAll(@NotNull Pageable pageable);

    /**
     * Searches through all the entities starting from the particular identifier
     * @param from starting identifier
     * @param pageable parameters of page number, sort options and page size
     * @return Page of existing sales that starts from the particular identifier
     */
    Page<Sale> findByIdGreaterThan(long from, @Nullable Pageable pageable);

    /**
     * Counts through all the entities starting from the particular identifier
     * @param from starting identifier
     * @return the amount of sale entities
     */
    Long countByIdGreaterThan(Long from);

    /**
     * Looks through all the entities in order to find one with the name given
     * @param name customer name
     * @return sale entity
     */
    Sale findByCustomerName(String name);

}
