package com.classora.prices.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public record DateRange(LocalDateTime start, LocalDateTime end) {

    public DateRange {
        Objects.requireNonNull(start, "The start is required.");
        Objects.requireNonNull(end, "The end is required.");
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("The end (" + end + ") is before the start (" + start + ").");
        }
    }

    public boolean contains(LocalDateTime moment) {
        Objects.requireNonNull(moment, "The moment is required.");
        return !moment.isBefore(start) && !moment.isAfter(end);
    }
}
