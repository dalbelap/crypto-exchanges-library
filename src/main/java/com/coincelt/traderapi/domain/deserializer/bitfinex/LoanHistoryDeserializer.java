package com.coincelt.traderapi.domain.deserializer.bitfinex;

import com.coincelt.traderapi.domain.LoanHistory;
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
 * Custom Jackson deserializer for transforming a JSON object from LoanHistory
 */
public class LoanHistoryDeserializer extends JsonDeserializer<List<LoanHistory>> {

    @Override
    public List<LoanHistory> deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);
        List<LoanHistory> result = new ArrayList<>();
        Exchange exchange = (Exchange)context.findInjectableValue(Exchange.class.getName(), null, null);
        String currency = (String)context.findInjectableValue(String.class.getName(), null, null);

        if(node.has(ExchangesJsonMessageError.BITFINEX.getName())){
            throw new IOException(node.get(ExchangesJsonMessageError.BITFINEX.getName()).textValue());
        }
        
        node.forEach(json -> {
            LoanHistory loanHistory = new LoanHistory();
            loanHistory.setExchange(exchange);
            loanHistory.setCurrency(currency);
            loanHistory.setRate(BigDecimalUtils.textToBigDecimal(json.get("rate").textValue()));
            loanHistory.setAmountLent(BigDecimalUtils.textToBigDecimal(json.get("amount_lent").textValue()));
            loanHistory.setAmountUsed(BigDecimalUtils.textToBigDecimal(json.get("amount_used").textValue()));
            loanHistory.setDate(ZonedDateTimeUtils.timestampToZonedDateTime(json.get("timestamp").asLong()));
            result.add(loanHistory);
        });

        return result;
    }
}
