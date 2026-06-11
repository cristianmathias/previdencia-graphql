package br.com.previdencia.graphql.icatu;

import java.util.List;

public record Beneficio(
        String codigo,
        String nome,
        String tipo,
        String indiceAtualizacaoMonetaria,
        String periodicidadeAtualizacao,
        String tabuaAtuarial,
        String tipoRenda,
        String garantiaRenda,
        String fatorAtualDeRenda,
        List<Fundo> fundos,
        List<TipoConta> tipoContas
) {
}
