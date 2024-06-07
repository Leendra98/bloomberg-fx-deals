package com.progressSoft.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Table(name = "deals")
@Entity
@Getter
@Setter
public class Deal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Unique ID cannot be null")
    @Column(name = "unique_id", unique = true)
    private String uniqueId;

    @NotNull(message = "From Currency cannot be null")
    private String fromCurrency;

    @NotNull(message = "To Currency cannot be null")
    private String toCurrency;

    @NotNull(message = "Deal timestamp cannot be null")
    @Column(name = "deal_timestamp")
    private LocalDateTime dealTimestamp;

    @Positive(message = "Deal amount must be positive")
    @Column(name = "deal_amount", precision = 15, scale = 2)
    private Double dealAmount;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
