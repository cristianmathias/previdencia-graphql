package br.com.previdencia.graphql.icatu;

import java.util.List;

public record CertificadosPayload(
        CertificadosPayloadTipo tipo,
        List<CertificadoResumo> certificados,
        CertificadoDetalhe certificado,
        String json
) {
}
