package br.com.previdencia.graphql.icatu;

import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class IcatuCertificadosClient {

    private final WebClient icatuWebClient;

    public IcatuCertificadosClient(WebClient icatuWebClient) {
        this.icatuWebClient = icatuWebClient;
    }

    public Mono<CertificadosPayload> buscar(String idCliente, String idCertificado, CertificadosFiltro filtro) {
        if (StringUtils.hasText(idCertificado)) {
            return buscarDetalhe(idCliente, idCertificado);
        }

        return buscarLista(idCliente, Optional.ofNullable(filtro).orElse(new CertificadosFiltro(
                null, null, null, null, null, null, null
        )));
    }

    private Mono<CertificadosPayload> buscarLista(String idCliente, CertificadosFiltro filtro) {
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
                .bodyToMono(String.class)
                .map(json -> new CertificadosPayload(CertificadosPayloadTipo.LISTA, json));
    }

    private Mono<CertificadosPayload> buscarDetalhe(String idCliente, String idCertificado) {
        return icatuWebClient.get()
                .uri("/clientes/{idcliente}/certificados/{idcertificado}", idCliente, idCertificado)
                .retrieve()
                .onStatus(status -> status.isError(), response -> response.bodyToMono(String.class)
                        .defaultIfEmpty("")
                        .map(body -> new IcatuApiException(response.statusCode(), body)))
                .bodyToMono(String.class)
                .map(json -> new CertificadosPayload(CertificadosPayloadTipo.DETALHE, json));
    }
}
