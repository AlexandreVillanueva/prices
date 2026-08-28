package com.classora.prices.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import org.junit.jupiter.api.Test;

class PriceTest {

    private static final DateRange PERIOD = new DateRange(LocalDateTime.parse("2020-06-14T15:00:00"), LocalDateTime.parse("2020-06-14T18:30:00"));
    private static final Money AMOUNT = new Money(new BigDecimal("25.45"), Currency.getInstance("EUR"));

    @Test
    void appliesInsideItsPeriod() {
        Price price = new Price(1, 35455, 2, 1, PERIOD, AMOUNT);
        assertThat(price.appliesAt(LocalDateTime.parse("2020-06-14T16:00:00"))).isTrue();
    }

    @Test
    void doesNotApplyOutsideItsPeriod() {
        Price price = new Price(1, 35455, 2, 1, PERIOD, AMOUNT);
        assertThat(price.appliesAt(LocalDateTime.parse("2020-06-14T21:00:00"))).isFalse();
    }

    @Test
    void acceptsPriorityZeroBecauseItIsTheBaseRate() {
        assertThat(new Price(1, 35455, 1, 0, PERIOD, AMOUNT).priority()).isZero();
    }

    @Test
    void rejectsANegativePriority() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Price(1, 35455, 1, -1, PERIOD, AMOUNT));
    }

    @Test
    void rejectsIdentifiersThatAreNotGreaterThanZero() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Price(0, 35455, 1, 0, PERIOD, AMOUNT));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Price(1, 0, 1, 0, PERIOD, AMOUNT));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Price(1, 35455, 0, 0, PERIOD, AMOUNT));
    }

    @Test
    void rejectsNulls() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new Price(1, 35455, 1, 0, null, AMOUNT));
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new Price(1, 35455, 1, 0, PERIOD, null));
    }
}
