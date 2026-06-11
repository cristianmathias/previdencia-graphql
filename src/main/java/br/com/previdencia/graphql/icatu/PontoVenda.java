package br.com.previdencia.graphql.icatu;

public record PontoVenda(
        Integer codigoUnidadeProdutora,
        String nomeUnidadeProdutora,
        Integer codigoPontoAtendimento,
        String nomePontoAtendimento
) {
}
