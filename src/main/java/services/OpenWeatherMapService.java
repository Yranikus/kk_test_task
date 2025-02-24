package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.OpenWeather.OpenWeatherForecast;
import dto.OpenWeather.OpenWeatherParams;
import dto.OpenWeather.OpenWeatherResponse;
import dto.WeatherForecast;

import java.net.http.HttpClient;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class OpenWeatherMapService extends BaseWeatherService {

    private final String todayUri = "http://api.openweathermap.org/data/2.5/find";
    private final String weekUri = "http://api.openweathermap.org/data/2.5/forecast";
    private final String apiKey;

    public OpenWeatherMapService(HttpClient httpClient, ObjectMapper objectMapper, String apiKey) {
        super(httpClient, objectMapper);
        this.apiKey = apiKey;
    }

    @Override
    public List<WeatherForecast> getTodayWeather(String city) {
        return sendOpenWeatherRequest(todayUri, city);
    }

    @Override
    public List<WeatherForecast> getWeekWeather(String city) {
        return sendOpenWeatherRequest(weekUri, city);
    }

    private List<WeatherForecast> sendOpenWeatherRequest(String uri, String city) {
        return sendGetRequest(uri, new OpenWeatherParams(city, apiKey), List.of())
                .map(body -> mapResponseBody(body, OpenWeatherResponse.class))
                .map(this::checkOkCode)
                .map(openWeatherForecasts -> openWeatherForecasts.stream()
//В бесплатной версии можно делать выборку с интервалами по 3 часа, поэтому тут фильтр по дате через переопределенные equals hashcode
                                .distinct()
                                .map(x -> x.mapToWeatherForecast(city))
                                .collect(Collectors.toList()))
                .orElseGet(Collections::emptyList);
    }

    private List<OpenWeatherForecast> checkOkCode(OpenWeatherResponse response) {
        if (response.cod().equals("200")) return response.list();
        else {
            System.out.println(response.message());
            return Collections.emptyList();
        }
    }
}
