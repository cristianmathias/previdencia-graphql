package br.com.previdencia.graphql.icatu;

public record Documento(
        String tipo,
        String numero,
        String orgaoExpedidor,
        String dataEmissao
) {
}
