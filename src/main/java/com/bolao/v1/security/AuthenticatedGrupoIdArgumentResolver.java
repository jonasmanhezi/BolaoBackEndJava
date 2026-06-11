package com.bolao.v1.security;

import com.bolao.v1.core.port.in.GrupoPortIn;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class AuthenticatedGrupoIdArgumentResolver implements HandlerMethodArgumentResolver {

    public static final String GROUP_HEADER = "X-Group-Id";

    private final GrupoPortIn grupoService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticatedGrupoId.class)
                && Long.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        Object attributeGrupoId = webRequest.getAttribute("grupoId", NativeWebRequest.SCOPE_REQUEST);
        if (attributeGrupoId instanceof Long longGrupoId) {
            return longGrupoId;
        }

        String header = webRequest.getHeader(GROUP_HEADER);
        if (header == null || header.isBlank()) {
            throw new IllegalArgumentException("Informe o grupo ativo no header X-Group-Id.");
        }

        Long grupoId;
        try {
            grupoId = Long.parseLong(header.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Grupo inválido.");
        }

        Object attributeUserId = webRequest.getAttribute("userId", NativeWebRequest.SCOPE_REQUEST);
        if (!(attributeUserId instanceof Integer userId)) {
            throw new AccessDeniedException("Usuário não autenticado.");
        }

        grupoService.validarMembro(userId, grupoId);
        return grupoId;
    }
}