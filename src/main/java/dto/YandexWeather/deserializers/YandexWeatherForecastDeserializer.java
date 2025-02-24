package dto.YandexWeather.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import dto.YandexWeather.YandexWeatherForecast;

import java.io.IOException;
import java.util.Date;

public class YandexWeatherForecastDeserializer extends JsonDeserializer<YandexWeatherForecast> {
    @Override
    public YandexWeatherForecast deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = jsonParser.readValueAsTree();
        Date date = new Date(node.get("date_ts").asLong() * 1000);
        JsonNode dayForecast = node.get("parts").get("day_short");
        double temp = dayForecast.get("temp").asDouble();
        double windSpeed = dayForecast.get("wind_speed").asDouble();
        String weatherCondition = dayForecast.get("condition").asText();
        return new YandexWeatherForecast(temp, windSpeed, weatherCondition, date);
    }
}
