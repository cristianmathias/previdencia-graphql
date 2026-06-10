package br.com.previdencia.graphql.graphql;

import br.com.previdencia.graphql.icatu.CertificadosFiltro;
import br.com.previdencia.graphql.icatu.CertificadosPayload;
import br.com.previdencia.graphql.icatu.IcatuCertificadosClient;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
public class CertificadosQueryController {

    private final IcatuCertificadosClient certificadosClient;

    public CertificadosQueryController(IcatuCertificadosClient certificadosClient) {
        this.certificadosClient = certificadosClient;
    }

    @QueryMapping
    Mono<CertificadosPayload> certificados(
            @Argument String idCliente,
            @Argument String idCertificado,
            @Argument CertificadosFiltro filtro
    ) {
        return certificadosClient.buscar(idCliente, idCertificado, filtro);
    }
}
