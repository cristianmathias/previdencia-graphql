package br.com.previdencia.graphql.icatu;

public record RepresentanteLegal(
        String nome,
        String grauParentesco,
        String cpf,
        String dataNascimento
) {
}
