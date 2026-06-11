package com.bolao.v1.core.port.in.dto.response.partidaExterna;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties (ignoreUnknown = true)
public class PartidaExternaDto {


    private Integer id;
    /** Raw short status from API-Football (e.g. 1H, FT, PST). */
    private String statusShort;
    /** Normalized category label (IN_PLAY, FINISHED, NS, etc.). */
    private String status;

    @JsonProperty("score")
    private Score score;

    public Integer getPlacarCasa() {
        return (score != null && score.getFullTime () != null) ?
                score.getFullTime().getHome() : null;

    }

    public Integer getPlacarVisitante() {
        return (score != null && score.getFullTime() != null) ?
                score.getFullTime().getAway() : null;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Score {

        @JsonProperty("fullTime")
        private FullTime fullTime;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FullTime {
        private Integer home;
        private Integer away;
    }
}
