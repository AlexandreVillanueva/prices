package com.classora.prices.application.port.in;

import java.time.LocalDateTime;
import java.util.Objects;

public record PriceQuery(long brandId, long productId, LocalDateTime moment) {

    public PriceQuery {
        Objects.requireNonNull(moment, "The moment is required.");
        if (brandId <= 0) {
            throw new IllegalArgumentException("The brandId (" + brandId + ") must be greater than zero.");
        }
        if (productId <= 0) {
            throw new IllegalArgumentException("The productId (" + productId + ") must be greater than zero.");
        }
    }
}
