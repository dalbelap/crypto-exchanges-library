package com.coincelt.traderapi.domain.deserializer.poloniex;

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

        if(node.has(ExchangesJsonMessageError.POLONIEX.getName())){
            throw new IOException(node.get(ExchangesJsonMessageError.POLONIEX.getName()).textValue());
        }

        node.forEach(trade -> {
            TradeHistory entity = new TradeHistory();
            entity.setExchange(exchange);
            entity.setCurrencyPair(currencyPair);
            entity.setDate(ZonedDateTimeUtils.stringToZonedDateTimeUTC(trade.get("date").asText()));
            entity.setGlobalTradeID(trade.get("globalTradeID").asLong());
            entity.setTradeID(trade.get("tradeID").asLong());
            entity.setType(TradeType.forName(trade.get("type").asText()));
            entity.setRate(BigDecimalUtils.textToBigDecimal(trade.get("rate").textValue()));
            entity.setAmount(BigDecimalUtils.textToBigDecimal(trade.get("amount").textValue()));
            entity.setTotal(BigDecimalUtils.textToBigDecimal(trade.get("total").textValue()));
            result.add(entity);
        });

        return result;
    }
}
