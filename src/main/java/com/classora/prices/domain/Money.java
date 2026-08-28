package com.classora.prices.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

public record Money(BigDecimal amount, Currency currency) {

    public Money {
        Objects.requireNonNull(amount, "The amount is required.");
        Objects.requireNonNull(currency, "The currency is required.");
        if (amount.signum() < 0) {
            throw new IllegalArgumentException("The amount (" + amount + ") must not be negative.");
        }
        amount = amount.setScale(currency.getDefaultFractionDigits(), RoundingMode.UNNECESSARY);
    }
}
