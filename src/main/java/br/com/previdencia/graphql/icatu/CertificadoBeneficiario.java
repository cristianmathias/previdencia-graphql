package br.com.previdencia.graphql.icatu;

public record CertificadoBeneficiario(
        String nome,
        String cpf,
        String dataNascimento,
        Integer idParentesco,
        String grauParentesco,
        Double percentualDistribuicao
) {
}
