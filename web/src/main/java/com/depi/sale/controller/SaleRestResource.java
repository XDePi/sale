package com.depi.sale.controller;

import com.depi.sale.dto.SaleDTO;
import com.depi.sale.entity.Sale;
import com.depi.sale.service.WebSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class SaleRestResource {

    @Autowired
    WebSaleService webSaleService;

    @GetMapping("/sales")
    Page<SaleDTO> findAll(@RequestParam Optional<Integer> page,
                          @RequestParam Optional<Integer> pageSize,
                          @RequestParam Optional<String> sort,
                          Pageable pageable) {
        return webSaleService.findAll(sort, page, pageSize, pageable);

    }

    @PostMapping("/sales")
    SaleDTO newSale(@RequestBody Sale newSale) {
        return webSaleService.newSale(newSale);
    }

    @GetMapping("/sales/{id}")
    SaleDTO one(@PathVariable Long id) {
        return webSaleService.getById(id);
    }

    @PutMapping("/sales/{id}")
    SaleDTO replaceSale(@RequestBody Sale newSale,
                     @PathVariable Long id) {
        return webSaleService.replaceSale(newSale, id);
    }

    @DeleteMapping("/sales/{id}")
    void deleteSale(@PathVariable Long id) {
        webSaleService.deleteSale(id);
    }

}
