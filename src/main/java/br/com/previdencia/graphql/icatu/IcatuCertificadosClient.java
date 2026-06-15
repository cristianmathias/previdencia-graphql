package br.com.previdencia.graphql.icatu;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class IcatuCertificadosClient {

    private final WebClient icatuWebClient;
    private final ObjectMapper objectMapper;

    public IcatuCertificadosClient(WebClient icatuWebClient, ObjectMapper objectMapper) {
        this.icatuWebClient = icatuWebClient;
        this.objectMapper = objectMapper;
    }

    public Mono<List<CertificadoResumo>> listarCertificadosComSaldo(String idCliente) {
        return listarCertificados(idCliente, new CertificadosFiltro(
                null, null, true, null, true, null, null
        ));
    }

    public Mono<List<CertificadoResumo>> listarCertificados(String idCliente, CertificadosFiltro filtro) {
        return getCertificadosJson(idCliente, Optional.ofNullable(filtro).orElse(new CertificadosFiltro(
                        null, null, null, null, null, null, null
                )))
                .map(this::readCertificados);
    }

    public Mono<CertificadoDetalhe> detalharCertificado(String idCliente, String idCertificado) {
        return getCertificadoDetalheJson(idCliente, idCertificado)
                .map(this::readCertificadoDetalhe);
    }

    public Mono<List<CertificadoBeneficio>> listarBeneficios(String idCliente, String idCertificado) {
        return icatuWebClient.get()
                .uri("/clientes/{idcliente}/certificados/{idcertificado}/produtos/planos/beneficios", idCliente, idCertificado)
                .retrieve()
                .onStatus(status -> status.isError(), response -> response.bodyToMono(String.class)
                        .defaultIfEmpty("")
                        .map(body -> new IcatuApiException(response.statusCode(), body)))
                .bodyToMono(String.class)
                .map(this::readCertificadoBeneficios);
    }

    private Mono<String> getCertificadosJson(String idCliente, CertificadosFiltro filtro) {
        return icatuWebClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path("/clientes/{idcliente}/certificados")
                            .queryParamIfPresent("FormaElegibilidade", Optional.ofNullable(filtro.formaElegibilidade()))
                            .queryParamIfPresent("Proposta", Optional.ofNullable(filtro.proposta()))
                            .queryParamIfPresent("Cliente", Optional.ofNullable(filtro.cliente()))
                            .queryParamIfPresent("Produtor", Optional.ofNullable(filtro.produtor()))
                            .queryParamIfPresent("SaldoReserva", Optional.ofNullable(filtro.saldoReserva()))
                            .queryParamIfPresent("periodoAcumuladoInicio", Optional.ofNullable(filtro.periodoAcumuladoInicio()))
                            .queryParamIfPresent("periodoAcumuladoFim", Optional.ofNullable(filtro.periodoAcumuladoFim()));
                    return builder.build(idCliente);
                })
                .retrieve()
                .onStatus(status -> status.isError(), response -> response.bodyToMono(String.class)
                        .defaultIfEmpty("")
                        .map(body -> new IcatuApiException(response.statusCode(), body)))
                .bodyToMono(String.class);
    }

    private Mono<String> getCertificadoDetalheJson(String idCliente, String idCertificado) {
        return icatuWebClient.get()
                .uri("/clientes/{idcliente}/certificados/{idcertificado}", idCliente, idCertificado)
                .retrieve()
                .onStatus(status -> status.isError(), response -> response.bodyToMono(String.class)
                        .defaultIfEmpty("")
                        .map(body -> new IcatuApiException(response.statusCode(), body)))
                .bodyToMono(String.class);
    }

    private List<CertificadoResumo> readCertificados(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (Exception ex) {
            throw new IllegalStateException("Nao foi possivel ler a lista de certificados retornada pela Icatu.", ex);
        }
    }

    private CertificadoDetalhe readCertificadoDetalhe(String json) {
        try {
            return objectMapper.readValue(json, CertificadoDetalhe.class);
        } catch (Exception ex) {
            throw new IllegalStateException("Nao foi possivel ler o detalhe do certificado retornado pela Icatu.", ex);
        }
    }

    private List<CertificadoBeneficio> readCertificadoBeneficios(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (Exception ex) {
            throw new IllegalStateException("Nao foi possivel ler os beneficios do certificado retornados pela Icatu.", ex);
        }
    }
}
