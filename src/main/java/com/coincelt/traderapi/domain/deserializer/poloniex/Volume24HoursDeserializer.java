package com.coincelt.traderapi.domain.deserializer.poloniex;

import com.coincelt.traderapi.domain.VolumeStats;
import com.coincelt.traderapi.domain.enumeration.Exchange;
import com.coincelt.traderapi.domain.enumeration.ExchangesJsonMessageError;
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
 * Custom Jackson deserializer for transforming a JSON object from Volume24Hours
 */
public class Volume24HoursDeserializer extends JsonDeserializer<List<VolumeStats>> {

    @Override
    public List<VolumeStats> deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);
        List<VolumeStats> result = new ArrayList<>();
        Exchange exchange = (Exchange)context.findInjectableValue(Exchange.class.getName(), null, null);

        if(node.has(ExchangesJsonMessageError.POLONIEX.getName())){
            throw new IOException(node.get(ExchangesJsonMessageError.POLONIEX.getName()).textValue());
        }

        Iterator<Map.Entry<String, JsonNode>> nodeIterator = node.fields();
        while(nodeIterator.hasNext()){
            Map.Entry<String, JsonNode> entry = nodeIterator.next();
            if(entry.getKey().contains("_")){
                JsonNode crypto = entry.getValue();
                VolumeStats entity = new VolumeStats();
                entity.setCurrencyPair(entry.getKey());
                entity.setExchange(exchange);
                Iterator<Map.Entry<String, JsonNode>> childIterator = crypto.fields();
                Map.Entry<String, JsonNode> mainCurrencyEntry = childIterator.next();
                Map.Entry<String, JsonNode> secondCurrencyEntry = childIterator.next();

                entity.setMainCoin(mainCurrencyEntry.getKey());
                entity.setSecondCoin(secondCurrencyEntry.getKey());
                entity.setMainCoinVolume(BigDecimalUtils.textToBigDecimal(mainCurrencyEntry.getValue().asText()));
                entity.setSecondCoinVolume(BigDecimalUtils.textToBigDecimal(secondCurrencyEntry.getValue().asText()));
                result.add(entity);
            }
        }

        return result;
    }
}
