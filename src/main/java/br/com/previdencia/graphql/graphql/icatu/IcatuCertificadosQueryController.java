package br.com.previdencia.graphql.graphql.icatu;

import br.com.previdencia.graphql.icatu.CertificadoBeneficio;
import br.com.previdencia.graphql.icatu.CertificadoDetalhe;
import br.com.previdencia.graphql.icatu.CertificadoResumo;
import br.com.previdencia.graphql.icatu.CertificadosFiltro;
import br.com.previdencia.graphql.icatu.IcatuCertificadosClient;
import java.util.List;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
public class IcatuCertificadosQueryController {

    private final IcatuCertificadosClient certificadosClient;

    public IcatuCertificadosQueryController(IcatuCertificadosClient certificadosClient) {
        this.certificadosClient = certificadosClient;
    }

    @QueryMapping
    Mono<List<CertificadoResumo>> icatuCertificados(
            @Argument String idCliente,
            @Argument CertificadosFiltro filtro
    ) {
        return certificadosClient.listarCertificados(idCliente, filtro);
    }

    @QueryMapping
    Mono<CertificadoDetalhe> icatuCertificado(
            @Argument String idCliente,
            @Argument String idCertificado
    ) {
        return certificadosClient.detalharCertificado(idCliente, idCertificado);
    }

    @QueryMapping
    Mono<List<CertificadoBeneficio>> icatuCertificadoBeneficios(
            @Argument String idCliente,
            @Argument String idCertificado
    ) {
        return certificadosClient.listarBeneficios(idCliente, idCertificado);
    }
}
