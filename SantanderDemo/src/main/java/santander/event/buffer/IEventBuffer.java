package santander.event.buffer;

import java.util.Queue;

/**
 * Created by bensanger on 31/08/2022.
 */
public interface IEventBuffer<E> extends Queue<E> {

    void start();

    void stop();
}
