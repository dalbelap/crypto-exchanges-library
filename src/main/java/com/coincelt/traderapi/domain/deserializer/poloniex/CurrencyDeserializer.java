package com.coincelt.traderapi.domain.deserializer.poloniex;

import com.coincelt.traderapi.domain.Currency;
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
 * Custom Jackson deserializer for transforming a JSON object from Currency
 */
public class CurrencyDeserializer extends JsonDeserializer<List<Currency>> {

    @Override
    public List<Currency> deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);
        List<Currency> result = new ArrayList<>();
        Exchange exchange = (Exchange)context.findInjectableValue(Exchange.class.getName(), null, null);

        if(node.has(ExchangesJsonMessageError.POLONIEX.getName())){
            throw new IOException(node.get(ExchangesJsonMessageError.POLONIEX.getName()).textValue());
        }

        Iterator<Map.Entry<String, JsonNode>> nodeIterator = node.fields();
        while(nodeIterator.hasNext()){
            Map.Entry<String, JsonNode> entry = nodeIterator.next();
            Currency currency = new Currency();
            currency.setExchange(exchange);
            currency.setCurrency(entry.getKey());
            currency.setName(entry.getValue().get("name").textValue());
            currency.setTxFee(BigDecimalUtils.textToBigDecimal(entry.getValue().get("txFee").textValue()));
            currency.setMinConf(entry.getValue().get("minConf").intValue());
            currency.setDisabled(entry.getValue().get("disabled").intValue()==0 ? false : true);
            currency.setDelisted(entry.getValue().get("delisted").intValue()==0 ? false : true);
            currency.setFrozen(entry.getValue().get("frozen").intValue()==0 ? false : true);
            result.add(currency);
        }
        return result;
    }
}
