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

O endpoint GraphQL da aplicacao fica em:

```text
POST http://localhost:8080/graphql
```

## APIs implementadas

Até o momento, a aplicacao implementa apenas metodos `GET` do grupo Clientes -> Certificados da API Icatu.

| API Icatu | Metodo | Query GraphQL | Quando e chamada |
| --- | --- | --- | --- |
| `/clientes/{idcliente}/certificados` | `GET` | `certificados(idCliente, filtro)` | Quando `idCertificado` nao e informado |
| `/clientes/{idcliente}/certificados/{idcertificado}` | `GET` | `certificados(idCliente, idCertificado)` | Quando `idCertificado` e informado |

Headers enviados para a Icatu em todas as chamadas:

| Header | Origem |
| --- | --- |
| `Ocp-Apim-Subscription-Key` | `icatu.ocp-apim-subscription-key` ou `ICATU_OCP_APIM_SUBSCRIPTION_KEY` |
| `CodigoEmpresa` | `icatu.codigo-empresa` ou `ICATU_CODIGO_EMPRESA` |

## Query GraphQL

A query disponivel e:

```graphql
type Query {
  certificados(
    idCliente: String!
    idCertificado: String
    filtro: CertificadosFiltroInput
  ): CertificadosPayload!
}
```

### Filtros da lista

Os filtros abaixo sao repassados para `GET /clientes/{idcliente}/certificados`.

| Campo GraphQL | Query param Icatu | Tipo |
| --- | --- | --- |
| `formaElegibilidade` | `FormaElegibilidade` | `String` |
| `proposta` | `Proposta` | `String` |
| `cliente` | `Cliente` | `Boolean` |
| `produtor` | `Produtor` | `String` |
| `saldoReserva` | `SaldoReserva` | `Boolean` |
| `periodoAcumuladoInicio` | `periodoAcumuladoInicio` | `String` |
| `periodoAcumuladoFim` | `periodoAcumuladoFim` | `String` |

### Retorno

O payload GraphQL retorna:

| Campo | Descricao |
| --- | --- |
| `tipo` | `LISTA` para consulta sem `idCertificado`; `DETALHE` para consulta com `idCertificado` |
| `certificados` | Lista tipada retornada por `/clientes/{idcliente}/certificados` |
| `certificado` | Detalhe tipado retornado por `/clientes/{idcliente}/certificados/{idcertificado}` |
| `json` | Corpo original da Icatu como string JSON, util para troubleshooting |

Os retornos tipados sao representados no Java por `records` no pacote `br.com.previdencia.graphql.icatu`.

Campos principais disponiveis em `certificados`:

```graphql
certificados {
  nome
  matricula
  numeroCertificado
  statusCertificado
  motivo
  saldoValor {
    dataSaldo
    total
    quantidadeCotasTotal
    valorCotaFechada
    dataAcumuladaEfetivaInicial
    dataAcumuladaEfetivaFinal
    rentabilidadeAcumulada
    rendimentoAcumuladoBruto
  }
}
```

Campos principais disponiveis em `certificado`:

```graphql
certificado {
  statusCertificado
  dataCancelamento
  motivoCancelamento
  regimeTributacao
  situacaoRegimeTributacao
  numeroProposta
  numeroContrato
  tipoCertificado
  dataImplantacao
  dataInicio
  dataFimDiferimento
  statusFaturamento
  dataProximoReajusteContribuicao
  idadeAposentadoria
  participante { nome cpf email telefone }
  empresa { nome cnpj numeroContrato matriculaFuncionario }
  enderecoCorrespondencia { logradouro bairro cidade uf cep }
  representanteLegal { nome cpf grauParentesco dataNascimento }
  responsavelFinanceiro { nome cpf dataNascimento paisResidencia }
  parceiro { idParceiro parceria { codigo nome } }
  produto {
    codigo
    nome
    nomeComercial
    modalidadeContratacao
    plano {
      numeroSUSEP
      nome
      tipoPlano
      beneficios {
        codigo
        nome
        tipo
        fundos { codigo nome cnpj tipoFundo }
      }
    }
  }
  dadosComercializacao { comissionado codigoProdutor papel email telefone }
  pontoVenda { codigoUnidadeProdutora nomeUnidadeProdutora codigoPontoAtendimento nomePontoAtendimento }
  sucursal { codigo descricao }
}
```

## Exemplos

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
    certificados {
      numeroCertificado
      statusCertificado
      nome
      saldoValor {
        dataSaldo
        total
      }
    }
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
    certificado {
      statusCertificado
      numeroProposta
      produto {
        nome
        plano {
          tipoPlano
          beneficios {
            nome
            fundos {
              nome
              cnpj
            }
          }
        }
      }
      participante {
        nome
        cpf
        email
      }
    }
  }
}
```

O campo `json` continua disponivel e retorna o corpo original da API Icatu como string JSON.
