package santander.event.listener;

/**
 * Created by bensanger on 31/08/2022.
 */
public interface EventListener<E> {
    void onEvent(E e);
}