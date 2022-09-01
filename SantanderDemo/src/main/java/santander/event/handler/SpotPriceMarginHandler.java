package santander.event.handler;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import santander.calc.SpotPriceCalculatorHelper;
import santander.event.cache.PriceCache;
import santander.event.listener.EventListener;
import santander.model.impl.SpotPrice;


/**
 * Created by bensanger on 31/08/2022.
 */
public class SpotPriceMarginHandler implements EventListener <SpotPrice> {

    protected static final DateTimeFormatter DATE_TIME_FORMATTER
            = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss:SSS").withZoneUTC();


    private final PriceCache priceCache;
    private final double percentageMargin;

    public SpotPriceMarginHandler(PriceCache priceCache, double percentageMargin) {
        this.priceCache = priceCache;
        this.percentageMargin = percentageMargin;
    }

    @Override
    public void onEvent(SpotPrice marketSpotPrice) {
        // apply margin and then update priceCache
        SpotPrice marginSpotPrice = SpotPriceCalculatorHelper.calculateSpotMargin(marketSpotPrice, percentageMargin);

        System.out.println(prettySpotMarketDataMessage(marketSpotPrice, marginSpotPrice));
        priceCache.addPrice(marginSpotPrice);
    }

    private String prettySpotMarketDataMessage(SpotPrice marketSpotPrice, SpotPrice marginSpotPrice){
        return new StringBuilder()
                .append("Thread[")
                .append(Thread.currentThread().getName())
                .append("], SpotPrice[")
                .append("Id[")
                .append( marketSpotPrice.getId())
                .append("], Instrument[")
                .append( marketSpotPrice.getInstrument())
                .append("], Market Bid[")
                .append(marketSpotPrice.getBid())
                .append("], Market Ask[")
                .append(marketSpotPrice.getAsk())
                .append("], Margin Bid[")
                .append(marginSpotPrice.getBid())
                .append("], Margin Ask[")
                .append(marginSpotPrice.getAsk())
                .append("], Timestamp[")
                .append(DATE_TIME_FORMATTER.print(marketSpotPrice.getTimeStamp()))
                .append("]")
                .toString();
    }


}
