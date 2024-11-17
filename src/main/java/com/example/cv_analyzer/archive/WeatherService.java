package com.example.cv_analyzer.archive;

import org.springframework.web.client.RestClient;

import java.util.function.Function;

public class WeatherService implements Function<WeatherService.Request, WeatherService.Response> {

    @Override
    public Response apply(Request request) {

        RestClient restClient = RestClient.builder().build();
        String body = restClient.get()
                .uri("https://wttr.in/wroclaw?format=%t" + request.location)
                .retrieve()
                .body(String.class);
        return new WeatherService.Response(body);
    }

    public record Request(String location) {

    }

    public record Response(String result) {

    }
}
