package santander.event.cache.impl;

import santander.event.cache.PriceCache;
import santander.model.impl.SpotPrice;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by bensanger on 31/08/2022.
 *
 *  This cache could be used by a RESTful webserver to get the latest price
 *
 */
public class SpotPriceCache implements PriceCache<SpotPrice> {
    private final static SpotPrice NO_MARKET_DATA = new SpotPrice("0", "NO_MARKET_DATA", BigDecimal.ZERO,
            BigDecimal.ZERO, 0l);
    private final ConcurrentMap<String, SpotPrice> cache = new ConcurrentHashMap<>();

    @Override
    public void addPrice(SpotPrice price) {
        // always replace with latest price
        cache.put(price.getInstrument(), price);
    }

    @Override
    public SpotPrice getPrice(String key) {
        SpotPrice ret = cache.get(key);
        if (ret == null) {
            // don't return null
            ret = NO_MARKET_DATA;
        }
        return ret;
    }
}
