package com.anyi.sparrow.common.utils;

import com.google.common.eventbus.AsyncEventBus;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EventBusUtil {
    private static final Executor executor = Executors.newFixedThreadPool(8);

    private static final AsyncEventBus bus = new AsyncEventBus("order-push", executor);

    public static AsyncEventBus getBus() {
        return bus;
    }

    public static void registe(Object o){
        getBus().register(o);
    }
}
