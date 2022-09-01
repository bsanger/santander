package santander.calc;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import santander.model.impl.SpotPrice;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static santander.calc.SpotPriceCalculatorHelper.getLogTimestamp;

/**
 * Created by bensanger on 01/09/2022.
 */
public class SpotPriceCalculatorHelperTest {


    @Mock
    SpotPrice mockMarketSpotPrice;


    @Test
    public void testCalculateSpotMargin() throws Exception {

        // "111,EUR/USD,1.1500,1.2500,01-06-2020 12:01:02:115"

        initMocks(this);
        Mockito.when(mockMarketSpotPrice.getId()).thenReturn("111");
        Mockito.when(mockMarketSpotPrice.getInstrument()).thenReturn("EUR/USD");
        Mockito.when(mockMarketSpotPrice.getBid()).thenReturn(BigDecimal.valueOf(1.1500d));
        Mockito.when(mockMarketSpotPrice.getAsk()).thenReturn(BigDecimal.valueOf(1.2500d));
        Mockito.when(mockMarketSpotPrice.getTimeStamp()).thenReturn(1591012862115l);

        SpotPrice marginSpotPrice = SpotPriceCalculatorHelper.calculateSpotMargin(mockMarketSpotPrice, 0.1d);

        Assert.assertEquals(new SpotPrice("111",
                "EUR/USD",
                BigDecimal.valueOf(1.1489d),
                BigDecimal.valueOf(1.2513d),
                getLogTimestamp("01-06-2020 12:01:02:115")
        ), marginSpotPrice);

    }

}