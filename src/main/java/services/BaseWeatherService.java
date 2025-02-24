package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.constants.ExceptionsMessages;
import dto.Header;
import dto.QueryParams;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

public abstract class BaseWeatherService implements IWeatherService{

    protected final HttpClient httpClient;
    protected final ObjectMapper objectMapper;

    protected BaseWeatherService(HttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    protected Optional<String> sendGetRequest(String uri, QueryParams queryParams, List<Header> headers){
        HttpRequest httpRequest = buildGetRequest(uri, queryParams, headers);
        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == HttpStatus.OK.value()) return Optional.of(response.body());
            System.out.printf((ExceptionsMessages.httpRequestError), httpRequest.uri().toString(), response.statusCode());
            return Optional.empty();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    protected HttpRequest buildGetRequest(String url, QueryParams queryParams, List<Header> headers){
        HttpRequest.Builder request = HttpRequest.newBuilder().uri(URI.create(url + queryParams.toStringUriParams()));
        for (Header header : headers){
            request.setHeader(header.name(), header.value());
        }
        return request.GET().build();
    }

    protected <T> T mapResponseBody(String body, Class<T> tClass){
        try {
            return objectMapper.readValue(body, tClass);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
