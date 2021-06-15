package com.depi.sale.service.impl;

import com.depi.sale.entity.Sale;
import com.depi.sale.repository.SaleRepository;
import com.depi.sale.service.ExcelService;
import imports.ExcelHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    SaleRepository saleRepository;

    @Override
    public void save(MultipartFile file) {
        try {
            List<Sale> sales = ExcelHelper.excelToSales(file.getInputStream());
            saleRepository.saveAll(sales);
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }
}
