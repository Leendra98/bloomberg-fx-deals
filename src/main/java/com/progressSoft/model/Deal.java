package com.progressSoft.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Deal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Unique ID is mandatory")
    private String uniqueId;

    @NotBlank(message = "From Currency is mandatory")
    private String fromCurrency;

    @NotBlank(message = "To Currency is mandatory")
    private String toCurrency;

    @NotNull(message = "Deal timestamp is mandatory")
    private LocalDateTime dealTimestamp;

    @NotNull(message = "Deal amount is mandatory")
    @Positive(message = "Deal amount must be positive")
    private Double dealAmount;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
