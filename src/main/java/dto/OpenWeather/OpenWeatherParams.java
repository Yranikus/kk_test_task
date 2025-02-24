package dto.OpenWeather;

import dto.QueryParams;

public record OpenWeatherParams(String city, String apiKey) implements QueryParams {
    @Override
    public String toStringUriParams() {
        return "?q=" + city + "&type=like&APPID=" + apiKey + "&units=metric&lang=ru";
    }
}
