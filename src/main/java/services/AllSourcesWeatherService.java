package services;

import dto.WeatherForecast;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class AllSourcesWeatherService implements IWeatherService {

    private final IWeatherService openWeatherService;
    private final IWeatherService yandexWeatherService;

    private final ExecutorService executorService;

    public AllSourcesWeatherService(IWeatherService openWeatherService,
                                    IWeatherService yandexWeatherService,
                                    ExecutorService executorService) {
        this.openWeatherService = openWeatherService;
        this.yandexWeatherService = yandexWeatherService;
        this.executorService = executorService;
    }

    @Override
    public List<WeatherForecast> getTodayWeather(String city) {
        return concatFutures(executorService.submit(() -> openWeatherService.getTodayWeather(city)),
        executorService.submit(() -> yandexWeatherService.getTodayWeather(city)));

    }

    @Override
    public List<WeatherForecast> getWeekWeather(String city) {
        return concatFutures(executorService.submit(() -> openWeatherService.getWeekWeather(city)),
        executorService.submit(() -> yandexWeatherService.getWeekWeather(city)));
    }

    private List<WeatherForecast> concatFutures(Future<List<WeatherForecast>> openWeaterFuture, Future<List<WeatherForecast>> yandexWeaterFuture){
        List<WeatherForecast> finalResult = new LinkedList<>();
        try {
            finalResult.addAll(openWeaterFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            System.out.println(e);
        }
        try {
            finalResult.addAll(yandexWeaterFuture.get());
        }catch (InterruptedException | ExecutionException e) {
            System.out.println(e);
        }
        return finalResult;
    }
}
