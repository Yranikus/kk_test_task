package dto.OpenWeather;


import config.constants.Source;
import dto.WeatherForecast;

import java.util.Date;
import java.util.Objects;

public record OpenWeatherForecast(double temperature, double windSpeed, String weatherConditions, Date date){

    public WeatherForecast mapToWeatherForecast(String  cityName) {
        return new WeatherForecast(cityName, temperature, windSpeed, weatherConditions, Source.OPEN_WEATHER, date.toString());
    }

    //У этого сервиса нельзя в бесплатной версии получить прогноз по дням, только с интервалом в 3 часа для 5 дней
    //Поэтому нужна сортировка по датам т.к. в теле ответа для одного дня несколько прогнозов
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        OpenWeatherForecast other = (OpenWeatherForecast) obj;
        return date.compareTo(other.date) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }
}
