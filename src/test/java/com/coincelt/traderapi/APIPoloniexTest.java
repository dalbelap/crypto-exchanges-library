package com.coincelt.traderapi;

import com.coincelt.traderapi.api.APIPoloniex;
import com.coincelt.traderapi.domain.*;
import com.coincelt.traderapi.domain.enumeration.PeriodTime;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * Test API from Poloniex
 */
public class APIPoloniexTest {

    public static void main(String args[]) throws IOException {

        TraderResource traderResource = new TraderResource();
        APIPoloniex api = traderResource.getAPIPoloniex();

        List<VolumeStats> volumeStatsList = api.get24Volume();
        volumeStatsList.forEach(volume24Hours ->
                System.out.println(volume24Hours));

        List<Currency> currencies = api.getCurrencies();
        currencies.forEach(currency ->
                System.out.println(currency));

        List<LoanOrderBook> loanOrderBookList = api.getLoanOrderBook("BTC");
        loanOrderBookList.forEach(loan ->
                System.out.println(loan));

        List<Ticker> tickerList = api.getTicker();
        tickerList.forEach(ticker ->
                System.out.println(ticker));


        List<OrderBook> orderBooks = api.getAllOrderBook(10);
        orderBooks.forEach(orderBook ->
                System.out.println(orderBook));

        orderBooks = api.getOrderBook("BTC_ETH", 10);
        orderBooks.forEach(orderBook ->
                System.out.println(orderBook));

        Instant end = Instant.now();
        LocalDateTime time = LocalDateTime.ofInstant(end, ZoneOffset.ofHours(0));
        time = time.minusMinutes(1);
        Instant start = time.atZone(ZoneOffset.ofHours(0)).toInstant();
        List<TradeHistory> tradeHistories = api.getTradeHistory("BTC_ETH", start.getEpochSecond(), end.getEpochSecond());
        tradeHistories.forEach(tradeHistory ->
                System.out.println(tradeHistory));

        List<ChartData> chartDatas = api.getChartData("BTC_ETH", start.getEpochSecond(), end.getEpochSecond(), PeriodTime.FIVE_MINUTES);
        chartDatas.forEach(chartData ->
                System.out.println(chartData));
    }

}
