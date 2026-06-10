package br.com.previdencia.graphql.icatu;

public record CertificadosPayload(
        CertificadosPayloadTipo tipo,
        String json
) {
}
