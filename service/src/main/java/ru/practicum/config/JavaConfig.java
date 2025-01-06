package ru.practicum.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.Client;

@Configuration
public class JavaConfig {

    @Value("${stats.url}")
    private String url;

    @Bean
    public Client client() {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        return new Client(url, restTemplateBuilder);
    }
}
