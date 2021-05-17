package com.depi.sale.controller;

import com.depi.sale.entity.Sale;
import com.depi.sale.exceptions.SaleNotFoundException;
import com.depi.sale.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SaleRestResource {

    @Autowired
    SaleRepository saleRepository;

    @GetMapping("/sales")
    List<Sale> all() {
        return saleRepository.findAll();
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
                .orElseGet(() -> {
                    newSale.setId(id);
                    return saleRepository.save(newSale);
                });
    }

    @DeleteMapping("/sales/{id}")
    void deleteSale(@PathVariable Long id) {
        saleRepository.deleteById(id);
    }
}
