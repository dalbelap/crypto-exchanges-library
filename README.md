# crypto-exchanges-library
---

Java library to query API from cryptocurrency exchanges.

## Requirements

- Java 8
- Maven 3.3

## Supported exchanges

- Bitfinex
- Kraken
- Poloniex

## Samples

```
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
```

License
-------
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

More info in "LICENSE" file

