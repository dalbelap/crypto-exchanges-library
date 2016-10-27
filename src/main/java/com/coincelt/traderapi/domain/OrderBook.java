package com.coincelt.traderapi.domain;

import com.coincelt.traderapi.domain.enumeration.Exchange;
import com.coincelt.traderapi.domain.enumeration.OrderBookType;
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
 * Returns the order book for a given market, as well as a sequence number for use with the Push API and an indicator specifying whether the market is frozen. You may set currencyPair to "all" to get the order books of all markets.
 * 
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderBook implements Serializable {

    private static final long serialVersionUID = 1L;

    private String currencyPair;
    @JacksonInject
    private Exchange exchange;
    private BigDecimal amount;
    private BigDecimal rate;
    private OrderBookType orderBookType;
    private ZonedDateTime date;
}
