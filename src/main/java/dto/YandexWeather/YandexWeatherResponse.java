package dto.YandexWeather;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dto.YandexWeather.deserializers.YandexWeatherForecastDeserializer;

import java.util.List;

public record YandexWeatherResponse(
        @JsonDeserialize(contentUsing = YandexWeatherForecastDeserializer.class)
        List<YandexWeatherForecast> forecasts) {
}
