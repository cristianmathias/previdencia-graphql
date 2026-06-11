package br.com.previdencia.graphql.icatu;

public record Fundo(
        String codigo,
        String nome,
        String cnpj,
        String estrategiaFundo,
        TaxaAdministracao taxaAdministracao,
        String taxaPerformance,
        String limiteMaximoTaxaPerformance,
        String tipoFundo,
        Integer prazoCotizacaoAplicacao,
        Integer prazoCotizacaoResgate,
        Integer prazoLiquidacaoResgate,
        Integer prazoPagamentoResgate
) {
}
