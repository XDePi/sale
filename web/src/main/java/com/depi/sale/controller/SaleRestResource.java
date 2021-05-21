package com.depi.sale.controller;

import com.depi.sale.dto.SaleDTO;
import com.depi.sale.entity.Sale;
import com.depi.sale.repository.SaleRepository;
import com.depi.sale.service.WebSaleService;
import exporter.SaleExcelExporter;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
public class SaleRestResource {

    @Autowired
    WebSaleService webSaleService;
    @Autowired
    SaleRepository saleRepository;

    @ApiOperation(value = "List of sales", notes = "Get list of sales filtered by name as default")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",
                    value = "Page number starting from 0",
                    paramType = "query",
                    dataType = "int"),
            @ApiImplicitParam(name = "size",
                    value = "Page size (10 by default)",
                    paramType = "query",
                    dataType = "int"),
            @ApiImplicitParam(name = "sort",
                    value = "Sort parameter, which sorts by customer name as default",
                    paramType = "query", dataType = "string")

    })
    @GetMapping("/sales")
    Page<SaleDTO> findAll(@PageableDefault
                          @SortDefault(sort = "customerName", direction = Sort.Direction.ASC)
                                  @ApiIgnore Pageable pageable) {
        return webSaleService.findAll(pageable);

    }

    @ApiOperation(value = "Get sale by Sale ID", notes = "Get sale by Sale ID")
    @GetMapping("/sales/{id}")
    SaleDTO one(@ApiParam(value = "Sale ID", required = true)
                @PathVariable Long id) {
        return webSaleService.getById(id);
    }

    @GetMapping("/sales/download")
    public void exportToExcel(HttpServletResponse response,
                              Pageable pageable) throws IOException {
        response.setContentType("application/json");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=sales.xlsx";

        response.setHeader(headerKey, headerValue);

        Page<Sale> sales = saleRepository.findAll(pageable);

        SaleExcelExporter excelExporter = new SaleExcelExporter(sales);
        excelExporter.export(response);
    }

    @ApiOperation(value = "Post new sale entity to DB", notes = "Post new sale entity to DB")
    @PostMapping("/sales")
    SaleDTO newSale(@ApiParam(value = "New Sale entity", required = true)
                    @RequestBody Sale newSale) {
        return webSaleService.newSale(newSale);
    }

    @ApiOperation(value = "Update existing Sale entity in DB", notes = "Update existing Sale entity")
    @PutMapping("/sales/{id}")
    SaleDTO replaceSale(@ApiParam(value = "Sale entity", required = true)
                        @RequestBody Sale newSale,
                        @ApiParam(value = "Sale ID which needed to be updated")
                        @PathVariable Long id) {
        return webSaleService.replaceSale(newSale, id);
    }

    @ApiOperation(value = "Delete existing Sale entity from DB", notes = "Delete existing Sale entity")
    @DeleteMapping("/sales/{id}")
    void deleteSale(@ApiParam(value = "Sale ID", required = true)
                    @PathVariable Long id) {
        webSaleService.deleteSale(id);
    }

}
