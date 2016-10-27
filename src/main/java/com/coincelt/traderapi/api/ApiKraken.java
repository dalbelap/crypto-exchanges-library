package com.coincelt.traderapi.api;

import com.coincelt.traderapi.domain.*;
import com.coincelt.traderapi.domain.deserializer.kraken.*;
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
import java.util.Map;

/**
 * Api for Kraken exchange
 */
public class ApiKraken extends GeneralApi{

    private final static String PUBLIC_URL = "https://api.kraken.com/0/public/";
    private static final Exchange API_EXCHANGE = Exchange.KRAKEN;

    private ObjectMapper objectMapperTicker;
    private ObjectMapper objectMapperCurrency;
    private ObjectMapper objectMapperSymbol;
    private ObjectMapper objectMapperOrderBook;
    private ObjectMapper objectMapperTradeHistory;

    public ApiKraken(OkHttpClient client) {
        super(client);
        createObjectMappers();
    }


    /**
     * Creates and configure an object Mapper with its deserializer types and injected values for Poloniex exchange
     */
    private void createObjectMappers() {
        InjectableValues exchangeInjectable = new InjectableValues.Std().addValue(Exchange.class, API_EXCHANGE);
        this.objectMapperTicker = new ObjectMapper();
        this.objectMapperCurrency = new ObjectMapper();
        this.objectMapperSymbol = new ObjectMapper();
        this.objectMapperOrderBook = new ObjectMapper();
        this.objectMapperTradeHistory = new ObjectMapper();

        objectMapperCurrency.setInjectableValues(exchangeInjectable);
        objectMapperSymbol.setInjectableValues(exchangeInjectable);
        objectMapperTicker.setInjectableValues(exchangeInjectable);

        SimpleModule module = new SimpleModule();
        module.addDeserializer(Ticker.class, new TickerDeserializer());
        objectMapperTicker.registerModule(module);

        module = new SimpleModule();
        module.addDeserializer(List.class, new CurrencyDeserializer());
        objectMapperCurrency.registerModule(module);

        module = new SimpleModule();
        module.addDeserializer(List.class, new SymbolDeserializer());
        objectMapperSymbol.registerModule(module);

        module = new SimpleModule();
        module.addDeserializer(List.class, new OrderBookDeserializer());
        objectMapperOrderBook.registerModule(module);

        module = new SimpleModule();
        module.addDeserializer(List.class, new TradeHistoryDeserializer());
        objectMapperTradeHistory.registerModule(module);
    }

    /**
     * Return a command line API
     * @param command String command API
     * @return String json
     * @throws IOException input output API exception
     */
    private String getJson(String command) throws IOException {
        StringBuilder urlRequest = new StringBuilder(PUBLIC_URL).append(command);

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
        StringBuilder urlRequest = new StringBuilder(PUBLIC_URL).append(command);
        params.forEach((k,v) -> urlRequest.append("&").append(k).append("=").append(v));

        Request request = new Request.Builder()
                .url(urlRequest.toString())
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    /**
     * Returns information about assets.
     * @return List Currency
     * @throws IOException input output exception from getJson
     */
    public List<Currency> getAssets() throws IOException {
        return objectMapperCurrency.readValue(getJson("Assets"), new TypeReference<List<Currency>>(){});
    }

    /**
     * Return asset pairs information.
     * @return List Symbol
     * @throws IOException
     */
    public List<Symbol> getAssetPairs() throws IOException {
        return objectMapperSymbol.readValue(getJson("AssetPairs"), new TypeReference<List<Symbol>>(){});
    }

    /**
     * Return asset pairs information.
     * @return Ticker
     * @throws IOException
     */
    public Ticker getTicker(String pair) throws IOException {
        return objectMapperTicker.readValue(getJson("Ticker?pair=" + pair), Ticker.class);
    }

    /**
     * Return order book
     * @param pair String
     * @param count Integer
     * @return List OrderBook
     * @throws IOException
     */
    public List<OrderBook> getOrderBook(String pair, Integer count) throws IOException {
        StringBuilder params = new StringBuilder("pair=" + pair);
        if(count != null){
            params.append("&count=" + count);
        }
        objectMapperOrderBook.setInjectableValues(new InjectableValues.Std().
                addValue(Exchange.class, API_EXCHANGE).
                addValue(String.class, pair));
        return objectMapperOrderBook.readValue(getJson("Depth?" + params), new TypeReference<List<OrderBook>>(){});
    }


    /**
     * Return order book
     * @param pair String
     * @param count Integer
     * @return List OrderBook
     * @throws IOException
     */
    public List<TradeHistory> getTradeHistory(String pair, Integer since) throws IOException {
        StringBuilder params = new StringBuilder("pair=" + pair);
        if(since != null){
            params.append("&since=" + since);
        }
        objectMapperTradeHistory.setInjectableValues(new InjectableValues.Std().
                addValue(Exchange.class, API_EXCHANGE).
                addValue(String.class, pair));
        return objectMapperTradeHistory.readValue(getJson("Trades?" + params), new TypeReference<List<TradeHistory>>(){});
    }


}
