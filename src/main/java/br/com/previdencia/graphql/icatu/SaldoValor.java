package br.com.previdencia.graphql.icatu;

public record SaldoValor(
        String dataSaldo,
        Double total,
        Double quantidadeCotasTotal,
        Double valorCotaFechada,
        String dataAcumuladaEfetivaInicial,
        String dataAcumuladaEfetivaFinal,
        Double rentabilidadeAcumulada,
        Double rendimentoAcumuladoBruto
) {
}
