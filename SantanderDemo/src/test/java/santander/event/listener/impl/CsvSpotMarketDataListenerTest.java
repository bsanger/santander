package santander.event.listener.impl;

import org.junit.Assert;
import org.junit.Test;
import santander.event.cache.impl.SpotPriceCache;
import santander.event.handler.SpotPriceMarginHandler;
import santander.model.impl.SpotPrice;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static santander.calc.SpotPriceCalculatorHelper.getLogTimestamp;

/**
 * Created by bensanger on 31/08/2022.
 */
public class CsvSpotMarketDataListenerTest {

    private final static double PERCENTAGE_MARGIN = 0.1d;
    private CsvSpotMarketDataListener csvSpotMarketDataListener;
    private SpotPriceMarginHandler spotPriceMarginHandler;
    private SpotPriceCache spotPriceCache;

    @org.junit.Before
    public void setUp() throws Exception {
        spotPriceCache = new SpotPriceCache();
        spotPriceMarginHandler = new SpotPriceMarginHandler(spotPriceCache, PERCENTAGE_MARGIN);
        csvSpotMarketDataListener = new CsvSpotMarketDataListener(spotPriceMarginHandler);
    }

    @Test
    public void testLatestSpotPriceFromPriceCache() throws Exception {
        csvSpotMarketDataListener.onEvent("106,EUR/USD,1.1000,1.2000,01-06-2020 12:01:01:001");
        csvSpotMarketDataListener.onEvent("107,EUR/JPY,119.60,119.90,01-06-2020 12:01:02:002");
        csvSpotMarketDataListener.onEvent("108,GBP/USD,1.2500,1.2560,01-06-2020 12:01:02:002");
        csvSpotMarketDataListener.onEvent("109,GBP/USD,1.2499,1.2561,01-06-2020 12:01:02:100");
        csvSpotMarketDataListener.onEvent("110,EUR/JPY,119.61,119.91,01-06-2020 12:01:02:110");
        csvSpotMarketDataListener.onEvent("111,EUR/USD,1.1500,1.2500,01-06-2020 12:01:02:115");

        Thread.sleep(100l); // better to use Mockito for waiting for async calls to complete

        Assert.assertEquals(new SpotPrice("111",
                "EUR/USD",
                BigDecimal.valueOf(1.1489d),
                BigDecimal.valueOf(1.2513d),
                getLogTimestamp("01-06-2020 12:01:02:115")
                ), spotPriceCache.getPrice("EUR/USD"));

        // EUR/JPY should really be 2DP
        Assert.assertEquals(new SpotPrice("110",
                "EUR/JPY",
                BigDecimal.valueOf(119.4904d),
                BigDecimal.valueOf(120.0299d),
                getLogTimestamp("01-06-2020 12:01:02:110")
        ), spotPriceCache.getPrice("EUR/JPY"));

        Assert.assertEquals(new SpotPrice("109",
                "GBP/USD",
                BigDecimal.valueOf(1.2487d),
                BigDecimal.valueOf(1.2574d),
                getLogTimestamp("01-06-2020 12:01:02:100")
        ), spotPriceCache.getPrice("GBP/USD"));
    }

}