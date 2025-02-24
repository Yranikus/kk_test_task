package dto;

import config.constants.Source;

import java.util.Date;

public record WeatherForecast(String city, double temperature, double windSpeed, String weatherConditions, Source source, String date) { }
