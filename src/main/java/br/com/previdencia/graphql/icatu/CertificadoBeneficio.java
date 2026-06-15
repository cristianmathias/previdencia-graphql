package br.com.previdencia.graphql.icatu;

import java.util.List;

public record CertificadoBeneficio(
        String codigo,
        String nome,
        String tipo,
        Double valorContribuicao,
        Double valorCapitalSegurado,
        String indiceAtualizacaoMonetaria,
        String intervaloCorrecao,
        String defasagemMeses,
        List<CertificadoBeneficiario> beneficiarios
) {
}
