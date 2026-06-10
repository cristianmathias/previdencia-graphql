package br.com.previdencia.graphql.config;

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
    WebClient icatuWebClient(WebClient.Builder builder, IcatuProperties properties) {
        return builder
                .baseUrl(properties.baseUrl())
                .defaultHeader("Ocp-Apim-Subscription-Key", properties.ocpApimSubscriptionKey())
                .defaultHeader("CodigoEmpresa", properties.codigoEmpresa())
                .build();
    }
}
