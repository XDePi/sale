package com.depi.sale.service;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

public interface ExcelService {

    /**
     * Imports all data from xls file to DB
     * @param file xls file
     */
    @NotNull
    public void save(MultipartFile file);
}
