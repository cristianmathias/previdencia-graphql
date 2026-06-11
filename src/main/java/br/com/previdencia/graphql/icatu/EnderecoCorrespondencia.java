package br.com.previdencia.graphql.icatu;

public record EnderecoCorrespondencia(
        String apelido,
        String logradouro,
        String bairro,
        String cidade,
        String uf,
        String cep
) {
}
