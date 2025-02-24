package dto.OpenWeather.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import dto.OpenWeather.OpenWeatherForecast;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class OpenWeatherDeserializer extends JsonDeserializer<OpenWeatherForecast> {
    @Override
    public OpenWeatherForecast deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.readValueAsTree();
        double temperature = node.get("main").get("temp").asDouble();
        double windSpeed = node.get("wind").get("speed").asDouble();
        StringBuilder stringBuilder = new StringBuilder();
        for (JsonNode weatherNode : node.get("weather")) {
            stringBuilder.append(weatherNode.get("description").asText());
        }
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(node.get("dt").asLong() * 1000);
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new OpenWeatherForecast(temperature, windSpeed, stringBuilder.toString(), calendar.getTime());
    }
}
