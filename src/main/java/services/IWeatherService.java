package services;

import dto.WeatherForecast;

import java.util.List;


public interface IWeatherService {
    List<WeatherForecast> getTodayWeather(String city);
    List<WeatherForecast> getWeekWeather(String city);
}
