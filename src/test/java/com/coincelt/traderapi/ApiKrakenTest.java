package com.coincelt.traderapi;

import com.coincelt.traderapi.api.ApiKraken;

import java.io.IOException;

/**
 * Test API from Poloniex
 */
public class ApiKrakenTest {

    public static void main(String args[]) throws IOException {

        TraderResource traderResource = new TraderResource();

        ApiKraken api = traderResource.getApiKraken();

        api.getTradeHistory("ETHXBT", null).forEach(tradeHistory ->
        {
            System.out.println(tradeHistory);
        });

        api.getAssets().forEach(currency ->
                System.out.println(currency));

        api.getAssetPairs().forEach(pair ->
                System.out.println(pair));

        api.getTicker("DAOETH");

        api.getOrderBook("ETHXBT", 1).forEach(order ->
                System.out.println(order));

    }
}
