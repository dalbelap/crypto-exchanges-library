package com.coincelt.traderapi.domain;

import com.coincelt.traderapi.domain.enumeration.Exchange;
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
 * A loan history
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoanHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @JacksonInject
    private Exchange exchange;
    private String currency;
    private BigDecimal rate;
    private BigDecimal amountLent;
    private BigDecimal amountUsed;
    private ZonedDateTime date;
}
