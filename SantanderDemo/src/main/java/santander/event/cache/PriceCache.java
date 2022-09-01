package santander.event.cache;

import santander.model.Price;

/**
 * Created by bensanger on 31/08/2022.
 */
public interface PriceCache <P extends Price> {
    void addPrice (P price);
    P getPrice(String key);
}
