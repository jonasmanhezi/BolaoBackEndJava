package com.bolao.v1.adapter.externalApi;

import com.bolao.v1.core.port.in.dto.response.partidaExterna.PartidaExternaDto;
import com.bolao.v1.core.port.out.partidaExterna.PartidaExternaPortOut;
import com.bolao.v1.shared.fixture.FixtureApiStatusMapper;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.time.Duration;
import java.util.List;

@Component
@Slf4j
public class PartidaExternaApiClientAdapter implements PartidaExternaPortOut {

    private final RestClient restClient;
    private final int retryMaxAttempts;
    private final long retryDelayMs;

    public PartidaExternaApiClientAdapter(
            @Value("${app.football-api.key}") String apiKey,
            @Value("${app.football-api.url}") String baseUrl,
            @Value("${app.football-api.connect-timeout-ms:10000}") int connectTimeoutMs,
            @Value("${app.football-api.read-timeout-ms:15000}") int readTimeoutMs,
            @Value("${app.football-api.retry-max-attempts:3}") int retryMaxAttempts,
            @Value("${app.football-api.retry-delay-ms:1500}") long retryDelayMs) {

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofMillis(connectTimeoutMs));
        requestFactory.setReadTimeout(Duration.ofMillis(readTimeoutMs));

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("x-apisports-key", apiKey)
                .requestFactory(requestFactory)
                .build();
        this.retryMaxAttempts = Math.max(1, retryMaxAttempts);
        this.retryDelayMs = Math.max(0, retryDelayMs);
    }

    @Override
    public PartidaExternaDto buscarDadosPartidaExterna(Integer externalId) {
        RuntimeException lastFailure = null;

        for (int attempt = 1; attempt <= retryMaxAttempts; attempt++) {
            try {
                return fetchFixture(externalId);
            } catch (ResourceAccessException e) {
                lastFailure = e;
                if (!shouldRetry(attempt)) {
                    break;
                }
                log.warn(
                        "API-Sports I/O falhou (tentativa {}/{}), fixture externalId={}: {}",
                        attempt, retryMaxAttempts, externalId, e.getMessage()
                );
                sleepBeforeRetry(attempt);
            } catch (RestClientResponseException e) {
                if (e.getStatusCode().value() == 429 && shouldRetry(attempt)) {
                    log.warn(
                            "API-Sports rate limit 429 (tentativa {}/{}), fixture externalId={}",
                            attempt, retryMaxAttempts, externalId
                    );
                    sleepBeforeRetry(attempt);
                    lastFailure = e;
                    continue;
                }
                throw e;
            }
        }

        throw lastFailure != null
                ? lastFailure
                : new IllegalStateException("Falha ao consultar fixture externalId=" + externalId);
    }

    private PartidaExternaDto fetchFixture(Integer externalId) {
        ApiFixturesResponse apiResponse = restClient.get()
                .uri("/fixtures?id={id}", externalId)
                .retrieve()
                .body(ApiFixturesResponse.class);

        if (apiResponse == null || apiResponse.response() == null || apiResponse.response().isEmpty()) {
            throw new IllegalStateException("Nenhum fixture retornado pela API-Sports para id=" + externalId);
        }

        FixtureItem item = apiResponse.response().get(0);

        String rawShort = (item.fixture() != null && item.fixture().status() != null)
                ? item.fixture().status().shortStatus()
                : null;

        String normalizedStatus = FixtureApiStatusMapper.toNormalizedLabel(rawShort);

        Integer home = null;
        Integer away = null;

        if (item.goals() != null) {
            home = item.goals().home();
            away = item.goals().away();
        }
        if ((home == null || away == null) && item.score() != null && item.score().fulltime() != null) {
            home = item.score().fulltime().home();
            away = item.score().fulltime().away();
        }

        PartidaExternaDto.FullTime fullTime = null;
        if (home != null || away != null) {
            fullTime = PartidaExternaDto.FullTime.builder()
                    .home(home)
                    .away(away)
                    .build();
        }

        PartidaExternaDto.Score score = null;
        if (fullTime != null) {
            score = PartidaExternaDto.Score.builder()
                    .fullTime(fullTime)
                    .build();
        }

        return PartidaExternaDto.builder()
                .id(item.fixture() != null ? item.fixture().id() : externalId)
                .statusShort(rawShort)
                .status(normalizedStatus)
                .score(score)
                .build();
    }

    private boolean shouldRetry(int attempt) {
        return attempt < retryMaxAttempts;
    }

    private void sleepBeforeRetry(int attempt) {
        if (retryDelayMs <= 0) {
            return;
        }
        long backoff = retryDelayMs * attempt;
        try {
            Thread.sleep(backoff);
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Retry da API-Sports interrompido", interrupted);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record ApiFixturesResponse(List<FixtureItem> response) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record FixtureItem(Fixture fixture, Goals goals, Score score) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record Fixture(Integer id, Status status) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record Status(
            @JsonProperty("short") String shortStatus,
            @JsonProperty("long") String longStatus,
            Integer elapsed
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record Goals(Integer home, Integer away) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record Score(@JsonProperty("fulltime") FullTime fulltime) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record FullTime(Integer home, Integer away) {}
}