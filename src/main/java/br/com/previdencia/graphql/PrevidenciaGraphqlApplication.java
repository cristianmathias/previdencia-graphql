package br.com.previdencia.graphql;

import br.com.previdencia.graphql.config.IcatuProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(IcatuProperties.class)
public class PrevidenciaGraphqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrevidenciaGraphqlApplication.class, args);
    }
}
