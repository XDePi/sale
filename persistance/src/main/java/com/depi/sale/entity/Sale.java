package com.depi.sale.entity;

import com.poiji.annotation.ExcelCellName;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "sale")
@Data
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @NonNull
    @ExcelCellName("sale_id")
    private long id;

    @Column()
    @CreationTimestamp
    @NonNull
    @ExcelCellName("date")
    private Date date;

    @Column(name = "customer_name")
    @NotBlank
    @NonNull
    @ExcelCellName("customer_name")
    private String customerName;

    @Column
    @NonNull
    @Positive
    @ExcelCellName("amount")
    private BigDecimal amount;

    public Sale() {
    }

}
