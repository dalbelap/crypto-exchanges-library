package com.coincelt.traderapi.domain;

import com.coincelt.traderapi.domain.enumeration.Exchange;
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
 * Returns the ticker for a given Exchange and currency pair Market
 * 
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ticker implements Serializable {

    private static final long serialVersionUID = 1L;

    @JacksonInject
    private Exchange exchange;
    private String currencyPair;
    private BigDecimal middle;
    private BigDecimal last;
    private BigDecimal lowestAsk;
    private BigDecimal highestBid;
    private BigDecimal percentChange;
    private BigDecimal baseVolume;
    private BigDecimal quoteVolume;
    private BigDecimal high24hr;
    private BigDecimal low24hr;
    private Boolean isFrozen;
    private ZonedDateTime date;
}
