package com.coincelt.traderapi.domain.deserializer.poloniex;

import com.coincelt.traderapi.domain.LoanOrderBook;
import com.coincelt.traderapi.domain.enumeration.Exchange;
import com.coincelt.traderapi.domain.enumeration.ExchangesJsonMessageError;
import com.coincelt.traderapi.domain.enumeration.LoanOrderBookType;
import com.coincelt.traderapi.domain.util.BigDecimalUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom Jackson deserializer for transforming a JSON object from LoanOrder
 */
public class LoanOrderBookDeserializer extends JsonDeserializer<List<LoanOrderBook>> {

    @Override
    public List<LoanOrderBook> deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);
        List<LoanOrderBook> result = new ArrayList<>();
        Exchange exchange = (Exchange)context.findInjectableValue(Exchange.class.getName(), null, null);
        String currency = (String) context.findInjectableValue(String.class.getName(), null, null);

        if(node.has(ExchangesJsonMessageError.POLONIEX.getName())){
            throw new IOException(node.get(ExchangesJsonMessageError.POLONIEX.getName()).textValue());
        }

        JsonNode offers = node.get("offers");
        offers.forEach(offer -> {
            LoanOrderBook loanOrderBook = new LoanOrderBook();
            loanOrderBook.setExchange(exchange);
            loanOrderBook.setCurrency(currency);
            loanOrderBook.setLoanOrderBookType(LoanOrderBookType.OFFERS);
            loanOrderBook.setRate(BigDecimalUtils.textToBigDecimal(offer.get("rate").textValue()));
            loanOrderBook.setAmount(BigDecimalUtils.textToBigDecimal(offer.get("amount").textValue()));
            loanOrderBook.setRangeMin(offer.get("rangeMin").intValue());
            loanOrderBook.setRangeMax(offer.get("rangeMax").intValue());
            result.add(loanOrderBook);
        });

        JsonNode demands = node.get("demands");
        demands.forEach(demand -> {
            LoanOrderBook loanOrderBook = new LoanOrderBook();
            loanOrderBook.setExchange(exchange);
            loanOrderBook.setCurrency(currency);
            loanOrderBook.setLoanOrderBookType(LoanOrderBookType.DEMANDS);
            loanOrderBook.setRate(BigDecimalUtils.textToBigDecimal(demand.get("rate").textValue()));
            loanOrderBook.setAmount(BigDecimalUtils.textToBigDecimal(demand.get("amount").textValue()));
            loanOrderBook.setRangeMin(demand.get("rangeMin").intValue());
            loanOrderBook.setRangeMax(demand.get("rangeMax").intValue());
            result.add(loanOrderBook);
        });

        return result;
    }
}
