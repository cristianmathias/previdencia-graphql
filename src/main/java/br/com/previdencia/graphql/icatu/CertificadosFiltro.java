package br.com.previdencia.graphql.icatu;

public record CertificadosFiltro(
        String formaElegibilidade,
        String proposta,
        Boolean cliente,
        String produtor,
        Boolean saldoReserva,
        String periodoAcumuladoInicio,
        String periodoAcumuladoFim
) {
}
