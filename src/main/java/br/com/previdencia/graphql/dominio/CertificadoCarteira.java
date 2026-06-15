package br.com.previdencia.graphql.dominio;

import br.com.previdencia.graphql.icatu.CertificadoResumo;
import br.com.previdencia.graphql.icatu.SaldoValor;
import java.util.List;

public record CertificadoCarteira(
        String cpf,
        CertificadoResumo resumo
) {

    public String nome() {
        return resumo.nome();
    }

    public String matricula() {
        return resumo.matricula();
    }

    public String numeroCertificado() {
        return resumo.numeroCertificado();
    }

    public String statusCertificado() {
        return resumo.statusCertificado();
    }

    public List<String> motivo() {
        return resumo.motivo();
    }

    public List<SaldoValor> saldoValor() {
        return resumo.saldoValor();
    }
}
