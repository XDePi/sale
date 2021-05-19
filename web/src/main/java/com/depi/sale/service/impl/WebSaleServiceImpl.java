package com.depi.sale.service.impl;

import com.depi.sale.dto.SaleDTO;
import com.depi.sale.entity.Sale;
import com.depi.sale.exceptions.SaleNotFoundException;
import com.depi.sale.repository.SaleRepository;
import com.depi.sale.service.WebSaleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class WebSaleServiceImpl implements WebSaleService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    SaleRepository saleRepository;

    @Override
    @Transactional
    public Page<SaleDTO> findAll(Optional<String> sort, Optional<Integer> page, Optional<Integer> pageSize, Pageable pageable) {

        Page<Sale> sales = saleRepository.findAll(
                PageRequest.of(page.orElse(0), pageSize.orElse(10), Sort.by(sort.orElse("customerName"))));
        return sales.map(this::convertToDto);
    }

    @Override
    @Transactional
    public SaleDTO newSale(Sale newSale) {
        Sale sale = saleRepository.save(newSale);
        return convertToDto(sale);
    }

    @Override
    public SaleDTO getById(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException(id));
        return convertToDto(sale);
    }

    @Override
    @Transactional
    public SaleDTO replaceSale(Sale newSale, Long id) {
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

    @Override
    @Transactional
    public void deleteSale(Long id) {
        saleRepository.deleteById(id);
    }

    private SaleDTO convertToDto(Sale sale) {
        return modelMapper.map(sale, SaleDTO.class);
    }

    private Sale convertToEntity(SaleDTO saleDTO) {
        return modelMapper.map(saleDTO, Sale.class);
    }
}
