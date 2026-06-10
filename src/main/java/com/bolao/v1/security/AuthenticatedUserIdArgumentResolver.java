package com.bolao.v1.security;

import org.springframework.core.MethodParameter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthenticatedUserIdArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticatedUserId.class)
                && Integer.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        Object attributeUserId = webRequest.getAttribute("userId", NativeWebRequest.SCOPE_REQUEST);
        if (attributeUserId instanceof Integer integerUserId) {
            return integerUserId;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null) {
            throw new AccessDeniedException("Token JWT ausente ou inválido. Faça login em /auth/login e use Authorize no Swagger.");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof Long longUserId) {
            return longUserId.intValue();
        }
        if (principal instanceof Integer integerPrincipal) {
            return integerPrincipal;
        }

        throw new AccessDeniedException("Não foi possível identificar o usuário autenticado.");
    }
}