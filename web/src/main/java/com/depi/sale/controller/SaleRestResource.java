package com.depi.sale.controller;

import com.depi.sale.DTO.SaleDTO;
import com.depi.sale.entity.Sale;
import com.depi.sale.exceptions.SaleNotFoundException;
import com.depi.sale.repository.SaleRepository;
import com.depi.sale.service.SaleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class SaleRestResource {

    @Autowired
    SaleService saleService;

    @GetMapping("/sales")
    Page<SaleDTO> findAll(@RequestParam Optional<Integer> page,
                          @RequestParam Optional<Integer> pageSize) {
        return saleService.findAll(PageRequest.of(page.orElse(0), pageSize.orElse(10), Sort.by("customerName")));

    }

    @PostMapping("/sales")
    SaleDTO newSale(@RequestBody Sale newSale) {
        return saleService.newSale(newSale);
    }

    @GetMapping("/sales/{id}")
    SaleDTO one(@PathVariable Long id) {
        return saleService.getById(id);
    }

    @PutMapping("/sales/{id}")
    SaleDTO replaceSale(@RequestBody Sale newSale,
                     @PathVariable Long id) {
        return saleService.replaceSale(newSale, id);
    }

    @DeleteMapping("/sales/{id}")
    void deleteSale(@PathVariable Long id) {
        saleService.deleteSale(id);
    }

}
