package com.coincelt.traderapi.domain.deserializer.kraken;

import com.coincelt.traderapi.domain.Ticker;
import com.coincelt.traderapi.domain.enumeration.Exchange;
import com.coincelt.traderapi.domain.enumeration.ExchangesJsonMessageError;
import com.coincelt.traderapi.domain.util.BigDecimalUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * Custom Jackson deserializer for transforming a JSON object from Ticker
 */
public class TickerDeserializer extends JsonDeserializer<Ticker> {

    @Override
    public Ticker deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);

        if(node.has(ExchangesJsonMessageError.KRAKEN.getName())
                && node.get(ExchangesJsonMessageError.KRAKEN.getName()).isArray()){
            Iterator<JsonNode> errorIterator = node.get(ExchangesJsonMessageError.KRAKEN.getName()).elements();
            if(errorIterator.hasNext()){
                throw new IOException(errorIterator.next().asText());
            }
        }

        if(!node.has("result")){
            throw new IOException("There are not results");
        }

        Exchange exchange = (Exchange)context.findInjectableValue(Exchange.class.getName(), null, null);

        Ticker ticker = new Ticker();
        Iterator<Map.Entry<String, JsonNode>> nodeIterator = node.get("result").fields();
        while(nodeIterator.hasNext()){
            Map.Entry<String, JsonNode> entry = nodeIterator.next();
            JsonNode crypto = entry.getValue();
            ticker.setExchange(exchange);
            ticker.setCurrencyPair(entry.getKey());

            ticker.setLast(BigDecimalUtils.textToBigDecimal(crypto.get("c").get(0).asText()));
            ticker.setLowestAsk(BigDecimalUtils.textToBigDecimal(crypto.get("a").get(0).asText()));
            ticker.setHighestBid(BigDecimalUtils.textToBigDecimal(crypto.get("b").get(0).asText()));
            ticker.setPercentChange(BigDecimalUtils.textToBigDecimal(crypto.get("p").get(0).asText()));
            ticker.setBaseVolume(BigDecimalUtils.textToBigDecimal(crypto.get("v").get(0).asText()));
            ticker.setQuoteVolume(BigDecimalUtils.textToBigDecimal(crypto.get("t").get(0).asText()));
            ticker.setHigh24hr(BigDecimalUtils.textToBigDecimal(crypto.get("h").get(0).asText()));
            ticker.setLow24hr(BigDecimalUtils.textToBigDecimal(crypto.get("o").asText()));
        }

        return ticker;
    }
}
