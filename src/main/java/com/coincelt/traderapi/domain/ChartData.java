package com.coincelt.traderapi.domain;

import com.coincelt.traderapi.domain.enumeration.Exchange;
import com.coincelt.traderapi.domain.enumeration.PeriodTime;
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
 * A ChartData.
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChartData implements Serializable {

    private static final long serialVersionUID = 1L;

    @JacksonInject
    private Exchange exchange;
    private String currencyPair;
    private ZonedDateTime date;
    private PeriodTime period;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal open;
    private BigDecimal close;
    private BigDecimal volume;
    private BigDecimal quoteVolume;
    private BigDecimal weightedAverage;
}
