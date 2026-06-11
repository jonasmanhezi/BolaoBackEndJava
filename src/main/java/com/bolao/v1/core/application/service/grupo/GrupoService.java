package com.bolao.v1.core.application.service.grupo;

import com.bolao.v1.core.domain.entity.grupo.Grupo;
import com.bolao.v1.core.port.in.GrupoPortIn;
import com.bolao.v1.core.port.in.dto.request.grupo.EntrarGrupoRequestDto;
import com.bolao.v1.core.port.out.grupo.GrupoRepositoryPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GrupoService implements GrupoPortIn {

    private final GrupoRepositoryPortOut grupoRepositoryPortOut;

    @Override
    @Transactional
    public Grupo entrarComCodigo(Integer usuarioId, EntrarGrupoRequestDto request) {
        String codigo = request.getCodigo() == null ? "" : request.getCodigo().trim();

        if (codigo.isEmpty()) {
            throw new IllegalArgumentException("Informe o código do grupo.");
        }

        Grupo grupo = grupoRepositoryPortOut.findByCodigo(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Código de grupo inválido."));

        grupoRepositoryPortOut.adicionarMembro(grupo.getId(), usuarioId.longValue());
        return grupo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Grupo> listarMeusGrupos(Integer usuarioId) {
        return grupoRepositoryPortOut.findByUsuarioId(usuarioId.longValue());
    }

    @Override
    @Transactional(readOnly = true)
    public void validarMembro(Integer usuarioId, Long grupoId) {
        if (grupoId == null) {
            throw new IllegalArgumentException("Grupo não informado.");
        }

        if (!grupoRepositoryPortOut.isMembro(grupoId, usuarioId.longValue())) {
            throw new IllegalArgumentException("Você não pertence a este grupo.");
        }
    }
}