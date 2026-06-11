package br.com.previdencia.graphql.icatu;

import java.util.List;
import java.util.Map;

public record CertificadosPayload(
        CertificadosPayloadTipo tipo,
        List<Map<String, Object>> certificados,
        Map<String, Object> certificado,
        String json
) {
}
