package com.bolao.v1.api.rest.config;

import com.bolao.v1.security.AuthenticatedGrupoIdArgumentResolver;
import com.bolao.v1.security.AuthenticatedUserIdArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthenticatedUserIdArgumentResolver authenticatedUserIdArgumentResolver;
    private final AuthenticatedGrupoIdArgumentResolver authenticatedGrupoIdArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticatedUserIdArgumentResolver);
        resolvers.add(authenticatedGrupoIdArgumentResolver);
    }
}