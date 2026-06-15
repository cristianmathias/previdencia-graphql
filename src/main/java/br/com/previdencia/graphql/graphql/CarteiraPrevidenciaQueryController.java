package br.com.previdencia.graphql.graphql;

import br.com.previdencia.graphql.icatu.CarteiraPrevidencia;
import br.com.previdencia.graphql.icatu.CertificadoBeneficio;
import br.com.previdencia.graphql.icatu.CertificadoCarteira;
import br.com.previdencia.graphql.icatu.CertificadoDetalhe;
import br.com.previdencia.graphql.icatu.IcatuCertificadosClient;
import java.util.List;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
public class CarteiraPrevidenciaQueryController {

    private final IcatuCertificadosClient certificadosClient;

    public CarteiraPrevidenciaQueryController(IcatuCertificadosClient certificadosClient) {
        this.certificadosClient = certificadosClient;
    }

    @QueryMapping
    Mono<CarteiraPrevidencia> carteiraPrevidencia(@Argument String cpf) {
        return certificadosClient.listarCertificadosComSaldo(cpf)
                .map(certificados -> certificados.stream()
                        .map(certificado -> new CertificadoCarteira(cpf, certificado))
                        .toList())
                .map(certificados -> new CarteiraPrevidencia(cpf, certificados));
    }

    @SchemaMapping(typeName = "CertificadoCarteira", field = "detalhe")
    Mono<CertificadoDetalhe> detalhe(CertificadoCarteira certificado) {
        return certificadosClient.detalharCertificado(certificado.cpf(), certificado.numeroCertificado());
    }

    @SchemaMapping(typeName = "CertificadoCarteira", field = "beneficios")
    Mono<List<CertificadoBeneficio>> beneficios(CertificadoCarteira certificado) {
        return certificadosClient.listarBeneficios(certificado.cpf(), certificado.numeroCertificado());
    }
}
