package ru.practicum.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.Client;

@Configuration
public class JavaConfig {
    @Bean
    public Client client(){
        return new Client("http://localhost:9090", new RestTemplateBuilder());
    }
}
