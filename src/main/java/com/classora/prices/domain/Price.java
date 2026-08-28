package com.classora.prices.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public record Price(
        long brandId,
        long productId,
        long priceListId,
        int priority,
        DateRange applicationPeriod,
        Money amount) {

    public Price {
        Objects.requireNonNull(applicationPeriod, "The applicationPeriod is required.");
        Objects.requireNonNull(amount, "The amount is required.");
        if (brandId <= 0) {
            throw new IllegalArgumentException("The brandId (" + brandId + ") must be greater than zero.");
        }
        if (productId <= 0) {
            throw new IllegalArgumentException("The productId (" + productId + ") must be greater than zero.");
        }
        if (priceListId <= 0) {
            throw new IllegalArgumentException("The priceListId (" + priceListId + ") must be greater than zero.");
        }
        if (priority < 0) {
            throw new IllegalArgumentException("The priority (" + priority + ") must not be negative.");
        }
    }

    public boolean appliesAt(LocalDateTime moment) {
        return applicationPeriod.contains(moment);
    }
}
