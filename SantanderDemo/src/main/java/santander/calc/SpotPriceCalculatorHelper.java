package santander.calc;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import santander.model.impl.SpotPrice;

import java.math.BigDecimal;

/**
 * Created by bensanger on 01/09/2022.
 */
public class SpotPriceCalculatorHelper {

    protected static final DateTimeFormatter DATE_TIME_FORMATTER
            = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss:SSS").withZoneUTC();

    protected final static double ROUNDING_DP_SCALE = 10000d;

    public final static SpotPrice calculateSpotMargin(SpotPrice marketSpotPrice, double percentageMargin) {

        double marketSpotBid = marketSpotPrice.getBid().doubleValue();
        double marketSpotAsk = marketSpotPrice.getAsk().doubleValue();

        double marginSpotBid = simpleRounder(marketSpotBid - ((marketSpotBid / 100) * percentageMargin));
        double marginSpotAsk = simpleRounder(marketSpotAsk + ((marketSpotAsk / 100) * percentageMargin));

        return new SpotPrice(marketSpotPrice.getId(),
                marketSpotPrice.getInstrument(),
                BigDecimal.valueOf(marginSpotBid),
                BigDecimal.valueOf(marginSpotAsk),
                marketSpotPrice.getTimeStamp());
    }

    public static long getLogTimestamp(String line) {
        return DATE_TIME_FORMATTER.parseMillis(line.substring(0, 23));
    }

    protected static double simpleRounder(double value){
        return (double)Math.round(value * ROUNDING_DP_SCALE) / ROUNDING_DP_SCALE;
    }
}
