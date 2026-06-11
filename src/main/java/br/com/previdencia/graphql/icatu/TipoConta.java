package br.com.previdencia.graphql.icatu;

import java.util.List;

public record TipoConta(
        String codigo,
        String descricao,
        String patrocinio,
        List<Fundo> fundos
) {
}
