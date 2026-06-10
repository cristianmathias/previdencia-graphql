package br.com.previdencia.graphql.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "icatu")
public record IcatuProperties(
        @NotBlank
        String baseUrl,
        @NotBlank
        String ocpApimSubscriptionKey,
        @NotBlank
        String codigoEmpresa
) {
}
