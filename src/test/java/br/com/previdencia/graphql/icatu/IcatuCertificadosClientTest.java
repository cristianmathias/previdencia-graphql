package br.com.previdencia.graphql.icatu;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class IcatuCertificadosClientTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void deveConsultarListaQuandoIdCertificadoNaoForInformado() {
        CapturingExchangeFunction exchange = new CapturingExchangeFunction("[{\"numeroCertificado\":\"000000000001\"}]");
        var client = new IcatuCertificadosClient(WebClient.builder()
                .baseUrl("https://api-sandbox.icatuseguros.com.br/relacionamento-parceiro/previdencia/v3")
                .exchangeFunction(exchange)
                .build(), objectMapper);

        StepVerifier.create(client.buscar("12345678901", null, new CertificadosFiltro(
                        "RESGATE", "123", true, "999", true, "2026-01-01", "2026-01-31"
                )))
                .assertNext(payload -> {
                    assertThat(payload.tipo()).isEqualTo(CertificadosPayloadTipo.LISTA);
                    assertThat(payload.certificados()).hasSize(1);
                    assertThat(payload.certificados().getFirst()).containsEntry("numeroCertificado", "000000000001");
                    assertThat(payload.certificado()).isNull();
                    assertThat(payload.json()).contains("numeroCertificado");
                    assertThat(exchange.uri().getPath()).isEqualTo("/relacionamento-parceiro/previdencia/v3/clientes/12345678901/certificados");
                    assertThat(exchange.uri().getQuery()).contains("FormaElegibilidade=RESGATE");
                    assertThat(exchange.uri().getQuery()).contains("Cliente=true");
                    assertThat(exchange.uri().getQuery()).contains("SaldoReserva=true");
                })
                .verifyComplete();
    }

    @Test
    void deveConsultarDetalheQuandoIdCertificadoForInformado() {
        CapturingExchangeFunction exchange = new CapturingExchangeFunction("{\"statusCertificado\":\"ATIVO\"}");
        var client = new IcatuCertificadosClient(WebClient.builder()
                .baseUrl("https://api-sandbox.icatuseguros.com.br/relacionamento-parceiro/previdencia/v3")
                .exchangeFunction(exchange)
                .build(), objectMapper);

        StepVerifier.create(client.buscar("12345678901", "000000000001", null))
                .assertNext(payload -> {
                    assertThat(payload.tipo()).isEqualTo(CertificadosPayloadTipo.DETALHE);
                    assertThat(payload.certificados()).isNull();
                    assertThat(payload.certificado()).containsEntry("statusCertificado", "ATIVO");
                    assertThat(payload.json()).contains("ATIVO");
                    assertThat(exchange.uri().getPath()).isEqualTo("/relacionamento-parceiro/previdencia/v3/clientes/12345678901/certificados/000000000001");
                    assertThat(exchange.uri().getQuery()).isNull();
                })
                .verifyComplete();
    }

    private static final class CapturingExchangeFunction implements ExchangeFunction {
        private final String body;
        private URI uri;

        private CapturingExchangeFunction(String body) {
            this.body = body;
        }

        @Override
        public Mono<ClientResponse> exchange(org.springframework.web.reactive.function.client.ClientRequest request) {
            this.uri = request.url();
            return Mono.just(ClientResponse.create(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(body)
                    .build());
        }

        URI uri() {
            return uri;
        }
    }
}
