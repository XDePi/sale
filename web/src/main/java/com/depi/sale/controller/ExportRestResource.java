package com.depi.sale.controller;

import feed.exports.AppExportService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
public class ExportRestResource {

    @Autowired
    private AppExportService appExportService;

    @ApiOperation(value = "Downloads an excel file from the DB")
    @GetMapping("/sales/download")
    public ResponseEntity<ByteArrayResource> download() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sales.xlsx");
        File file = appExportService.exportMappings();
        byte[] data = Files.readAllBytes(file.toPath());
        ByteArrayResource resource = new ByteArrayResource(data);
        Files.delete(file.toPath());

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(data.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
