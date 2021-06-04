package com.depi.sale.integration;

import com.depi.sale.SaleApplication;
import com.depi.sale.repository.SaleRepository;
import com.depi.sale.service.WebSaleService;
import feed.exports.AppExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = {SaleApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseIT {

    @Autowired
    protected SaleRepository saleRepository;

    @Autowired
    protected AppExportService appExportService;

    @Autowired
    protected WebSaleService saleService;
}
