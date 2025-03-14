package com.kriss.me.views.weather;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Route("weather")
@PageTitle("Weather")
public class WeatherView extends VerticalLayout {
    private final H2 title;
    private final Span temperature;
    private final Span humidity;
    private final Span windSpeed;
    private Registration broadcasterRegistration;

    public WeatherView() {
        addClassName("weather-view");
        setSpacing(true);
        setPadding(true);

        title = new H2("Today's Weather");
        temperature = new Span();
        humidity = new Span();
        windSpeed = new Span();

        add(title, temperature, humidity, windSpeed);
        
        // Initial weather fetch
        fetchWeather();
    }

    private void fetchWeather() {
        WebClient webClient = WebClient.create();
        // Using Helsinki coordinates as an example
        webClient.get()
            .uri("https://api.open-meteo.com/v1/forecast?latitude=60.1699&longitude=24.9384&current=temperature_2m,relative_humidity_2m,wind_speed_10m")
            .retrieve()
            .bodyToMono(String.class)
            .subscribe(this::updateWeatherInfo);
    }

    private void updateWeatherInfo(String weatherData) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(weatherData);
            JsonNode current = root.get("current");

            getUI().ifPresent(ui -> ui.access(() -> {
                temperature.setText(String.format("Temperature: %.1f°C", current.get("temperature_2m").asDouble()));
                humidity.setText(String.format("Humidity: %d%%", current.get("relative_humidity_2m").asInt()));
                windSpeed.setText(String.format("Wind Speed: %.1f m/s", current.get("wind_speed_10m").asDouble()));
            }));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        // Schedule periodic updates
        broadcasterRegistration = WeatherBroadcaster.register(weatherData -> {
            getUI().ifPresent(ui -> ui.access(() -> updateWeatherInfo(weatherData)));
        });
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        if (broadcasterRegistration != null) {
            broadcasterRegistration.remove();
            broadcasterRegistration = null;
        }
    }
}
