package dto.OpenWeather;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dto.OpenWeather.deserializers.OpenWeatherDeserializer;

import java.util.List;

public record OpenWeatherResponse(String message, String cod,
                                  @JsonDeserialize(contentUsing = OpenWeatherDeserializer.class)
                                  List<OpenWeatherForecast> list) { }
