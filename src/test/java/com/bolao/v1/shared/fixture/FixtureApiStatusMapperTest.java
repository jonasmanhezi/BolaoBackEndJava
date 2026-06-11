package com.bolao.v1.shared.fixture;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FixtureApiStatusMapperTest {

    @ParameterizedTest
    @CsvSource({
            "NS, SCHEDULED",
            "TBD, SCHEDULED",
            "1H, IN_PLAY",
            "2H, IN_PLAY",
            "HT, IN_PLAY",
            "ET, IN_PLAY",
            "BT, IN_PLAY",
            "P, IN_PLAY",
            "SUSP, IN_PLAY",
            "INT, IN_PLAY",
            "LIVE, IN_PLAY",
            "FT, FINISHED",
            "AET, FINISHED",
            "PEN, FINISHED",
            "AWD, FINISHED",
            "WO, FINISHED",
            "CANC, CANCELLED",
            "ABD, CANCELLED",
            "PST, POSTPONED"
    })
    void categorize_mapsApiFootballStatuses(String apiStatus, FixtureApiStatusMapper.Category expected) {
        assertEquals(expected, FixtureApiStatusMapper.categorize(apiStatus));
    }

    @Test
    void categorize_unknownStatus_returnsUnknown() {
        assertEquals(FixtureApiStatusMapper.Category.UNKNOWN, FixtureApiStatusMapper.categorize("FOO"));
    }

    @Test
    void categorize_nullOrBlank_returnsScheduled() {
        assertEquals(FixtureApiStatusMapper.Category.SCHEDULED, FixtureApiStatusMapper.categorize(null));
        assertEquals(FixtureApiStatusMapper.Category.SCHEDULED, FixtureApiStatusMapper.categorize("  "));
    }
}