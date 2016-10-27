package com.coincelt.traderapi.domain;

import com.coincelt.traderapi.domain.enumeration.Exchange;
import com.coincelt.traderapi.domain.enumeration.LoanOrderBookType;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * A loan order
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoanOrderBook implements Serializable {

    private static final long serialVersionUID = 1L;

    @JacksonInject
    private Exchange exchange;
    private String currency;
    private LoanOrderBookType loanOrderBookType;
    private BigDecimal rate;
    private BigDecimal amount;
    private Integer rangeMin;
    private Integer rangeMax;
    private Integer period;
    private ZonedDateTime date;
    private Boolean FlashReturnRate;
}
