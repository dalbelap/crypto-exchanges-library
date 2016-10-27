package com.coincelt.traderapi.api;

import com.coincelt.traderapi.domain.*;
import com.coincelt.traderapi.domain.deserializer.poloniex.*;
import com.coincelt.traderapi.domain.enumeration.Exchange;
import com.coincelt.traderapi.domain.enumeration.PeriodTime;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Poloniex Public API
 */
public class APIPoloniex extends GeneralApi {

    private static final String PUBLIC_URL = "https://poloniex.com/public";
    private static final Exchange API_EXCHANGE = Exchange.POLONIEX;

    private ObjectMapper objectMapperTicker;
    private ObjectMapper objectMapperVolume;
    private ObjectMapper objectMapperOrderBook;
    private ObjectMapper objectMapperAllOrderBook;
    private ObjectMapper objectMapperTradeHistory;
    private ObjectMapper objectMapperChartData;
    private ObjectMapper objectMapperCurrency;
    private ObjectMapper objectMapperLoanOrder;

    public APIPoloniex(OkHttpClient client){
        super(client);
        createObjectMappers();
    }

    /**
     * Creates and configure an object Mapper with its deserializer types and injected values for Poloniex exchange
     * @return
     */
    private void createObjectMappers() {
        InjectableValues exchangeInjectable = new InjectableValues.Std().addValue(Exchange.class, API_EXCHANGE);
        this.objectMapperTicker = new ObjectMapper();
        this.objectMapperVolume = new ObjectMapper();
        this.objectMapperOrderBook = new ObjectMapper();
        this.objectMapperAllOrderBook = new ObjectMapper();
        this.objectMapperTradeHistory = new ObjectMapper();
        this.objectMapperChartData = new ObjectMapper();
        this.objectMapperCurrency = new ObjectMapper();
        this.objectMapperLoanOrder = new ObjectMapper();
        objectMapperTicker.setInjectableValues(exchangeInjectable);
        objectMapperVolume.setInjectableValues(exchangeInjectable);
        objectMapperCurrency.setInjectableValues(exchangeInjectable);
        objectMapperAllOrderBook.setInjectableValues(exchangeInjectable);

        SimpleModule module = new SimpleModule();
        module.addDeserializer(List.class, new TickerDeserializer());
        objectMapperTicker.registerModule(module);

        module = new SimpleModule();
        module.addDeserializer(List.class, new Volume24HoursDeserializer());
        objectMapperVolume.registerModule(module);

        module = new SimpleModule();
        module.addDeserializer(List.class, new OrderBookDeserializer());
        objectMapperOrderBook.registerModule(module);

        module = new SimpleModule();
        module.addDeserializer(List.class, new AllOrderBookDeserializer());
        objectMapperAllOrderBook.registerModule(module);

        module = new SimpleModule();
        module.addDeserializer(List.class, new TradeHistoryDeserializer());
        objectMapperTradeHistory.registerModule(module);

        module = new SimpleModule();
        module.addDeserializer(List.class, new ChartDataDeserializer());
        objectMapperChartData.registerModule(module);

        module = new SimpleModule();
        module.addDeserializer(List.class, new CurrencyDeserializer());
        objectMapperCurrency.registerModule(module);

        module = new SimpleModule();
        module.addDeserializer(List.class, new LoanOrderBookDeserializer());
        objectMapperLoanOrder.registerModule(module);
    }

    /**
     * Return a command line API
     * @param command String command API
     * @return String json
     * @throws IOException
     */
    private String getJson(String command) throws IOException {
        StringBuilder urlRequest = new StringBuilder(PUBLIC_URL).append("?command=").append(command);

        Request request = new Request.Builder()
                .url(urlRequest.toString())
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    /**
     * Returns a command line API with optional parameters
     * @param command String command API
     * @param params Map String, String optional parameters
     * @return String json
     * @throws IOException
     */
    private String getJsonWithParameters(String command, Map<String, String> params) throws IOException {
        StringBuilder urlRequest = new StringBuilder(PUBLIC_URL).append("?command=").append(command);
        params.forEach((k,v) -> urlRequest.append("&").append(k).append("=").append(v));

        Request request = new Request.Builder()
                .url(urlRequest.toString())
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    /**
     * Returns the ticker for all currencyPairs.
     * @return List Ticker
     * @throws IOException
     */
    public List<Ticker> getTicker() throws IOException {
        return objectMapperTicker.readValue(getJson("returnTicker"), new TypeReference<List<Ticker>>(){});
    }

    /**
     * Returns the 24-hour volume for all currencyPairs, plus totals for primary currencies.
     * @return List Volume24Hours
     * @throws IOException input output exception
     */
    public List<VolumeStats> get24Volume() throws IOException {
        return objectMapperVolume.readValue(getJson("return24hVolume"), new TypeReference<List<VolumeStats>>(){});
    }

    /**
     * Returns the order book for a given currencyPair, as well as a sequence number for use with the Push API and an indicator specifying whether the currencyPair is frozen. You may set currencyPair to "all" to get the order books of all currencyPairs.
     * @param currencyPair String
     * @param depth Integer
     * @return List OrderBook
     * @throws IOException input output exception
     */
    public List<OrderBook> getOrderBook(String currencyPair, Integer depth) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("currencyPair", currencyPair);
        params.put("depth", Integer.toString(depth));
        objectMapperOrderBook.setInjectableValues(new InjectableValues.Std().
                addValue(Exchange.class, API_EXCHANGE).
                addValue(String.class, currencyPair));
        return objectMapperOrderBook.readValue(getJsonWithParameters("returnOrderBook", params),
                new TypeReference<List<OrderBook>>(){});
    }

    /**
     * Returns all the order book as well as a sequence number for use with the Push API and an indicator specifying whether the currencyPair is frozen. You may set currencyPair to "all" to get the order books of all currencyPairs.
     * @param depth Integer
     * @return List OrderBook
     * @throws IOException input output exception
     */
    public List<OrderBook> getAllOrderBook(Integer depth) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("currencyPair", "all");
        params.put("depth", Integer.toString(depth));
        return objectMapperAllOrderBook.readValue(getJsonWithParameters("returnOrderBook", params),
                new TypeReference<List<OrderBook>>(){});
    }

    /**
     * Returns the past 200 trades for a given currencyPair,
     * or all of the trades between a range specified in UNIX timestamps by the "start" and "end" GET parameters
     * @param currencyPair String
     * @param start Integer UNX timestamp start. Nullable
     * @param end Integer UNX timestamp end. Nullable
     * @return List TradeHistory
     * @throws IOException input output exception
     */
    public List<TradeHistory> getTradeHistory(String currencyPair, Long start, Long end) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("currencyPair", currencyPair);
        Optional.ofNullable(start).ifPresent(startLambda -> params.put("start", Long.toString(startLambda)));
        Optional.ofNullable(end).ifPresent(endLambda -> params.put("end", Long.toString(endLambda)));
        objectMapperTradeHistory.setInjectableValues(new InjectableValues.Std().
                addValue(Exchange.class, API_EXCHANGE).
                addValue(String.class, currencyPair));
        return objectMapperTradeHistory.readValue(getJsonWithParameters("returnTradeHistory", params),
                new TypeReference<List<TradeHistory>>(){});
    }

    /**
     * Returns candlestick chart data. Required GET parameters are "currencyPair", "period" (candlestick period in seconds),
     * "start", and "end". "Start" and "end" are given in UNIX timestamp format and used to specify the date range
     * for the data returned.
     * @param currencyPair String
     * @param start Long UNIX timestamp in seconds start
     * @param end Long UNIX timestamp in seconds end
     * @param period PeriodTime
     * @return List ChartData
     * @throws IOException input output exception
     */
    public List<ChartData> getChartData(String currencyPair, Long start, Long end, PeriodTime period) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("currencyPair", currencyPair);
        params.put("start", Long.toString(start));
        params.put("end", Long.toString(end));
        params.put("period", Integer.toString(period.getSeconds()));
        objectMapperChartData.setInjectableValues(new InjectableValues.Std().
                addValue(Exchange.class, API_EXCHANGE).
                addValue(String.class, currencyPair).
                addValue(PeriodTime.class, period));
        return objectMapperChartData.readValue(getJsonWithParameters("returnChartData", params),
                new TypeReference<List<TradeHistory>>(){});
    }

    /**
     * Returns information about currencies.
     * @return List Currency
     * @throws IOException input output exception from getJson
     */
    public List<Currency> getCurrencies() throws IOException {
        return objectMapperCurrency.readValue(getJson("returnCurrencies"), new TypeReference<List<Currency>>(){});
    }

    /**
     * Returns the list of loan offers and demands for a given currency, specified by the "currency" GET parameter.
     * @param currency String
     * @return list of loan order book with offers(ask lends) and demands(bid lends)
     * @throws IOException input output exception from getJsonWithParameters
     */
    public List<LoanOrderBook> getLoanOrderBook(String currency) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("currency", currency);
        objectMapperLoanOrder.setInjectableValues(new InjectableValues.Std().
                addValue(Exchange.class, API_EXCHANGE).
                addValue(String.class, currency));
        return objectMapperLoanOrder.readValue(getJsonWithParameters("returnLoanOrders", params),
                new TypeReference<List<LoanOrderBook>>(){});
    }

}
