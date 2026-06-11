package br.com.previdencia.graphql.icatu;

public record ResponsavelFinanceiro(
        String nome,
        String dataNascimento,
        String cpf,
        String paisResidencia
) {
}
