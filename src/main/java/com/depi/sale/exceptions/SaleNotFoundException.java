package com.depi.sale.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class SaleNotFoundException extends RuntimeException{
    public SaleNotFoundException(Long id) {
        super("Could not find sale " + id);
    }
}
