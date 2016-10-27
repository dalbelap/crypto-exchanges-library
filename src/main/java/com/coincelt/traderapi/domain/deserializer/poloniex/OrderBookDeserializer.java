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
import java.util.List;

/**
 * Custom Jackson deserializer for transforming a JSON object from OrderBook
 */
public class OrderBookDeserializer extends JsonDeserializer<List<OrderBook>> {

    @Override
    public List<OrderBook> deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);
        List<OrderBook> result = new ArrayList<>();
        Exchange exchange = (Exchange)context.findInjectableValue(Exchange.class.getName(), null, null);
        String currencyPair = (String)context.findInjectableValue(String.class.getName(), null, null);

        if(node.has(ExchangesJsonMessageError.POLONIEX.getName())){
            throw new IOException(node.get(ExchangesJsonMessageError.POLONIEX.getName()).textValue());
        }
        
        JsonNode asks = node.get("asks");
        asks.forEach(ask -> {
            OrderBook orderBook = new OrderBook();
            orderBook.setExchange(exchange);
            orderBook.setCurrencyPair(currencyPair);
            orderBook.setOrderBookType(OrderBookType.ASK);
            orderBook.setRate(BigDecimalUtils.textToBigDecimal(ask.get(0).textValue()));
            orderBook.setAmount(BigDecimalUtils.doubleToBigDecimal(ask.get(1).doubleValue()));
            result.add(orderBook);
        });

        JsonNode bids = node.get("bids");
        bids.forEach(ask -> {
            OrderBook orderBook = new OrderBook();
            orderBook.setExchange(exchange);
            orderBook.setCurrencyPair(currencyPair);
            orderBook.setOrderBookType(OrderBookType.BID);
            orderBook.setRate(BigDecimalUtils.textToBigDecimal(ask.get(0).textValue()));
            orderBook.setAmount(BigDecimalUtils.doubleToBigDecimal(ask.get(1).doubleValue()));
            result.add(orderBook);
        });

        return result;
    }
}
