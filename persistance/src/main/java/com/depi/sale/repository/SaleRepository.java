package com.depi.sale.repository;

import com.depi.sale.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
public interface SaleRepository extends PagingAndSortingRepository<Sale, Long> {

    Page<Sale> findAll(@NotNull Pageable pageable);
    List<Sale> findAll();

}
