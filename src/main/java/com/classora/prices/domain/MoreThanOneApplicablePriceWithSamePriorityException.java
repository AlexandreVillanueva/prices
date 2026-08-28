package com.classora.prices.domain;

import java.time.LocalDateTime;
import java.util.List;

public class MoreThanOneApplicablePriceWithSamePriorityException extends RuntimeException {

    public MoreThanOneApplicablePriceWithSamePriorityException(long brandId, long productId, LocalDateTime moment, int priority, List<Long> priceListIds) {
        super("More than one price has the same priority (" + priority + ") at " + moment + " for the brand " + brandId
                + " and the product " + productId + ". Price lists ids: " + priceListIds);
    }
}
