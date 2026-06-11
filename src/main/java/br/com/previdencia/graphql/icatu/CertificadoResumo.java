package br.com.previdencia.graphql.icatu;

import java.util.List;

public record CertificadoResumo(
        String nome,
        String matricula,
        String numeroCertificado,
        String statusCertificado,
        List<String> motivo,
        List<SaldoValor> saldoValor
) {
}
