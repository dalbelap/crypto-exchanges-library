package com.coincelt.traderapi.domain.deserializer.kraken;

import com.coincelt.traderapi.domain.Currency;
import com.coincelt.traderapi.domain.enumeration.Exchange;
import com.coincelt.traderapi.domain.enumeration.ExchangesJsonMessageError;
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
 * Custom Jackson deserializer for transforming a JSON object from Currency
 */
public class CurrencyDeserializer extends JsonDeserializer<List<Currency>> {

    @Override
    public List<Currency> deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);
        List<Currency> result = new ArrayList<>();
        Exchange exchange = (Exchange)context.findInjectableValue(Exchange.class.getName(), null, null);

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

        Iterator<Map.Entry<String, JsonNode>> nodeIterator = node.get("result").fields();
        while(nodeIterator.hasNext()){
            Map.Entry<String, JsonNode> entry = nodeIterator.next();
            Currency currency = new Currency();
            currency.setExchange(exchange);
            currency.setName(entry.getKey());
            currency.setCurrency(entry.getValue().get("altname").textValue());
            currency.setDecimals(entry.getValue().get("decimals").intValue());
            currency.setDisplayDecimals(entry.getValue().get("display_decimals").intValue());
            result.add(currency);
        }
        return result;
    }
}
