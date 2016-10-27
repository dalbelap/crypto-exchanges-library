package com.coincelt.traderapi.domain.deserializer.bitfinex;

import com.coincelt.traderapi.domain.Ticker;
import com.coincelt.traderapi.domain.enumeration.Exchange;
import com.coincelt.traderapi.domain.enumeration.ExchangesJsonMessageError;
import com.coincelt.traderapi.domain.util.BigDecimalUtils;
import com.coincelt.traderapi.domain.util.ZonedDateTimeUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

/**
 * Custom Jackson deserializer for transforming a JSON object from Ticker
 */
public class TickerDeserializer extends JsonDeserializer<Ticker> {

    @Override
    public Ticker deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);

        if(node.has(ExchangesJsonMessageError.BITFINEX.getName())){
            throw new IOException(node.get(ExchangesJsonMessageError.BITFINEX.getName()).textValue());
        }

        Ticker ticker = new Ticker();
        Exchange exchange = (Exchange)context.findInjectableValue(Exchange.class.getName(), null, null);
        String pair = (String)context.findInjectableValue(String.class.getName(), null, null);
        
        ticker.setExchange(exchange);
        ticker.setCurrencyPair(pair);
        ticker.setMiddle(BigDecimalUtils.textToBigDecimal(node.get("mid").asText()));
        ticker.setLast(BigDecimalUtils.textToBigDecimal(node.get("last_price").asText()));
        ticker.setLowestAsk(BigDecimalUtils.textToBigDecimal(node.get("ask").asText()));
        ticker.setHighestBid(BigDecimalUtils.textToBigDecimal(node.get("bid").asText()));
        ticker.setBaseVolume(BigDecimalUtils.textToBigDecimal(node.get("volume").asText()));
        ticker.setHigh24hr(BigDecimalUtils.textToBigDecimal(node.get("high").asText()));
        ticker.setLow24hr(BigDecimalUtils.textToBigDecimal(node.get("low").asText()));
        double posixTimeStamp = Double.valueOf(node.get("timestamp").asText());
        ticker.setDate(ZonedDateTimeUtils.timestampToZonedDateTime((long) posixTimeStamp));

        return ticker;
    }
}
