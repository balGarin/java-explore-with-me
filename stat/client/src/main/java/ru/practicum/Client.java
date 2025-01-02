package ru.practicum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client {
    protected final RestTemplate rest;

    public Client(@Value("${stat-service.url}") String statServiceUrl, RestTemplateBuilder builder) {
        rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(statServiceUrl))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build();

    }

    public void addStat(String app, String uri, String ip, LocalDateTime timestamp) {
        StatDtoIn statDtoIn = new StatDtoIn(app, uri, ip, timestamp);
        rest.exchange("/hit", HttpMethod.POST, new HttpEntity<>(statDtoIn, defaultHeaders()), Object.class)
                .getStatusCode();
    }

    public List<StatDtoOut> getStats(String start, String end, String[] uris, Boolean unique) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", start);
        parameters.put("end", end);
        parameters.put("uris", uris);
        parameters.put("unique", unique);
        return rest.exchange("/stats?start={start}&end={end}&unique={unique}&uris={uris}", HttpMethod.GET,
                new HttpEntity<>(defaultHeaders()), new ParameterizedTypeReference<List<StatDtoOut>>() {
                }, parameters).getBody();
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}
