package santander.model.impl;

import santander.model.Price;

import java.math.BigDecimal;

/**
 * Created by bensanger on 31/08/2022.
 */
public class SpotPrice implements Price {

    private final String id;
    private final String instrument;
    private final BigDecimal bid;
    private final BigDecimal ask;

    private final long timeStamp;


    public SpotPrice(String id, String instrument, BigDecimal bid, BigDecimal ask, long timeStamp) {
        this.id = id;
        this.instrument = instrument;
        this.bid = bid;
        this.ask = ask;
        this.timeStamp = timeStamp;
    }

    public String getInstrument() {
        return instrument;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public BigDecimal getBid() {
        return bid;
    }

    @Override
    public BigDecimal getAsk() {
        return ask;
    }

    @Override
    public long getTimeStamp() {
        return timeStamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpotPrice spotPrice = (SpotPrice) o;

        if (timeStamp != spotPrice.timeStamp) return false;
        if (id != null ? !id.equals(spotPrice.id) : spotPrice.id != null) return false;
        if (instrument != null ? !instrument.equals(spotPrice.instrument) : spotPrice.instrument != null) return false;
        if (bid != null ? !bid.equals(spotPrice.bid) : spotPrice.bid != null) return false;
        return ask != null ? ask.equals(spotPrice.ask) : spotPrice.ask == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (instrument != null ? instrument.hashCode() : 0);
        result = 31 * result + (bid != null ? bid.hashCode() : 0);
        result = 31 * result + (ask != null ? ask.hashCode() : 0);
        result = 31 * result + (int) (timeStamp ^ (timeStamp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "SpotPrice{" +
                "id='" + id + '\'' +
                ", instrument='" + instrument + '\'' +
                ", bid=" + bid +
                ", ask=" + ask +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
