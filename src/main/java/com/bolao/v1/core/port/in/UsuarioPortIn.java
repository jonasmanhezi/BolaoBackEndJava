package com.bolao.v1.core.port.in;

import com.bolao.v1.core.port.in.dto.request.usuario.UsuarioCreateRequestDto;
import com.bolao.v1.core.port.in.dto.request.usuario.UsuarioUpdateRequestDto;
import com.bolao.v1.core.port.in.dto.response.usuario.UsuarioResponseDto;

import java.util.List;

public interface UsuarioPortIn {


    UsuarioResponseDto create(UsuarioCreateRequestDto request);

    UsuarioResponseDto findById(Integer id);

    List<UsuarioResponseDto> findAll();

    UsuarioResponseDto update(Integer id, UsuarioUpdateRequestDto request);

    void delete(Integer id);
}
