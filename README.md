# Previdencia GraphQL

Projeto Spring Boot que expõe uma camada GraphQL para os endpoints GET de Clientes -> Certificados da API Icatu Previdencia Parceiro v3.

## Configuracao

As credenciais ficam em `src/main/resources/application.yml` e podem ser sobrescritas por variaveis de ambiente:

```bash
export ICATU_OCP_APIM_SUBSCRIPTION_KEY="sua-chave"
export ICATU_CODIGO_EMPRESA="seu-codigo"
```

A URL base configurada e:

```text
https://api-sandbox.icatuseguros.com.br/relacionamento-parceiro/previdencia/v3
```

## Rodando

```bash
GRADLE_USER_HOME=$PWD/.gradle ./gradlew bootRun
```

O GraphiQL fica disponivel em:

```text
http://localhost:8080/graphiql
```

## Query

Sem `idCertificado`, consulta a lista em `GET /clientes/{idcliente}/certificados`:

```graphql
query {
  certificados(
    idCliente: "12345678901"
    filtro: {
      cliente: true
      saldoReserva: true
    }
  ) {
    tipo
    json
  }
}
```

Com `idCertificado`, consulta o detalhe em `GET /clientes/{idcliente}/certificados/{idcertificado}`:

```graphql
query {
  certificados(
    idCliente: "12345678901"
    idCertificado: "000000000001"
  ) {
    tipo
    json
  }
}
```

O campo `json` retorna o corpo original da API Icatu como string JSON.
