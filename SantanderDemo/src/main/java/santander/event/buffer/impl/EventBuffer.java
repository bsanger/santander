package santander.event.buffer.impl;

import santander.event.buffer.IEventBuffer;
import santander.event.listener.EventListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * Unbridled/unthrottled event buffer
 *
 * Created by bensanger on 31/08/2022.
 */
public class EventBuffer<E> extends LinkedBlockingQueue<E> implements Runnable, IEventBuffer<E> {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private final EventListener<E> listener;

    public EventBuffer(EventListener<E> listener) {
        this.listener = listener;
    }

    private volatile boolean running;

    @Override
    public void run() {

        while(running) {
            try {
                // blocked take - unthrottled, we take when not empty
                // drainTo could be used if we took a throttled approach with a circular buffer
                E newItem = take();

                if (newItem != null) {
                    listener.onEvent(newItem);
                }
            }
            catch(Throwable t){
                //TODO handle exception in event buffer
                t.printStackTrace();
            }
        }
    }

    @Override
    public void start() {
        if (running){
            return;
        }

        running = true;
        executorService.submit(this);
    }

    @Override
    public void stop() {
        executorService.shutdown();
        running = false;
    }
}

