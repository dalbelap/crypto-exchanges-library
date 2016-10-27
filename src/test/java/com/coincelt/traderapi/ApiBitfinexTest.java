package com.coincelt.traderapi;

import com.coincelt.traderapi.api.ApiBitfinex;

import java.io.IOException;

/**
 * Test API from Poloniex
 */
public class ApiBitfinexTest {

    public static void main(String args[]) throws IOException {

        TraderResource traderResource = new TraderResource();
        ApiBitfinex api = traderResource.getApiBitfinex();

        System.out.println(api.getTicker("ethbtc"));
        api.getVolumeStats("ethbtc").forEach(stat ->
                System.out.println(stat));

        api.getOrderBook("ethbtc").forEach(book ->
                System.out.println(book));

        api.getTradeHistory("ethbtc").forEach(trade ->
                System.out.println(trade));

        api.getSymbols().forEach(symbol ->
                System.out.println(symbol));

        api.getLoanOrderBook("eth").forEach(loanOrderBook->
                System.out.println(loanOrderBook));

        api.getLoanHistory("eth").forEach(loanHistory ->
                System.out.println(loanHistory));
    }
}
