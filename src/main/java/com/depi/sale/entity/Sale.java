package com.depi.sale.entity;

import lombok.Data;
import lombok.NonNull;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import javax.persistence.*;
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
    private long id;

    @Column()
    @CreationTimestamp
    @NonNull
    private Date date;

    @Column
    @NotBlank
    @NonNull
    private String customer_name;

    @Column
    @NonNull
    @Positive
    private BigDecimal amount;

    public Sale() {
    }
}
