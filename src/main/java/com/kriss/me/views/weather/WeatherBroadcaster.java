package com.kriss.me.views.weather;

import com.vaadin.flow.shared.Registration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Component
public class WeatherBroadcaster {
    private static final Executor executor = Executors.newSingleThreadExecutor();
    private static final LinkedList<Consumer<String>> listeners = new LinkedList<>();

    public static synchronized Registration register(Consumer<String> listener) {
        listeners.add(listener);
        return () -> {
            synchronized (WeatherBroadcaster.class) {
                listeners.remove(listener);
            }
        };
    }

    @Scheduled(fixedRate = 300000) // Update every 5 minutes
    public void fetchAndBroadcast() {
        WebClient webClient = WebClient.create();
        webClient.get()
            .uri("https://api.open-meteo.com/v1/forecast?latitude=60.1699&longitude=24.9384&current=temperature_2m,relative_humidity_2m,wind_speed_10m")
            .retrieve()
            .bodyToMono(String.class)
            .subscribe(weatherData -> {
                synchronized (WeatherBroadcaster.class) {
                    for (Consumer<String> listener : listeners) {
                        executor.execute(() -> listener.accept(weatherData));
                    }
                }
            });
    }
}
