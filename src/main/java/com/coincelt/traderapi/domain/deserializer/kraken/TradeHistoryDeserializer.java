package com.coincelt.traderapi.domain.deserializer.kraken;

import com.coincelt.traderapi.domain.TradeHistory;
import com.coincelt.traderapi.domain.enumeration.Exchange;
import com.coincelt.traderapi.domain.enumeration.ExchangesJsonMessageError;
import com.coincelt.traderapi.domain.enumeration.TradeType;
import com.coincelt.traderapi.domain.util.BigDecimalUtils;
import com.coincelt.traderapi.domain.util.ZonedDateTimeUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Custom Jackson deserializer for transforming a JSON object from TradeHistory
 */
public class TradeHistoryDeserializer extends JsonDeserializer<List<TradeHistory>> {

    @Override
    public List<TradeHistory> deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);
        List<TradeHistory> result = new ArrayList<>();
        Exchange exchange = (Exchange)context.findInjectableValue(Exchange.class.getName(), null, null);
        String currencyPair = (String)context.findInjectableValue(String.class.getName(), null, null);

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

        if(node.get("result").elements().hasNext()) {
            Iterator<JsonNode> nodeIterator = node.get("result").elements().next().elements();
            while (nodeIterator.hasNext()) {
                JsonNode trade = nodeIterator.next();
                TradeHistory entity = new TradeHistory();
                entity.setExchange(exchange);
                entity.setCurrencyPair(currencyPair);
                entity.setRate(BigDecimalUtils.textToBigDecimal(trade.get(0).textValue()));
                entity.setAmount(BigDecimalUtils.textToBigDecimal(trade.get(1).textValue()));
                entity.setDate(ZonedDateTimeUtils.timestampAsDoubleToZonedDateTime(trade.get(2).asDouble()));
                entity.setType(trade.get(3).asText().equals("s") ? TradeType.SELL : TradeType.BUY);
                entity.setTotal(entity.getRate().multiply(entity.getAmount()));
                result.add(entity);
            }
        }

        return result;
    }
}
