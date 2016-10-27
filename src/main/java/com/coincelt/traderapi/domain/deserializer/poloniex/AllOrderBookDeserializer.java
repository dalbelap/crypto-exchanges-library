package com.coincelt.traderapi.domain.deserializer.poloniex;

import com.coincelt.traderapi.domain.OrderBook;
import com.coincelt.traderapi.domain.enumeration.Exchange;
import com.coincelt.traderapi.domain.enumeration.ExchangesJsonMessageError;
import com.coincelt.traderapi.domain.enumeration.OrderBookType;
import com.coincelt.traderapi.domain.util.BigDecimalUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Custom Jackson deserializer for transforming a JSON object from OrderBook
 */
public class AllOrderBookDeserializer extends JsonDeserializer<List<OrderBook>> {

    @Override
    public List<OrderBook> deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);
        List<OrderBook> result = new ArrayList<>();
        Exchange exchange = (Exchange)context.findInjectableValue(Exchange.class.getName(), null, null);

        if(node.has(ExchangesJsonMessageError.POLONIEX.getName())){
            throw new IOException(node.get(ExchangesJsonMessageError.POLONIEX.getName()).textValue());
        }

        Iterator<Map.Entry<String, JsonNode>> nodeIterator = node.fields();
        while(nodeIterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = nodeIterator.next();
            JsonNode crypto = entry.getValue();
            String currencyPair = entry.getKey();
            JsonNode asks = crypto.get("asks");
            asks.forEach(ask -> {
                OrderBook orderBook = new OrderBook();
                orderBook.setExchange(exchange);
                orderBook.setCurrencyPair(currencyPair);
                orderBook.setOrderBookType(OrderBookType.ASK);
                orderBook.setRate(BigDecimalUtils.textToBigDecimal(ask.get(0).textValue()));
                orderBook.setAmount(BigDecimalUtils.doubleToBigDecimal(ask.get(1).doubleValue()));
                result.add(orderBook);
            });

            JsonNode bids = crypto.get("bids");
            bids.forEach(ask -> {
                OrderBook orderBook = new OrderBook();
                orderBook.setExchange(exchange);
                orderBook.setCurrencyPair(currencyPair);
                orderBook.setOrderBookType(OrderBookType.BID);
                orderBook.setRate(BigDecimalUtils.textToBigDecimal(ask.get(0).textValue()));
                orderBook.setAmount(BigDecimalUtils.doubleToBigDecimal(ask.get(1).doubleValue()));
                result.add(orderBook);
            });

        }

        return result;
    }
}
