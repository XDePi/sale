package com.depi.sale.dto;


import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class SaleDTO {

    @NonNull
    private long id;

    @NonNull
    private Date date;

    @NonNull
    private String customerName;

    @NonNull
    private BigDecimal amount;

}
