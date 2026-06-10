package com.bolao.v1.adapter.externalApi;

import com.bolao.v1.core.port.in.dto.response.partidaExterna.PartidaExternaDto;
import com.bolao.v1.core.port.out.partidaExterna.PartidaExternaPortOut;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class PartidaExternaApiClientAdapter implements PartidaExternaPortOut {

    private final RestClient restClient;

    public PartidaExternaApiClientAdapter(
            @Value("${app.football-api.key}") String apiKey,
            @Value("${app.football-api.url}") String baseUrl) {

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("x-apisports-key", apiKey)
                .build();
    }

    @Override
    public PartidaExternaDto buscarDadosPartidaExterna(Integer externalId) {
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

        String normalizedStatus = normalizeStatus(rawShort);

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
                .status(normalizedStatus)
                .score(score)
                .build();
    }

    private String normalizeStatus(String shortStatus) {
        if (shortStatus == null) {
            return "NS";
        }
        return switch (shortStatus) {
            case "FT", "AET", "PEN" -> "FINISHED";
            case "1H", "2H", "HT", "ET", "BT", "LIVE", "P" -> "IN_PLAY";
            case "NS", "TBD" -> "NS";
            default -> shortStatus;
        };
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
