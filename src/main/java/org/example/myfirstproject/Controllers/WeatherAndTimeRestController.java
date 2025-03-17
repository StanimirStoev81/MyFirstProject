package org.example.myfirstproject.Controllers;

import org.example.myfirstproject.Services.Impl.WeatherAndTimeServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class WeatherAndTimeRestController {

    private final WeatherAndTimeServiceImpl weatherAndTimeServiceIpl;

    public WeatherAndTimeRestController(WeatherAndTimeServiceImpl weatherAndTimeServiceIpl) {
        this.weatherAndTimeServiceIpl = weatherAndTimeServiceIpl;
    }

    // 🆕 Взима и времето, и температурата
    @GetMapping("/weather-time")
    public Mono<String> getWeatherAndTime() {
        return weatherAndTimeServiceIpl.getWeatherAndTime();
    }
}
