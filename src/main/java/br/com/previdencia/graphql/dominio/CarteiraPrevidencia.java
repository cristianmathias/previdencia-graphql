package br.com.previdencia.graphql.dominio;

import java.util.List;

public record CarteiraPrevidencia(
        String cpf,
        List<CertificadoCarteira> certificados
) {
}
