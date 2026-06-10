package br.com.previdencia.graphql.icatu;

import org.springframework.http.HttpStatusCode;

public class IcatuApiException extends RuntimeException {

    private final HttpStatusCode statusCode;
    private final String responseBody;

    public IcatuApiException(HttpStatusCode statusCode, String responseBody) {
        super(buildMessage(statusCode, responseBody));
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    private static String buildMessage(HttpStatusCode statusCode, String responseBody) {
        if (responseBody == null || responseBody.isBlank()) {
            return "Erro ao consultar API Icatu: HTTP " + statusCode.value();
        }

        return "Erro ao consultar API Icatu: HTTP " + statusCode.value() + " - " + responseBody;
    }

    public HttpStatusCode statusCode() {
        return statusCode;
    }

    public String responseBody() {
        return responseBody;
    }
}
