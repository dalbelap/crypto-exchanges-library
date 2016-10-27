package com.coincelt.traderapi.domain.enumeration;

import com.coincelt.traderapi.domain.exceptions.ExchangeNotSupportedException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * Exchange enumeration name
 */
@ToString
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public enum Exchange {
    POLONIEX("Poloniex"), BITFINEX("Bitfinex"), KRAKEN("Kraken");

    private final String name;

    @JsonValue
    public String getName() {
        return name;
    }

    @JsonCreator
    public static Exchange forName(String name) throws ExchangeNotSupportedException {
        for (Exchange exchange : Exchange.values()) {
            if (name.equalsIgnoreCase(exchange.getName())) {
                return exchange;
            }
        }
        throw new ExchangeNotSupportedException(name);
    }
}
