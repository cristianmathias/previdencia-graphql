package br.com.previdencia.graphql.icatu;

public record Produto(
        String codigo,
        String nome,
        String nomeComercial,
        String modalidadeContratacao,
        Plano plano
) {
}
