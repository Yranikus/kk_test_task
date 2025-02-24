package config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.constants.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import services.IWeatherService;
import services.OpenWeatherMapService;
import services.AllSourcesWeatherService;
import services.YandexWeatherService;

import java.net.http.HttpClient;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@EnableWebMvc
@Configuration
@ComponentScan(value = {"controller", "services"})
public class ApplicationConfig {

    @Autowired
    private Environment env;

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    @Bean
    @Scope("singleton")
    public HttpClient httpClient() {
        return HttpClient.newHttpClient();
    }

    @Bean("openWeather")
    @Scope("singleton")
    public IWeatherService openWeatherService(HttpClient httpClient, ObjectMapper objectMapper) {
        return new OpenWeatherMapService(httpClient, objectMapper, env.getProperty("openWeatherApiKey"));
    }

    @Bean("yandexWeather")
    @Scope("singleton")
    public IWeatherService yandexWeatherService(HttpClient httpClient, ObjectMapper objectMapper) {
        return new YandexWeatherService(httpClient, objectMapper,
                env.getProperty("yandexWeatherApiKey"),
                env.getProperty("yandexGeocoderApiKey")
        );
    }

    @Bean(name = "executorService")
    @Scope("singleton")
    public ExecutorService executorService(){
        return Executors.newFixedThreadPool(4);
    }

    @Bean("main")
    @Scope("singleton")
    public IWeatherService weatherService(@Qualifier("openWeather") IWeatherService openWeather,
                                          @Qualifier("yandexWeather") IWeatherService yandexWeather,
                                          ExecutorService executorService) {
        Source source = Source.valueOf(env.getProperty("source"));
        return switch (source) {
            case OPEN_WEATHER -> openWeather;
            case YANDEX -> yandexWeather;
            default -> new AllSourcesWeatherService(openWeather, yandexWeather, executorService);
        };
    }
}
