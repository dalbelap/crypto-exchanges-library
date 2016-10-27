package com.coincelt.traderapi.domain;

import com.coincelt.traderapi.domain.enumeration.Exchange;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * A valid symbol IDs and the pair details.
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Symbol implements Serializable {

    private static final long serialVersionUID = 1L;

    private Exchange exchange;
    private String pair;
    private Integer pricePrecision;
    private BigDecimal initialMargin;
    private BigDecimal minimunMargin;
    private BigDecimal maximunOrderSize;
    private ZonedDateTime expiration;
}
