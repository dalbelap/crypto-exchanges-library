package com.coincelt.traderapi.domain.deserializer.poloniex;

import com.coincelt.traderapi.domain.ChartData;
import com.coincelt.traderapi.domain.enumeration.Exchange;
import com.coincelt.traderapi.domain.enumeration.ExchangesJsonMessageError;
import com.coincelt.traderapi.domain.enumeration.PeriodTime;
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
 * Custom Jackson deserializer for transforming a JSON object from ChartData
 */
public class ChartDataDeserializer extends JsonDeserializer<List<ChartData>> {

    @Override
    public List<ChartData> deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);
        List<ChartData> result = new ArrayList<>();
        Exchange exchange = (Exchange)context.findInjectableValue(Exchange.class.getName(), null, null);
        String currencyPair = (String)context.findInjectableValue(String.class.getName(), null, null);
        PeriodTime period = (PeriodTime) context.findInjectableValue(PeriodTime.class.getName(), null, null);

        if(node.has(ExchangesJsonMessageError.POLONIEX.getName())){
            throw new IOException(node.get(ExchangesJsonMessageError.POLONIEX.getName()).textValue());
        }

        node.forEach(trade -> {
            ChartData entity = new ChartData();
            entity.setExchange(exchange);
            entity.setCurrencyPair(currencyPair);
            entity.setPeriod(period);
            entity.setDate(ZonedDateTimeUtils.timestampToZonedDateTime(trade.get("date").asLong()));
            entity.setHigh(BigDecimalUtils.doubleToBigDecimal(trade.get("high").doubleValue()));
            entity.setLow(BigDecimalUtils.doubleToBigDecimal(trade.get("low").doubleValue()));
            entity.setOpen(BigDecimalUtils.doubleToBigDecimal(trade.get("open").doubleValue()));
            entity.setClose(BigDecimalUtils.doubleToBigDecimal(trade.get("close").doubleValue()));
            entity.setVolume(BigDecimalUtils.doubleToBigDecimal(trade.get("volume").doubleValue()));
            entity.setQuoteVolume(BigDecimalUtils.doubleToBigDecimal(trade.get("quoteVolume").doubleValue()));
            entity.setWeightedAverage(BigDecimalUtils.doubleToBigDecimal(trade.get("weightedAverage").doubleValue()));
            result.add(entity);
        });

        return result;
    }
}
