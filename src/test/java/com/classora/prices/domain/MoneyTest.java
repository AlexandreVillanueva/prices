package com.classora.prices.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.math.BigDecimal;
import java.util.Currency;
import org.junit.jupiter.api.Test;

class MoneyTest {

    @Test
    void normalizesTheScaleToTheDecimalsOfTheCurrency() {
        assertThat(money("35.5", "EUR").amount()).isEqualTo(new BigDecimal("35.50"));
    }

    @Test
    void treatsTheSameAmountWrittenWithDifferentScaleAsEqual() {
        assertThat(money("35.5", "EUR")).isEqualTo(money("35.50", "EUR"));
    }

    @Test
    void rejectsMorePrecisionThanTheCurrencyAllows() {
        assertThatExceptionOfType(ArithmeticException.class).isThrownBy(() -> money("35.505", "EUR"));
    }

    @Test
    void rejectsANegativeAmount() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> money("-0.01", "EUR"));
    }

    @Test
    void acceptsZero() {
        assertThat(money("0", "EUR").amount()).isEqualTo(new BigDecimal("0.00"));
    }

    @Test
    void usesTheDecimalsOfEachCurrency() {
        assertThat(money("35", "JPY").amount()).isEqualTo(new BigDecimal("35"));
        assertThatExceptionOfType(ArithmeticException.class).isThrownBy(() -> money("35.50", "JPY"));
    }

    private Money money(String amount, String currencyIsoCode) {
        return new Money(new BigDecimal(amount), Currency.getInstance(currencyIsoCode));
    }
}
