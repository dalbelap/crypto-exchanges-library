package com.coincelt.traderapi.domain;

import com.coincelt.traderapi.domain.enumeration.Exchange;
import com.coincelt.traderapi.domain.enumeration.TradeType;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * Returns the past 200 trades for a given market, or all of the trades between a range specified in UNIX timestamps by the "start" and "end" GET parameters.
 * 
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TradeHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @JacksonInject
    private Exchange exchange;
    private String currencyPair;
    private Long globalTradeID;
    private Long tradeID;
    private ZonedDateTime date;
    private TradeType type;
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal total;

}
