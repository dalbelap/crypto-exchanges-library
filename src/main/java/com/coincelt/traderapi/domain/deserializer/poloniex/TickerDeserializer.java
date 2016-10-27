package com.coincelt.traderapi.domain.deserializer.poloniex;

import com.coincelt.traderapi.domain.Ticker;
import com.coincelt.traderapi.domain.enumeration.Exchange;
import com.coincelt.traderapi.domain.enumeration.ExchangesJsonMessageError;
import com.coincelt.traderapi.domain.util.BigDecimalUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Custom Jackson deserializer for transforming a JSON object from Ticker
 */
public class TickerDeserializer extends JsonDeserializer<List<Ticker>> {

    @Override
    public List<Ticker> deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);
        List<Ticker> result = new ArrayList<>();
        Exchange exchange = (Exchange)context.findInjectableValue(Exchange.class.getName(), null, null);

        if(node.has(ExchangesJsonMessageError.POLONIEX.getName())){
            throw new IOException(node.get(ExchangesJsonMessageError.POLONIEX.getName()).textValue());
        }

        Iterator<Map.Entry<String, JsonNode>> nodeIterator = node.fields();
        while(nodeIterator.hasNext()){
            Map.Entry<String, JsonNode> entry = nodeIterator.next();
            JsonNode crypto = entry.getValue();
            Ticker ticker = new Ticker();
            ticker.setCurrencyPair(entry.getKey());
            ticker.setExchange(exchange);
            ticker.setLast(BigDecimalUtils.textToBigDecimal(crypto.get("last").asText()));
            ticker.setLowestAsk(BigDecimalUtils.textToBigDecimal(crypto.get("lowestAsk").asText()));
            ticker.setHighestBid(BigDecimalUtils.textToBigDecimal(crypto.get("highestBid").asText()));
            ticker.setPercentChange(BigDecimalUtils.textToBigDecimal(crypto.get("percentChange").asText()));
            ticker.setBaseVolume(BigDecimalUtils.textToBigDecimal(crypto.get("baseVolume").asText()));
            ticker.setQuoteVolume(BigDecimalUtils.textToBigDecimal(crypto.get("quoteVolume").asText()));
            ticker.setHigh24hr(BigDecimalUtils.textToBigDecimal(crypto.get("high24hr").asText()));
            ticker.setLow24hr(BigDecimalUtils.textToBigDecimal(crypto.get("low24hr").asText()));
            ticker.setIsFrozen(crypto.get("isFrozen").intValue()==0 ? false : true);
            ticker.setMiddle((ticker.getLowestAsk().add(ticker.getHighestBid()).divide(new BigDecimal(2), BigDecimalUtils.DECIMAL_SCALE, BigDecimalUtils.ROUNDING_MODE)));
            result.add(ticker);
        }
        return result;
    }
}
