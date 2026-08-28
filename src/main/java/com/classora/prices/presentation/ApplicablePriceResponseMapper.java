package com.classora.prices.presentation;

import com.classora.prices.domain.Price;

final class ApplicablePriceResponseMapper {

    private ApplicablePriceResponseMapper() {
    }

    static ApplicablePriceResponse toResponse(Price price) {
        return new ApplicablePriceResponse(
                price.productId(),
                price.brandId(),
                price.priceListId(),
                price.applicationPeriod().start(),
                price.applicationPeriod().end(),
                price.amount().amount(),
                price.amount().currency().getCurrencyCode());
    }
}
