package santander.event.listener.impl;

import com.opencsv.CSVReader;
import santander.event.buffer.IEventBuffer;
import santander.event.buffer.impl.EventBuffer;
import santander.event.listener.EventListener;
import santander.model.impl.SpotPrice;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static santander.calc.SpotPriceCalculatorHelper.getLogTimestamp;


/**
 * Created by bensanger on 31/08/2022.
 */
public class CsvSpotMarketDataListener implements EventListener<String> {

    // EventBuffer Map keyed by instrument - concurrent hashmap so it could handle multiple message streams
    private final ConcurrentMap<String, IEventBuffer<SpotPrice>> buffers = new ConcurrentHashMap<>();

    private final EventListener<SpotPrice> spotPriceHandler;

    public CsvSpotMarketDataListener(EventListener<SpotPrice> spotPriceHandler) {
        this.spotPriceHandler = spotPriceHandler;
    }


    @Override
    public void onEvent(String message) {

        // TODO - investigate more efficient cvsParsers like uniVocity CSV parser
        try (CSVReader cvsReader = new CSVReader(new StringReader(message))) {
            for (String[] csvMarketData : cvsReader.readAll()) {
                String id = csvMarketData[0];
                String instrument = csvMarketData[1];
                double bid = Double.parseDouble(csvMarketData[2]);
                double ask = Double.parseDouble(csvMarketData[3]);
                String timeStampStr = csvMarketData[4];
                long timeStamp = getLogTimestamp(timeStampStr);
                SpotPrice marketSpotPrice = new SpotPrice(id,
                        instrument, BigDecimal.valueOf(bid), BigDecimal.valueOf(ask), timeStamp);
                // insert at tail of queue if not full
                // keyed by instrument;
                getBuffer(instrument).offer(marketSpotPrice);

            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }


    private IEventBuffer getBuffer(String key) {
        IEventBuffer buffer = buffers.get(key);

        if (buffer == null) {
            IEventBuffer newBuffer = new EventBuffer<>(spotPriceHandler);

            buffer = buffers.putIfAbsent(key, newBuffer);
            if (buffer == null) {
                newBuffer.start();
                return newBuffer;
            }
        }
        return buffer;
    }

}
