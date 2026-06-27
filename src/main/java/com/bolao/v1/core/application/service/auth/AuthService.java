package com.bolao.v1.core.application.service.auth;

import com.bolao.v1.core.domain.entity.usuario.Usuario;
import com.bolao.v1.core.port.in.dto.request.auth.LoginRequestDto;
import com.bolao.v1.core.port.in.dto.request.auth.RegisterRequestDto;
import com.bolao.v1.core.port.in.dto.response.auth.AuthResponseDto;
import com.bolao.v1.core.port.out.usuario.UsuarioRepositoryPortOut;
import com.bolao.v1.security.SupabaseTokenValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UsuarioRepositoryPortOut usuarioRepository;
    private final SupabaseTokenValidator supabaseTokenValidator;
    private final ObjectMapper objectMapper;

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.anon-key}")
    private String supabaseAnonKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public AuthResponseDto register(RegisterRequestDto request) {
        try {
            String signupUrl = supabaseUrl + "/auth/v1/signup";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("apikey", supabaseAnonKey);
            headers.set("Authorization", "Bearer " + supabaseAnonKey);

            com.fasterxml.jackson.databind.node.ObjectNode signupBody = objectMapper.createObjectNode();
            signupBody.put("email", request.getEmail());
            signupBody.put("password", request.getSenha());
            com.fasterxml.jackson.databind.node.ObjectNode dataNode = signupBody.putObject("data");
            dataNode.put("name", request.getNome());

            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(signupBody), headers);
            ResponseEntity<String> response = restTemplate.postForEntity(signupUrl, entity, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new IllegalArgumentException("Falha no registro: " + response.getBody());
            }

            JsonNode json = objectMapper.readTree(response.getBody());
            JsonNode userNode = json.has("user") ? json.get("user") : json;
            if (userNode == null || userNode.get("id") == null || userNode.get("id").isNull()) {
                throw new IllegalArgumentException("Resposta inesperada do Supabase: " + response.getBody());
            }

            String accessToken = null;
            if (json.has("access_token") && json.get("access_token") != null && !json.get("access_token").isNull()) {
                accessToken = json.get("access_token").asText();
            }

            UUID supabaseUserId = UUID.fromString(userNode.get("id").asText());
            String email = userNode.has("email") && userNode.get("email") != null && !userNode.get("email").isNull() 
                ? userNode.get("email").asText() 
                : request.getEmail();
            String name = request.getNome();

            Usuario usuario = ensureLocalProfile(supabaseUserId, email, name);

            return AuthResponseDto.builder()
                    .token(accessToken)
                    .userId(usuario.getId().intValue())
                    .nome(usuario.getNome())
                    .email(usuario.getEmail())
                    .build();

        } catch (Exception e) {
            log.error("Register error", e);
            throw new IllegalArgumentException("Erro ao registrar usuário: " + e.getMessage());
        }
    }

    public AuthResponseDto login(LoginRequestDto request) {
        try {
            String tokenUrl = supabaseUrl + "/auth/v1/token?grant_type=password";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("apikey", supabaseAnonKey);
            headers.set("Authorization", "Bearer " + supabaseAnonKey);

            com.fasterxml.jackson.databind.node.ObjectNode loginBody = objectMapper.createObjectNode();
            loginBody.put("email", request.getEmail());
            loginBody.put("password", request.getSenha());

            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(loginBody), headers);
            ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, entity, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new IllegalArgumentException("Credenciais inválidas");
            }

            JsonNode json = objectMapper.readTree(response.getBody());
            String accessToken = json.has("access_token") && json.get("access_token") != null && !json.get("access_token").isNull() 
                ? json.get("access_token").asText() 
                : null;

            if (accessToken == null) {
                throw new IllegalArgumentException("Supabase não retornou access_token no login");
            }

            if (!supabaseTokenValidator.validateToken(accessToken)) {
                throw new IllegalArgumentException("Token inválido (verifique se o SUPABASE_JWT_SECRET está correto)");
            }

            UUID supabaseUserId = supabaseTokenValidator.extractUserId(accessToken);
            String email = supabaseTokenValidator.extractEmail(accessToken);
            String name = supabaseTokenValidator.extractName(accessToken);

            Usuario usuario = ensureLocalProfile(supabaseUserId, email, name);

            return AuthResponseDto.builder()
                    .token(accessToken)
                    .userId(usuario.getId().intValue())
                    .nome(usuario.getNome())
                    .email(usuario.getEmail())
                    .build();

        } catch (Exception e) {
            log.error("Login error", e);
            throw new IllegalArgumentException("Erro no login: " + e.getMessage());
        }
    }

    private Usuario ensureLocalProfile(UUID supabaseUserId, String email, String name) {
        return usuarioRepository.findBySupabaseUserId(supabaseUserId)
                .orElseGet(() -> {
                    Usuario newUser = Usuario.builder()
                            .supabaseUserId(supabaseUserId)
                            .email(email)
                            .nome(name != null ? name : email.split("@")[0])
                            .dataCriacao(java.time.LocalDateTime.now())
                            .build();
                    return usuarioRepository.save(newUser);
                });
    }
}