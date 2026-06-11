package br.com.previdencia.graphql.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class IcatuClientConfig {

    @Bean
    WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Bean
    WebClient icatuWebClient(WebClient.Builder builder, IcatuProperties properties) {
        return builder
                .baseUrl(properties.baseUrl())
                .defaultHeader("Ocp-Apim-Subscription-Key", properties.ocpApimSubscriptionKey())
                .defaultHeader("CodigoEmpresa", properties.codigoEmpresa())
                .build();
    }
}
