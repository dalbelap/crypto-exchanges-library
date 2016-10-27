package com.coincelt.traderapi.api;

import com.coincelt.traderapi.domain.*;
import com.coincelt.traderapi.domain.deserializer.bitfinex.*;
import com.coincelt.traderapi.domain.enumeration.Exchange;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

/**
 * Poloniex Public API
 */
public class ApiBitfinex extends GeneralApi {

    private final static String PUBLIC_URL = "https://api.bitfinex.com/v1";
    private static final Exchange API_EXCHANGE = Exchange.BITFINEX;

    private ObjectMapper objectMapperTicker;
    private ObjectMapper objectMapperStats;
    private ObjectMapper objectMapperOrderBook;
    private ObjectMapper objectMapperTradeHistory;
    private ObjectMapper objectMapperSymbol;
    private ObjectMapper objectMapperLoanOrderBook;
    private ObjectMapper objectMapperLoanHistory;

    public ApiBitfinex(OkHttpClient client){
        super(client);
        createObjectMappers();
    }

    /**
     * Creates and configure an object Mapper with its deserializer types and injected values for Poloniex exchange
     */
    private void createObjectMappers() {
        InjectableValues exchangeInjectable = new InjectableValues.Std().addValue(Exchange.class, API_EXCHANGE);
        this.objectMapperTicker = new ObjectMapper();
        this.objectMapperStats = new ObjectMapper();
        this.objectMapperOrderBook = new ObjectMapper();
        this.objectMapperTradeHistory = new ObjectMapper();
        this.objectMapperSymbol = new ObjectMapper();
        this.objectMapperLoanOrderBook = new ObjectMapper();
        this.objectMapperLoanHistory = new ObjectMapper();

        this.objectMapperSymbol.setInjectableValues(exchangeInjectable);

        SimpleModule module = new SimpleModule();
        module.addDeserializer(Ticker.class, new TickerDeserializer());
        objectMapperTicker.registerModule(module);

        module = new SimpleModule();
        module.addDeserializer(List.class, new VolumeStatsDeserializer());
        objectMapperStats.registerModule(module);

        module = new SimpleModule();
        module.addDeserializer(List.class, new OrderBookDeserializer());
        objectMapperOrderBook.registerModule(module);

        module = new SimpleModule();
        module.addDeserializer(List.class, new TradeHistoryDeserializer());
        objectMapperTradeHistory.registerModule(module);

        module = new SimpleModule();
        module.addDeserializer(List.class, new SymbolDeserializer());
        objectMapperSymbol.registerModule(module);

        module = new SimpleModule();
        module.addDeserializer(List.class, new LoanOrderBookDeserializer());
        objectMapperLoanOrderBook.registerModule(module);

        module = new SimpleModule();
        module.addDeserializer(List.class, new LoanHistoryDeserializer());
        objectMapperLoanHistory.registerModule(module);
    }

    /**
     * Return a command line API
     * @param command String command API
     * @return String json
     * @throws IOException input output API exception
     */
    private String getJson(String command) throws IOException {
        StringBuilder urlRequest = new StringBuilder(PUBLIC_URL).append("/").append(command);

        Request request = new Request.Builder()
                .url(urlRequest.toString())
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    /**
     * Gives innermost bid and asks and information on the most recent trade, as well as high, low and volume of the last 24 hours.
     *
     * @param pair String pair symbol
     * @return Ticker
     * @throws IOException input output API exception
     */
    public Ticker getTicker(String pair) throws IOException {
        objectMapperTicker.setInjectableValues(new InjectableValues.Std().
                addValue(Exchange.class, API_EXCHANGE).
                addValue(String.class, pair));
        return objectMapperTicker.readValue(getJson("pubticker/"+pair), Ticker.class);
    }

    /**
     * Various statistics about the requested pair.
     * @param pair String pair symbol
     * @return List VolumeStats
     * @throws IOException input output API exception
     */
    public List<VolumeStats> getVolumeStats(String pair) throws IOException {
        objectMapperStats.setInjectableValues(new InjectableValues.Std().
                addValue(Exchange.class, API_EXCHANGE).
                addValue(String.class, pair));
        return objectMapperStats.readValue(getJson("stats/"+pair), new TypeReference<List<VolumeStats>>(){});
    }

    /**
     * Get the full order book.
     * @param pair String
     * @return List OrderBook
     * @throws IOException input output API exception
     */
    public List<OrderBook> getOrderBook(String pair) throws IOException {
        objectMapperOrderBook.setInjectableValues(new InjectableValues.Std().
                addValue(Exchange.class, API_EXCHANGE).
                addValue(String.class, pair));
        return objectMapperOrderBook.readValue(getJson("book/"+pair), new TypeReference<List<OrderBook>>(){});
    }

    public List<TradeHistory> getTradeHistory(String pair) throws IOException {
        objectMapperTradeHistory.setInjectableValues(new InjectableValues.Std().
                addValue(Exchange.class, API_EXCHANGE).
                addValue(String.class, pair));
        return objectMapperTradeHistory.readValue(getJson("trades/"+pair), new TypeReference<List<TradeHistory>>(){});
    }

    /**
     * Get a list of valid symbol IDs and the pair details.
     * @return List Symbol
     * @throws IOException input output API exception
     */
    public List<Symbol> getSymbols() throws IOException{
        return objectMapperSymbol.readValue(getJson("symbols_details"), new TypeReference<List<Symbol>>(){});
    }

    /**
     * Get the full margin funding book
     * @param currency String
     * @return List LoanOrderBook
     * @throws IOException input output API exception
     */
    public List<LoanOrderBook> getLoanOrderBook(String currency) throws IOException{
        objectMapperLoanOrderBook.setInjectableValues(new InjectableValues.Std().
                addValue(Exchange.class, API_EXCHANGE).
                addValue(String.class, currency));
        return objectMapperLoanOrderBook.readValue(getJson("lendbook/" + currency), new TypeReference<List<LoanOrderBook>>(){});
    }

    /**
     * Get a list of the most recent funding data for the given currency: total amount provided and Flash Return Rate (in % by 365 days) over time.
     * @param currency String
     * @return List LoanHistory
     * @throws IOException input output API exception
     */
    public List<LoanHistory> getLoanHistory(String currency) throws IOException{
        objectMapperLoanHistory.setInjectableValues(new InjectableValues.Std().
                addValue(Exchange.class, API_EXCHANGE).
                addValue(String.class, currency));
        return objectMapperLoanHistory.readValue(getJson("lends/" + currency), new TypeReference<List<LoanHistory>>(){});
    }
}
