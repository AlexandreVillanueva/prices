package com.classora.prices.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class DateRangeTest {

    private static final LocalDateTime START = LocalDateTime.parse("2020-06-14T15:00:00");
    private static final LocalDateTime END = LocalDateTime.parse("2020-06-14T18:30:00");

    private final DateRange dateRange = new DateRange(START, END);

    @Test
    void containsTheExactStart() {
        assertThat(dateRange.contains(START)).isTrue();
    }

    @Test
    void containsTheExactEnd() {
        assertThat(dateRange.contains(END)).isTrue();
    }

    @Test
    void containsAMomentInside() {
        assertThat(dateRange.contains(LocalDateTime.parse("2020-06-14T16:00:00"))).isTrue();
    }

    @Test
    void doesNotContainOneSecondBeforeTheStart() {
        assertThat(dateRange.contains(START.minusSeconds(1))).isFalse();
    }

    @Test
    void doesNotContainOneSecondAfterTheEnd() {
        assertThat(dateRange.contains(END.plusSeconds(1))).isFalse();
    }

    @Test
    void rejectsAnEndBeforeTheStart() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new DateRange(END, START));
    }

    @Test
    void rejectsNulls() {
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> new DateRange(null, END));
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> new DateRange(START, null));
    }
}
