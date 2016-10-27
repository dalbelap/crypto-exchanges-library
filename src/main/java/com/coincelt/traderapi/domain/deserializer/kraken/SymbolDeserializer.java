package com.coincelt.traderapi.domain.deserializer.kraken;

import com.coincelt.traderapi.domain.Symbol;
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

/**
 * Custom Jackson deserializer for transforming a JSON object from Symbol
 */
public class SymbolDeserializer extends JsonDeserializer<List<Symbol>> {

    @Override
    public List<Symbol> deserialize(JsonParser parser, DeserializationContext context) throws IOException {
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

        List<Symbol> result = new ArrayList<>();
        Exchange exchange = (Exchange)context.findInjectableValue(Exchange.class.getName(), null, null);
        node.get("result").forEach(json -> {
            Symbol symbol = new Symbol();
            symbol.setExchange(exchange);
            symbol.setPair(json.get("altname").asText());
            symbol.setPricePrecision(json.get("pair_decimals").asInt());
            result.add(symbol);
        });


        return result;
    }
}
