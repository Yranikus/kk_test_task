package dto.YandexWeather.geocoder;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dto.YandexWeather.deserializers.GeocoderDeserializer;

@JsonDeserialize(using = GeocoderDeserializer.class)
public record GeocodeResponse(String lat, String lon) {
}
