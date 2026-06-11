package br.com.previdencia.graphql.icatu;

import java.util.List;

public record Plano(
        String numeroSUSEP,
        String nome,
        String tipoPlano,
        List<Beneficio> beneficios
) {
}
