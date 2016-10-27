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

/**
 * A Currency
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Currency implements Serializable {

    private static final long serialVersionUID = 1L;

    @JacksonInject
    private Exchange exchange;
    private String currency;
    private String name;
    private BigDecimal txFee;
    private Integer minConf;
    private Boolean disabled;
    private Boolean delisted;
    private Boolean frozen;
    private Integer decimals;
    private Integer displayDecimals;
}
