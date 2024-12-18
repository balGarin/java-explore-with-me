package ru.practicum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Client {
    private static final String API_PREFIX = "/hit";
    protected final RestTemplate rest;

    public Client(@Value("${stat-service.url}") String statServiceUrl, RestTemplateBuilder builder) {
        rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(statServiceUrl + API_PREFIX))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build();

    }

    protected ResponseEntity<Object> addStat(StatDtoIn statDtoIn) {
        return post("", null, null, statDtoIn);
    }

    protected <T> ResponseEntity<Object> post(String path, Long userId, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.POST, path, userId, parameters, body);
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, Long userId, @Nullable Map<String, Object> parameters, @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());
        ResponseEntity<Object> statServiceResponse;
        try {
            if (parameters != null) {
                statServiceResponse = rest.exchange(path, method, requestEntity, Object.class, parameters);
            } else {
                statServiceResponse = rest.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareResponse(statServiceResponse);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<MediaType> accept = new ArrayList<>();
        accept.add(MediaType.APPLICATION_JSON);
        headers.setAccept(accept);
        return headers;
    }

    private static ResponseEntity<Object> prepareResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());
        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }
        return responseBuilder.build();
    }


}
