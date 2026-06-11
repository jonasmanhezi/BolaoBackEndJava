package com.bolao.v1.shared.fixture;

/**
 * Maps API-Football fixture short status codes to internal sync categories.
 *
 * @see <a href="https://www.api-football.com/documentation-v3#tag/Fixtures/operation/get-fixtures">API-Football fixtures status</a>
 */
public final class FixtureApiStatusMapper {

    public enum Category {
        /** NS, TBD — scheduled, not started */
        SCHEDULED,
        /** 1H, 2H, HT, ET, BT, P, SUSP, INT, LIVE */
        IN_PLAY,
        /** FT, AET, PEN, AWD, WO */
        FINISHED,
        /** CANC, ABD */
        CANCELLED,
        /** PST — postponed; date may change later */
        POSTPONED,
        /** Unmapped API code */
        UNKNOWN
    }

    private FixtureApiStatusMapper() {
    }

    public static Category categorize(String apiShortStatus) {
        if (apiShortStatus == null || apiShortStatus.isBlank()) {
            return Category.SCHEDULED;
        }

        return switch (apiShortStatus.toUpperCase()) {
            case "NS", "TBD" -> Category.SCHEDULED;
            case "1H", "2H", "HT", "ET", "BT", "P", "SUSP", "INT", "LIVE" -> Category.IN_PLAY;
            case "FT", "AET", "PEN", "AWD", "WO" -> Category.FINISHED;
            case "CANC", "ABD" -> Category.CANCELLED;
            case "PST" -> Category.POSTPONED;
            default -> Category.UNKNOWN;
        };
    }

    /** Legacy normalized label kept on {@code PartidaExternaDto#status} for logging. */
    public static String toNormalizedLabel(Category category) {
        return switch (category) {
            case SCHEDULED -> "NS";
            case IN_PLAY -> "IN_PLAY";
            case FINISHED -> "FINISHED";
            case CANCELLED -> "CANCELLED";
            case POSTPONED -> "POSTPONED";
            case UNKNOWN -> "UNKNOWN";
        };
    }

    public static String toNormalizedLabel(String apiShortStatus) {
        return toNormalizedLabel(categorize(apiShortStatus));
    }
}