package dto.YandexWeather.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import dto.YandexWeather.geocoder.GeocodeResponse;

import java.io.IOException;

public class GeocoderDeserializer extends JsonDeserializer<GeocodeResponse> {
    @Override
    public GeocodeResponse deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.readValueAsTree();
        String[] coords = node.get("response").get("GeoObjectCollection")
                .get("featureMember").get(0).get("GeoObject")
                .get("Point").get("pos")
                .asText().split(" ");
        //Не знаю почему, но API погоды принимает широту долготу в обратном порядке.
        return new GeocodeResponse(coords[1], coords[0]);
    }
}
