package com.depi.sale.controller;

import com.depi.sale.entity.Sale;
import com.depi.sale.exceptions.SaleNotFoundException;
import com.depi.sale.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class SaleRestResource {

    @Autowired
    SaleRepository saleRepository;

    @GetMapping("/sales")
    Page<Sale> findAll(@RequestParam Optional<Integer> page,
                       @RequestParam Optional<Integer> pageSize) {
        return saleRepository.findAll(PageRequest.of(page.orElse(0), pageSize.orElse(10), Sort.by("customerName")));
    }

    @PostMapping("/sales")
    Sale newSale(@RequestBody Sale newSale) {
        return saleRepository.save(newSale);
    }

    @GetMapping("/sales/{id}")
    Sale one(@PathVariable Long id) {
        return saleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException(id));
    }

    @PutMapping("/sales/{id}")
    Sale replaceSale(@RequestBody Sale newSale,
                     @PathVariable Long id) {
        return saleRepository.findById(id)
                .map(sale -> {
                    sale.setDate(newSale.getDate());
                    sale.setCustomerName(newSale.getCustomerName());
                    sale.setAmount(newSale.getAmount());
                    return saleRepository.save(sale);
                })
                .orElseThrow(() -> new SaleNotFoundException(id));
    }

    @DeleteMapping("/sales/{id}")
    void deleteSale(@PathVariable Long id) {
        saleRepository.deleteById(id);
    }
}
