package com.coincelt.traderapi.domain.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * The TradeType enumeration.
 */
@ToString
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public enum TradeType {
    BUY("buy"), SELL("sell");

    private final String name;

    @JsonValue
    public String getName() {
        return name;
    }

    @JsonCreator
    public static TradeType forName(String name) {
        for (TradeType tradeType : TradeType.values()) {
            if (name.equals(tradeType.getName())) {
                return tradeType;
            }
        }
        return null;
    }
}
