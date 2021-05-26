package com.depi.sale.controller;

import feed.exports.AppExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
public class ExportRestResource {

    @Autowired
    private AppExportService appExportService;

    //  @ApiOperation(value = "Download instance of the DB", notes = "Export in excel file")
    //  @GetMapping("/sales/download")
    //  public void exportToExcel(HttpServletResponse response) throws IOException {
    //      response.setContentType("application/octet-stream");
    //      String headerKey = "Content-Disposition";
    //      String headerValue = "attachment; filename=sales.xlsx";
    //      response.setHeader(headerKey, headerValue);
//
    //      File file = appExportService.exportMappings();
    //      SaleExcelExporter saleExcelExporter = new SaleExcelExporter(f);
    //      saleExcelExporter.export(response);
    //  }

    @GetMapping("/sales/download")
    public ResponseEntity<Resource> download() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sales.xlsx");
        File file = appExportService.exportMappings();

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
