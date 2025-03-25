package org.example.myfirstproject.Services.Impl;



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@Service
public class WeatherAndTimeServiceImpl {
    private final WebClient webClient;

    // API ключ и настройки
    private static final String WEATHER_API_KEY = "9a7e562a385415699456166903904ba5";
    private static final String CITY = "Sofia";
    private static final String COUNTRY_CODE = "BG";

    public WeatherAndTimeServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    // ✅ Метод, който взима и времето, и температурата
    public Mono<String> getWeatherAndTime() {
        String weatherUrl = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s,%s&appid=%s&units=metric",
                CITY, COUNTRY_CODE, WEATHER_API_KEY);

        String timeUrl = "https://timeapi.io/api/Time/current/zone?timeZone=Europe/Sofia";

        // 🌡️ Взимаме температурата
        Mono<String> weatherMono = webClient.get()
                .uri(weatherUrl)
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(Duration.ofSeconds(10))
                .map(response -> {
                    if (response != null && response.containsKey("main")) {
                        Map<String, Object> main = (Map<String, Object>) response.get("main");
                        int temperature = (int) Math.round((double) main.get("temp"));
                        return "Температура: " + temperature + "°C";
                    } else {
                        return "Weather data unavailable";
                    }
                })
                .onErrorResume(e -> {
                    e.printStackTrace();
                    return Mono.just("Weather data unavailable");
                });

        // ⏰ Взимаме датата и часа
        Mono<String> timeMono = webClient.get()
                .uri(timeUrl)
                .retrieve()
                .bodyToMono(String.class)
                .map(this::extractDateTime)
                .onErrorResume(e -> {
                    e.printStackTrace();
                    return Mono.just("Time data unavailable");
                });

        // 🔗 Комбинираме резултатите
        return Mono.zip(weatherMono, timeMono)
                .map(tuple -> tuple.getT1() + " | " + tuple.getT2());
    }

    // 📅 Форматиране на датата и часа от JSON
    private String extractDateTime(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(json);

            String date = rootNode.get("date").asText();  // 03/17/2025
            String time = rootNode.get("time").asText();  // 10:25

            return "Дата: " + date + ", Час: " + time;
        } catch (Exception e) {
            e.printStackTrace();
            return "Грешка при обработката на времето!";
        }
    }
}

