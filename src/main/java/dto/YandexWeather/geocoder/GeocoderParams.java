package dto.YandexWeather.geocoder;

import dto.QueryParams;


public record GeocoderParams(String apiKey, String city) implements QueryParams {
    @Override
    public String toStringUriParams() {
        return "?apikey=" + apiKey + "&geocode=" + city + "&format=json";
    }
}
