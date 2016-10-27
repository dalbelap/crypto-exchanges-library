package com.coincelt.traderapi.domain.deserializer.bitfinex;

import com.coincelt.traderapi.domain.Symbol;
import com.coincelt.traderapi.domain.enumeration.Exchange;
import com.coincelt.traderapi.domain.enumeration.ExchangesJsonMessageError;
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
 * Custom Jackson deserializer for transforming a JSON object from Symbol
 */
public class SymbolDeserializer extends JsonDeserializer<List<Symbol>> {

    @Override
    public List<Symbol> deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);

        if(node.has(ExchangesJsonMessageError.BITFINEX.getName())){
            throw new IOException(node.get(ExchangesJsonMessageError.BITFINEX.getName()).textValue());
        }

        List<Symbol> result = new ArrayList<>();
        Exchange exchange = (Exchange)context.findInjectableValue(Exchange.class.getName(), null, null);
        node.forEach(json -> {
            Symbol symbol = new Symbol();
            symbol.setExchange(exchange);
            symbol.setPair(json.get("pair").asText());
            symbol.setPricePrecision(json.get("price_precision").asInt());
            symbol.setInitialMargin(BigDecimalUtils.textToBigDecimal(json.get("initial_margin").asText()));
            symbol.setMinimunMargin(BigDecimalUtils.textToBigDecimal(json.get("minimum_margin").asText()));
            symbol.setMaximunOrderSize(BigDecimalUtils.textToBigDecimal(json.get("maximum_order_size").asText()));
            String timestampAsString = json.get("expiration").asText();
            if(!timestampAsString.equals("NA")) {
                symbol.setExpiration(ZonedDateTimeUtils.stringToZonedDateTimeUTC(timestampAsString));
            }
            result.add(symbol);
        });


        return result;
    }
}
