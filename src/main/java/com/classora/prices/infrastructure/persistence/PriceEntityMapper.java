package com.classora.prices.infrastructure.persistence;

import com.classora.prices.domain.DateRange;
import com.classora.prices.domain.Money;
import com.classora.prices.domain.Price;
import java.util.Currency;

final class PriceEntityMapper {

    private PriceEntityMapper() {
    }

    static Price toDomain(PriceEntity priceEntity) {
        return new Price(
                priceEntity.getBrandId(),
                priceEntity.getProductId(),
                priceEntity.getPriceListId(),
                priceEntity.getPriority(),
                new DateRange(priceEntity.getStartDate(), priceEntity.getEndDate()),
                new Money(priceEntity.getPrice(), Currency.getInstance(priceEntity.getCurrency())));
    }
}
