package br.com.previdencia.graphql.icatu;

public record Empresa(
        String numeroContrato,
        String nome,
        String cnpj,
        String dataAdmissao,
        String matriculaFuncionario,
        Double salario,
        String dataDesligamento,
        String motivoDesligamento,
        String reservaLiberada,
        String situacaoFuncionario
) {
}
