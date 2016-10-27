package com.coincelt.traderapi.domain.deserializer.kraken;

import com.coincelt.traderapi.domain.OrderBook;
import com.coincelt.traderapi.domain.enumeration.Exchange;
import com.coincelt.traderapi.domain.enumeration.ExchangesJsonMessageError;
import com.coincelt.traderapi.domain.enumeration.OrderBookType;
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
import java.util.Map;

/**
 * Custom Jackson deserializer for transforming a JSON object from OrderBook
 */
public class OrderBookDeserializer extends JsonDeserializer<List<OrderBook>> {

    @Override
    public List<OrderBook> deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);
        List<OrderBook> result = new ArrayList<>();
        Exchange exchange = (Exchange)context.findInjectableValue(Exchange.class.getName(), null, null);
        String pair = (String)context.findInjectableValue(String.class.getName(), null, null);

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

        if(node.get("result").elements().hasNext()){
            Iterator<Map.Entry<String, JsonNode>> nodeIterator = node.get("result").elements().next().fields();
            while(nodeIterator.hasNext()) {
                Map.Entry<String, JsonNode> entry = nodeIterator.next();
                JsonNode orderBooks = entry.getValue();
                OrderBookType orderBookType = entry.getKey().equals("asks") ? OrderBookType.ASK : OrderBookType.BID;

                orderBooks.forEach(orderBookNode -> {
                    OrderBook orderBook = new OrderBook();
                    orderBook.setExchange(exchange);
                    orderBook.setCurrencyPair(pair);
                    orderBook.setOrderBookType(orderBookType);

                    orderBook.setRate(BigDecimalUtils.textToBigDecimal(orderBookNode.get(0).textValue()));
                    orderBook.setAmount(BigDecimalUtils.textToBigDecimal(orderBookNode.get(1).textValue()));
                    double posixTimeStamp = Double.valueOf(orderBookNode.get(2).asInt());
                    orderBook.setDate(ZonedDateTimeUtils.timestampToZonedDateTime((long) posixTimeStamp));
                    result.add(orderBook);
                });

            }
        }

        return result;
    }
}
