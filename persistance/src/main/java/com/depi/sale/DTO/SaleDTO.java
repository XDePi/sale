package com.depi.sale.DTO;


import com.depi.sale.entity.Sale;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

public class SaleDTO {

    @NonNull @Getter @Setter
    private long id;

    @NonNull @Getter @Setter
    private Date date;

    @NonNull @Getter @Setter
    private String customerName;

    @NonNull @Getter @Setter
    private BigDecimal amount;

}
