package com.depi.sale.controller;

import com.depi.sale.DTO.SaleDTO;
import com.depi.sale.entity.Sale;
import com.depi.sale.exceptions.SaleNotFoundException;
import com.depi.sale.repository.SaleRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class SaleRestResource {

    @Autowired
    SaleRepository saleRepository;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/sales")
    Page<SaleDTO> findAll(@RequestParam Optional<Integer> page,
                          @RequestParam Optional<Integer> pageSize) {
        Page<Sale> sales = saleRepository.findAll(PageRequest.of(page.orElse(0), pageSize.orElse(10), Sort.by("customerName")));
        Page<SaleDTO> saleDTOS = sales.map(this::convertToDto);
        return saleDTOS;

    }

    @PostMapping("/sales")
    SaleDTO newSale(@RequestBody Sale newSale) {
        Sale sale = saleRepository.save(newSale);
        return convertToDto(sale);
    }

    @GetMapping("/sales/{id}")
    SaleDTO one(@PathVariable Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException(id));
        return convertToDto(sale);
    }

    @PutMapping("/sales/{id}")
    SaleDTO replaceSale(@RequestBody Sale newSale,
                     @PathVariable Long id) {
        Sale sale1 = saleRepository.findById(id)
                .map(sale -> {
                    sale.setDate(newSale.getDate());
                    sale.setCustomerName(newSale.getCustomerName());
                    sale.setAmount(newSale.getAmount());
                    return saleRepository.save(sale);
                })
                .orElseThrow(() -> new SaleNotFoundException(id));
        return convertToDto(sale1);
    }

    @DeleteMapping("/sales/{id}")
    void deleteSale(@PathVariable Long id) {
        saleRepository.deleteById(id);
    }

    private SaleDTO convertToDto(Sale sale) {
        SaleDTO saleDTO = modelMapper.map(sale, SaleDTO.class);
        return saleDTO;
    }

    private Sale convertToEntity(SaleDTO saleDTO) {
        Sale sale = modelMapper.map(saleDTO, Sale.class);
        return sale;
    }
}
