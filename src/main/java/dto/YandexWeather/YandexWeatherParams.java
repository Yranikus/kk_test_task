package dto.YandexWeather;

import dto.QueryParams;

public record YandexWeatherParams(String lat, String lon, short limit) implements QueryParams {
    @Override
    public String toStringUriParams() {
        return "?lat=" + lat + "&lon=" + lon + "&limit=" + limit + "&lang=ru_RU&extra=false";
    }
}
