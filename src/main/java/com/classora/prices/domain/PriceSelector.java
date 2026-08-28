package com.classora.prices.domain;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class PriceSelector {

    private PriceSelector() {
    }

    public static Optional<Price> selectAt(Collection<Price> candidates, LocalDateTime moment) {
        Objects.requireNonNull(candidates, "The candidates are required.");
        Objects.requireNonNull(moment, "The moment is required.");

        List<Price> applicable = candidates.stream()
                .filter(price -> price.appliesAt(moment))
                .toList();
        if (applicable.isEmpty()) {
            return Optional.empty();
        }

        int highestPriority = applicable.stream()
                .mapToInt(Price::priority)
                .max()
                .orElseThrow();
        List<Price> winners = applicable.stream()
                .filter(price -> price.priority() == highestPriority)
                .toList();
        if (winners.size() > 1) {
            Price collision = winners.getFirst();
            throw new MoreThanOneApplicablePriceWithSamePriorityException(collision.brandId(), collision.productId(), moment, highestPriority, winners.stream().map(Price::priceListId).toList());
        }
        return Optional.of(winners.getFirst());
    }
}
