package com.bolao.v1.core.application.service.palpitecampeao;

import com.bolao.v1.core.domain.entity.palpitecampeao.PalpiteCampeao;
import com.bolao.v1.core.port.in.PalpiteCampeaoPortIn;
import com.bolao.v1.core.port.in.dto.request.palpitecampeao.PalpiteCampeaoCreateRequestDto;
import com.bolao.v1.core.port.out.palpitecampeao.PalpiteCampeaoRepositoryPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PalpiteCampeaoService implements PalpiteCampeaoPortIn {

    private final PalpiteCampeaoRepositoryPortOut repositoryPortOut;

    @Override
    @Transactional
    public PalpiteCampeao registrar(Long usuarioId, PalpiteCampeaoCreateRequestDto request) {
        if (repositoryPortOut.existsByUsuarioId(usuarioId)) {
            throw new IllegalArgumentException("Você já registrou seu palpite de campeão.");
        }

        PalpiteCampeao palpite = PalpiteCampeao.builder()
                .usuarioId(usuarioId)
                .timeSelecionadoId(request.getTimeSelecionadoId())
                .build();

        return repositoryPortOut.save(palpite);
    }

    @Override
    @Transactional(readOnly = true)
    public PalpiteCampeao buscarMeuPalpite(Long usuarioId) {
        return repositoryPortOut.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Nenhum palpite de campeão registrado."));
    }
}
