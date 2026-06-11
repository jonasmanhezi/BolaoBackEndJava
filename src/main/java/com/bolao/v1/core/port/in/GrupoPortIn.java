package com.bolao.v1.core.port.in;

import com.bolao.v1.core.domain.entity.grupo.Grupo;
import com.bolao.v1.core.port.in.dto.request.grupo.EntrarGrupoRequestDto;

import java.util.List;

public interface GrupoPortIn {

    Grupo entrarComCodigo(Integer usuarioId, EntrarGrupoRequestDto request);

    List<Grupo> listarMeusGrupos(Integer usuarioId);

    void validarMembro(Integer usuarioId, Long grupoId);
}