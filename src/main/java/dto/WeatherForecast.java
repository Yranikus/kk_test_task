package dto;

import config.constants.Source;

public record WeatherForecast(String city, double temperature, double windSpeed, String weatherConditions, Source source, String date) { }
