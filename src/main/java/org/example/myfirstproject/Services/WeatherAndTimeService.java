package org.example.myfirstproject.Services;

import reactor.core.publisher.Mono;

public interface WeatherAndTimeService {

    Mono<String> getWeatherAndTime();
}
