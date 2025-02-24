package controller;

import dto.WeatherForecast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import services.IWeatherService;

import java.util.List;

@RestController
public class WeatherRestController {

    @Autowired
    @Qualifier("main")
    private IWeatherService weatherService;

    @GetMapping("today")
    public List<WeatherForecast> getTodayWeatherForecast(@RequestParam(required = false, defaultValue = "Краснодар", name = "city") String city) {
        return weatherService.getTodayWeather(city);
    }

    @GetMapping("week")
    public List<WeatherForecast> getWeekWeatherForecast(@RequestParam(required = false, defaultValue = "Краснодар", name = "city") String city) {
        return weatherService.getWeekWeather(city);
    }
}
