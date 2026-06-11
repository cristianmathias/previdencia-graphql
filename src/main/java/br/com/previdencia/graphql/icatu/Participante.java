package br.com.previdencia.graphql.icatu;

import java.util.List;

public record Participante(
        String nomeSocial,
        String nome,
        String cpf,
        String email,
        String telefone,
        String dataNascimento,
        String sexo,
        Double valorPatrimonio,
        Boolean liberaAcessoSaldo,
        String estadoCivil,
        List<String> nacionalidades,
        Boolean cidadaoAmericano,
        String paisNascimento,
        String paisResidencia,
        PaisResidenciaFiscal paisResidenciaFiscal,
        Documento documento,
        InformacaoProfissional informacaoProfissional
) {
}
