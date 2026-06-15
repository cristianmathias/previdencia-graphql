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
| `/clientes/{idcliente}/certificados` | `GET` | `icatuCertificados(idCliente, filtro)` | Query AS IS de lista |
| `/clientes/{idcliente}/certificados/{idcertificado}` | `GET` | `icatuCertificado(idCliente, idCertificado)` | Query AS IS de detalhe |
| `/clientes/{idcliente}/certificados/{idcertificado}/produtos/planos/beneficios` | `GET` | `icatuCertificadoBeneficios(idCliente, idCertificado)` | Query AS IS de beneficios |
| `/clientes/{idcliente}/certificados?Cliente=true&SaldoReserva=true` | `GET` | `carteiraPrevidencia(cpf)` | Sempre que a carteira e consultada |
| `/clientes/{idcliente}/certificados/{idcertificado}` | `GET` | `carteiraPrevidencia(cpf) { certificados { detalhe } }` | Somente quando `detalhe` e selecionado |
| `/clientes/{idcliente}/certificados/{idcertificado}/produtos/planos/beneficios` | `GET` | `carteiraPrevidencia(cpf) { certificados { beneficios } }` | Somente quando `beneficios` e selecionado |

Headers enviados para a Icatu em todas as chamadas:

| Header | Origem |
| --- | --- |
| `Ocp-Apim-Subscription-Key` | `icatu.ocp-apim-subscription-key` ou `ICATU_OCP_APIM_SUBSCRIPTION_KEY` |
| `CodigoEmpresa` | `icatu.codigo-empresa` ou `ICATU_CODIGO_EMPRESA` |

## Organizacao das queries

O schema separa dois tipos de query:

| Tipo | Convencao | Objetivo |
| --- | --- | --- |
| AS IS da Icatu | Prefixo `icatu` | Espelhar rotas da Icatu de forma previsivel para debug, validacao e uso tecnico |
| Dominio | Sem prefixo `icatu` | Agregar rotas e expor uma visao pensada para telas/fluxos da aplicacao |

No codigo, os controllers seguem a mesma divisao:

```text
br.com.previdencia.graphql.graphql.icatu
br.com.previdencia.graphql.graphql.dominio
```

Os records que representam respostas da Icatu ficam em:

```text
br.com.previdencia.graphql.icatu
```

Os records de dominio ficam em:

```text
br.com.previdencia.graphql.dominio
```

## Queries AS IS da Icatu

Essas queries representam diretamente as rotas da Icatu implementadas ate agora:

```graphql
type Query {
  icatuCertificados(
    idCliente: String!
    filtro: CertificadosFiltroInput
  ): [CertificadoResumo!]!

  icatuCertificado(
    idCliente: String!
    idCertificado: String!
  ): CertificadoDetalhe!

  icatuCertificadoBeneficios(
    idCliente: String!
    idCertificado: String!
  ): [CertificadoBeneficio!]!
}
```

### Filtros da lista AS IS

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

## Queries de dominio

Essas queries agregam rotas da Icatu e podem fazer lazy loading conforme os campos selecionados:

```graphql
type Query {
  carteiraPrevidencia(cpf: String!): CarteiraPrevidencia!
}
```

Na query `carteiraPrevidencia`, a aplicacao sempre chama primeiro:

```http
GET /clientes/{cpf}/certificados?Cliente=true&SaldoReserva=true
```

Depois, para cada certificado retornado:

| Campo selecionado no GraphQL | Chamada adicional |
| --- | --- |
| `detalhe` | `GET /clientes/{cpf}/certificados/{numeroCertificado}` |
| `beneficios` | `GET /clientes/{cpf}/certificados/{numeroCertificado}/produtos/planos/beneficios` |

Se `detalhe` ou `beneficios` nao forem selecionados na query, essas chamadas adicionais nao sao executadas.

## Campos principais

Campos principais disponiveis em `CertificadoResumo`:

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

Campos principais disponiveis em `CertificadoDetalhe`:

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

### Carteira por CPF

Consulta apenas dados basicos e saldo. Executa somente a rota de lista de certificados:

```graphql
query {
  carteiraPrevidencia(cpf: "12345678901") {
    cpf
    certificados {
      numeroCertificado
      nome
      statusCertificado
      saldoValor {
        dataSaldo
        total
      }
    }
  }
}
```

Consulta dados para uma tela de cards com plano, fundo, tipo e beneficios. Executa a lista e, por certificado, as rotas de detalhe e beneficios:

```graphql
query {
  carteiraPrevidencia(cpf: "12345678901") {
    certificados {
      numeroCertificado
      saldoValor {
        total
      }
      detalhe {
        produto {
          nome
          nomeComercial
          plano {
            tipoPlano
            nome
            beneficios {
              nome
              tipo
              fundos {
                nome
                tipoFundo
                estrategiaFundo
              }
            }
          }
        }
      }
      beneficios {
        codigo
        nome
        tipo
        valorContribuicao
        valorCapitalSegurado
      }
    }
  }
}
```

### Rotas Icatu diretas

Lista AS IS em `GET /clientes/{idcliente}/certificados`:

```graphql
query {
  icatuCertificados(
    idCliente: "12345678901"
    filtro: {
      cliente: true
      saldoReserva: true
    }
  ) {
    numeroCertificado
    statusCertificado
    nome
    saldoValor {
      dataSaldo
      total
    }
  }
}
```

Detalhe AS IS em `GET /clientes/{idcliente}/certificados/{idcertificado}`:

```graphql
query {
  icatuCertificado(
    idCliente: "12345678901"
    idCertificado: "000000000001"
  ) {
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
```

Beneficios AS IS em `GET /clientes/{idcliente}/certificados/{idcertificado}/produtos/planos/beneficios`:

```graphql
query {
  icatuCertificadoBeneficios(
    idCliente: "12345678901"
    idCertificado: "000000000001"
  ) {
    codigo
    nome
    tipo
    valorContribuicao
    valorCapitalSegurado
  }
}
```
