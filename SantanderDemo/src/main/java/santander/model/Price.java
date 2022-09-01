package santander.model;

import java.math.BigDecimal;

/**
 * Created by bensanger on 31/08/2022.
 */
public interface Price {

    String getId();

    BigDecimal getBid();

    BigDecimal getAsk();

    long getTimeStamp();
}
