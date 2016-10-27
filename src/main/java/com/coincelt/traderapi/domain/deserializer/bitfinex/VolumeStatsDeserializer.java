package com.coincelt.traderapi.domain.deserializer.bitfinex;

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
import java.util.List;

/**
 * Custom Jackson deserializer for transforming a JSON object from Volume24Hours
 */
public class VolumeStatsDeserializer extends JsonDeserializer<List<VolumeStats>> {

    @Override
    public List<VolumeStats> deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);
        if(node.has(ExchangesJsonMessageError.BITFINEX.getName())){
            throw new IOException(node.get(ExchangesJsonMessageError.BITFINEX.getName()).textValue());
        }

        List<VolumeStats> result = new ArrayList<>();
        Exchange exchange = (Exchange)context.findInjectableValue(Exchange.class.getName(), null, null);
        String pair = (String)context.findInjectableValue(String.class.getName(), null, null);
        node.forEach(json -> {
            VolumeStats volumeStats = new VolumeStats();
            volumeStats.setExchange(exchange);
            volumeStats.setCurrencyPair(pair);
            volumeStats.setSecondCoinVolume(BigDecimalUtils.textToBigDecimal(json.get("volume").asText()));
            volumeStats.setPeriod(json.get("period").asInt());
            result.add(volumeStats);
        });

        return result;
    }
}
