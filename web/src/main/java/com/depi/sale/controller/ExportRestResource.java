package com.depi.sale.controller;

import feed.exports.AppExportService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
public class ExportRestResource {

    @Autowired
    private AppExportService appExportService;

    @ApiOperation(value = "Downloads an excel file from the DB")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",
                    value = "Sale ID which is used to mark the start point of downloading",
                    paramType = "query",
                    dataType = "long"),
            @ApiImplicitParam(name = "page",
                    value = "Page number starting from 0",
                    paramType = "query",
                    dataType = "int"),
            @ApiImplicitParam(name = "size",
                    value = "Page size (15 by default)",
                    paramType = "query",
                    dataType = "int")
    })
    @GetMapping("/sales/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable Long id,
                                             @PageableDefault(size = 15) Pageable pageable) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sales.xlsx");
        File file = appExportService.exportMappings(id, pageable);

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
