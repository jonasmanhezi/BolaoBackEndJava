package com.bolao.v1.security;

import com.bolao.v1.core.domain.entity.usuario.Usuario;
import com.bolao.v1.core.port.out.usuario.UsuarioRepositoryPortOut;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final SupabaseTokenValidator supabaseTokenValidator;
    private final UsuarioRepositoryPortOut usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (supabaseTokenValidator.validateToken(token)) {
                try {
                    UUID supabaseUserId = supabaseTokenValidator.extractUserId(token);
                    String email = supabaseTokenValidator.extractEmail(token);
                    String name = supabaseTokenValidator.extractName(token);

                    Usuario usuario = usuarioRepository.findBySupabaseUserId(supabaseUserId)
                            .orElseGet(() -> {
                                Usuario newUser = Usuario.builder()
                                        .supabaseUserId(supabaseUserId)
                                        .email(email)
                                        .nome(name != null ? name : email.split("@")[0])
                                        .dataCriacao(java.time.LocalDateTime.now())
                                        .build();
                                return usuarioRepository.save(newUser);
                            });

                    Long appUserId = usuario.getId();

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(appUserId, null, Collections.emptyList());
                    authentication.setDetails(email);

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    request.setAttribute("userId", appUserId.intValue());

                } catch (Exception e) {
                    log.warn("Invalid Supabase JWT token: {}", e.getMessage());
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}