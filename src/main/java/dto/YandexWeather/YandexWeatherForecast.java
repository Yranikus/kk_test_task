package dto.YandexWeather;

import config.constants.Source;
import dto.WeatherForecast;

import java.util.Date;

public record YandexWeatherForecast(double temperature, double windSpeed, String weatherConditions, Date date) {
    public WeatherForecast mapToWeatherForecast(String  cityName) {
        return new WeatherForecast(cityName, temperature, windSpeed, weatherConditions, Source.YANDEX, date.toString());
    }
}
