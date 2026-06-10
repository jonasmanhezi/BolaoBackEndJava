package com.bolao.v1.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.net.URI;
import java.security.AlgorithmParameters;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class SupabaseTokenValidator {

    @Value("${supabase.url}")
    private String supabaseUrl;

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate = new RestTemplate();

    private PublicKey publicKey;
    private String keyId;

    @PostConstruct
    public void init() {
        try {
            String jwksUrl = supabaseUrl + "/auth/v1/.well-known/jwks.json";
            log.info("Fetching Supabase JWKS from {}", jwksUrl);

            String jwksJson = restTemplate.getForObject(URI.create(jwksUrl), String.class);
            JsonNode jwks = objectMapper.readTree(jwksJson);

            JsonNode keysArray = jwks.get("keys");
            if (keysArray == null || !keysArray.isArray() || keysArray.size() == 0) {
                throw new IllegalStateException("No keys found in Supabase JWKS");
            }

            JsonNode key = null;
            for (JsonNode k : keysArray) {
                if ("EC".equals(k.path("kty").asText()) && "ES256".equals(k.path("alg").asText())) {
                    key = k;
                    break;
                }
            }
            if (key == null) {
                key = keysArray.get(0);
            }

            this.keyId = key.path("kid").asText();
            String xB64 = key.path("x").asText();
            String yB64 = key.path("y").asText();

            byte[] xBytes = Base64.getUrlDecoder().decode(xB64);
            byte[] yBytes = Base64.getUrlDecoder().decode(yB64);

            BigInteger x = new BigInteger(1, xBytes);
            BigInteger y = new BigInteger(1, yBytes);

            AlgorithmParameters parameters = AlgorithmParameters.getInstance("EC");
            parameters.init(new ECGenParameterSpec("secp256r1"));
            ECParameterSpec ecParams = parameters.getParameterSpec(ECParameterSpec.class);

            ECPoint point = new ECPoint(x, y);
            ECPublicKeySpec pubKeySpec = new ECPublicKeySpec(point, ecParams);

            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            this.publicKey = keyFactory.generatePublic(pubKeySpec);

            log.info("Successfully loaded Supabase ES256 public key for JWT verification (kid={})", this.keyId);

        } catch (Exception e) {
            log.error("CRITICAL: Failed to load Supabase JWKS for JWT verification. Login will fail.", e);
            throw new RuntimeException("Failed to initialize Supabase JWT validator from JWKS. Check supabase.url and network access to .well-known/jwks.json", e);
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token);

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Supabase JWT validation failed: {}", e.getMessage());
            return false;
        }
    }

    public UUID extractUserId(String token) {
        String sub = Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
        return UUID.fromString(sub);
    }

    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("email", String.class);
    }

    public String extractName(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        Object userMetadata = claims.get("user_metadata");
        if (userMetadata instanceof java.util.Map) {
            @SuppressWarnings("unchecked")
            java.util.Map<String, Object> meta = (java.util.Map<String, Object>) userMetadata;
            if (meta.containsKey("name")) return (String) meta.get("name");
            if (meta.containsKey("full_name")) return (String) meta.get("full_name");
        }

        Object appMetadata = claims.get("app_metadata");
        if (appMetadata instanceof java.util.Map) {
            @SuppressWarnings("unchecked")
            java.util.Map<String, Object> meta = (java.util.Map<String, Object>) appMetadata;
            if (meta.containsKey("name")) return (String) meta.get("name");
        }

        return claims.get("email", String.class);
    }
}