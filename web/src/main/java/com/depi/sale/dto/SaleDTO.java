package com.depi.sale.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ApiModel("Sale")
public class SaleDTO {

    @NonNull
    @ApiModelProperty(value = "Sale ID", required = true)
    private long id;

    @NonNull
    @ApiModelProperty(value = "Sale date", required = true)
    private Date date;

    @NonNull
    @ApiModelProperty(value = "Sale customerName", required = true)
    private String customerName;

    @NonNull
    @ApiModelProperty(value = "Sale amount", required = true)
    private BigDecimal amount;

}
