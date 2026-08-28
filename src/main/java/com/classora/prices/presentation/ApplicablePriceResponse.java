package com.classora.prices.presentation;

import java.math.BigDecimal;
import java.time.LocalDateTime;

record ApplicablePriceResponse(
        long productId,
        long brandId,
        long priceList,
        LocalDateTime startDate,
        LocalDateTime endDate,
        BigDecimal price,
        String currency) {
}
