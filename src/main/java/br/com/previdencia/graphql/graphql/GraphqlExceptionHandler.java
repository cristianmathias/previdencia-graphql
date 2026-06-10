package br.com.previdencia.graphql.graphql;

import br.com.previdencia.graphql.icatu.IcatuApiException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import java.util.Map;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;

@Component
public class GraphqlExceptionHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment environment) {
        if (ex instanceof IcatuApiException icatuApiException) {
            return GraphqlErrorBuilder.newError(environment)
                    .message(icatuApiException.getMessage())
                    .extensions(Map.of(
                            "httpStatus", icatuApiException.statusCode().value(),
                            "icatuResponseBody", icatuApiException.responseBody()
                    ))
                    .build();
        }

        return null;
    }
}
