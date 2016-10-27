package com.coincelt.traderapi.domain.enumeration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Enum from period time in seconds to obtain data charts
 */
@ToString
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public enum PeriodTime {
    ONE_MINUTE(60), THREE_MINUTES(180), FIVE_MINUTES(300), FIFTEEN_MINUTES(900), HALF_HOUR(1800), HOUR(3600),
    TWO_HOURS(7200), FOUR_HOURS(14400), SIX_HOURS(21600), TWELVE_HOURS(43200), DAY(86400), THREE_DAYS(259200),
    ONE_WEEK(604800);

    @Getter
    private final int seconds;

}
