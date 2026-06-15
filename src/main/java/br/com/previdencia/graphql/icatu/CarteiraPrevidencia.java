package br.com.previdencia.graphql.icatu;

import java.util.List;

public record CarteiraPrevidencia(
        String cpf,
        List<CertificadoCarteira> certificados
) {
}
