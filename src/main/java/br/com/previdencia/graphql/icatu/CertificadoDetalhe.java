package br.com.previdencia.graphql.icatu;

import java.util.List;

public record CertificadoDetalhe(
        String statusCertificado,
        String dataCancelamento,
        String motivoCancelamento,
        String regimeTributacao,
        String situacaoRegimeTributacao,
        String numeroProposta,
        String numeroContrato,
        String tipoCertificado,
        String dataImplantacao,
        String dataInicio,
        String dataFimDiferimento,
        String statusFaturamento,
        String dataProximoReajusteContribuicao,
        Integer idadeAposentadoria,
        Participante participante,
        Empresa empresa,
        EnderecoCorrespondencia enderecoCorrespondencia,
        RepresentanteLegal representanteLegal,
        ResponsavelFinanceiro responsavelFinanceiro,
        Parceiro parceiro,
        Produto produto,
        List<DadosComercializacao> dadosComercializacao,
        PontoVenda pontoVenda,
        Sucursal sucursal
) {
}
