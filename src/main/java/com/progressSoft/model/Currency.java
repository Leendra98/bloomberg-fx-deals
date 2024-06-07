package com.progressSoft.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Table(name = "currencies")
@Entity
@Getter
@Setter
public class Currency {

    @Id
    @Size(min = 3, max = 3, message = "Currency code must be 3 characters")
    @Column(name = "currency_code", length = 3)
    private String currencyCode;

    @NotNull(message = "Currency name cannot be null")
    @Size(max = 50, message = "Currency name must be less than 50 characters")
    @Column(name = "currency_name", length = 50)
    private String currencyName;

    @OneToMany(mappedBy = "fromCurrency")
    private Set<Deal> dealsFrom;

    @OneToMany(mappedBy = "toCurrency")
    private Set<Deal> dealsTo;
}
