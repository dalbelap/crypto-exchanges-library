package com.coincelt.traderapi.domain.deserializer.bitfinex;

import com.coincelt.traderapi.domain.LoanOrderBook;
import com.coincelt.traderapi.domain.enumeration.Exchange;
import com.coincelt.traderapi.domain.enumeration.ExchangesJsonMessageError;
import com.coincelt.traderapi.domain.enumeration.LoanOrderBookType;
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
 * Custom Jackson deserializer for transforming a JSON object from LoanOrderBook
 */
public class LoanOrderBookDeserializer extends JsonDeserializer<List<LoanOrderBook>> {

    @Override
    public List<LoanOrderBook> deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);
        List<LoanOrderBook> result = new ArrayList<>();
        Exchange exchange = (Exchange)context.findInjectableValue(Exchange.class.getName(), null, null);
        String currency = (String)context.findInjectableValue(String.class.getName(), null, null);

        if(node.has(ExchangesJsonMessageError.BITFINEX.getName())){
            throw new IOException(node.get(ExchangesJsonMessageError.BITFINEX.getName()).textValue());
        }

        JsonNode bids = node.get("bids");
        bids.forEach(bid -> {
            LoanOrderBook loanOrderBook = new LoanOrderBook();
            loanOrderBook.setExchange(exchange);
            loanOrderBook.setCurrency(currency);
            loanOrderBook.setLoanOrderBookType(LoanOrderBookType.DEMANDS);
            loanOrderBook.setRate(BigDecimalUtils.textToBigDecimal(bid.get("rate").textValue()));
            loanOrderBook.setAmount(BigDecimalUtils.textToBigDecimal(bid.get("amount").textValue()));
            loanOrderBook.setPeriod(bid.get("period").intValue());
            double posixTimeStamp = Double.valueOf(bid.get("timestamp").asText());
            loanOrderBook.setDate(ZonedDateTimeUtils.timestampToZonedDateTime((long) posixTimeStamp));
            loanOrderBook.setFlashReturnRate(bid.get("frr").asText().equals("Yes") ? true : false);
            result.add(loanOrderBook);
        });


        JsonNode asks = node.get("asks");
        asks.forEach(ask -> {
            LoanOrderBook loanOrderBook = new LoanOrderBook();
            loanOrderBook.setExchange(exchange);
            loanOrderBook.setCurrency(currency);
            loanOrderBook.setLoanOrderBookType(LoanOrderBookType.OFFERS);
            loanOrderBook.setRate(BigDecimalUtils.textToBigDecimal(ask.get("rate").textValue()));
            loanOrderBook.setAmount(BigDecimalUtils.textToBigDecimal(ask.get("amount").textValue()));
            loanOrderBook.setPeriod(ask.get("period").intValue());
            double posixTimeStamp = Double.valueOf(ask.get("timestamp").asText());
            loanOrderBook.setDate(ZonedDateTimeUtils.timestampToZonedDateTime((long) posixTimeStamp));
            loanOrderBook.setFlashReturnRate(ask.get("frr").asText().equals("Yes") ? true : false);
            result.add(loanOrderBook);
        });

        return result;
    }
}
