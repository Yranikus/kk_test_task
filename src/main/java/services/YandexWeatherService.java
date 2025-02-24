package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.Header;
import dto.WeatherForecast;
import dto.YandexWeather.YandexWeatherParams;
import dto.YandexWeather.YandexWeatherResponse;
import dto.YandexWeather.geocoder.GeocodeResponse;
import dto.YandexWeather.geocoder.GeocoderParams;

import java.net.http.HttpClient;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class YandexWeatherService extends BaseWeatherService {

    private final String weatherUri = "https://api.weather.yandex.ru/v2/forecast";
    private final String geocoderUri = "https://geocode-maps.yandex.ru/1.x/";

    private final Header authHeader;

    private final String geocoderApiKey;

    public YandexWeatherService(HttpClient httpClient, ObjectMapper objectMapper,
                                String weatherApiKey, String geocoderApiKey) {
        super(httpClient, objectMapper);
        this.geocoderApiKey = geocoderApiKey;
        authHeader = new Header("X-Yandex-Weather-Key", weatherApiKey);
    }

    @Override
    public List<WeatherForecast> getTodayWeather(String city) {
        return sendYandexWeatherRequest(city, (short) 1);
    }

    @Override
    public List<WeatherForecast> getWeekWeather(String city) {
        return sendYandexWeatherRequest(city, (short) 7);
    }

    private List<WeatherForecast> sendYandexWeatherRequest(String city, short limit) {
        return getCityCoords(city).flatMap(cords ->
                sendGetRequest(weatherUri, new YandexWeatherParams(cords.lat(), cords.lon(), limit), List.of(authHeader))
                        .map(body -> mapResponseBody(body, YandexWeatherResponse.class)
                                .forecasts().stream().map(x -> x.mapToWeatherForecast(city))
                                .collect(Collectors.toList()))).orElseGet(Collections::emptyList);
    }

    public Optional<GeocodeResponse> getCityCoords(String city) {
        return sendGetRequest(geocoderUri, new GeocoderParams(geocoderApiKey, city), List.of())
                .map(body -> mapResponseBody(body, GeocodeResponse.class));
    }
}
