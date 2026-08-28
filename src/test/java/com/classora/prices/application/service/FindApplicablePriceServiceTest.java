package com.classora.prices.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.classora.prices.application.port.in.FindApplicablePriceUseCase;
import com.classora.prices.application.port.in.PriceQuery;
import com.classora.prices.application.port.out.PriceRepository;
import com.classora.prices.domain.DateRange;
import com.classora.prices.domain.Money;
import com.classora.prices.domain.Price;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import org.junit.jupiter.api.Test;

class FindApplicablePriceServiceTest {

    private static final Currency EUR = Currency.getInstance("EUR");
    private static final LocalDateTime MOMENT = LocalDateTime.parse("2020-06-14T16:00:00");

    private final Price basePrice = price(1, 0, "2020-06-14T00:00:00", "2020-12-31T23:59:59", "35.50");
    private final Price promotion = price(2, 1, "2020-06-14T15:00:00", "2020-06-14T18:30:00", "25.45");
    private final Price futureRate = price(4, 9, "2020-06-15T16:00:00", "2020-12-31T23:59:59", "38.95");

    @Test
    void appliesTheDomainRuleOverWhatTheRepositoryReturns() {
        FindApplicablePriceUseCase useCase = new FindApplicablePriceService(stubReturning(List.of(basePrice, promotion, futureRate)));

        assertThat(useCase.findApplicablePrice(new PriceQuery(1, 35455, MOMENT))).contains(promotion);
    }

    @Test
    void returnsNothingWhenTheRepositoryFindsNoCandidates() {
        FindApplicablePriceUseCase useCase = new FindApplicablePriceService(stubReturning(List.of()));

        assertThat(useCase.findApplicablePrice(new PriceQuery(1, 35455, MOMENT))).isEmpty();
    }

    @Test
    void asksTheRepositoryForTheSameBrandProductAndMomentOfTheQuery() {
        List<PriceQuery> received = new ArrayList<>();
        PriceRepository spy = (brandId, productId, moment) -> {
            received.add(new PriceQuery(brandId, productId, moment));
            return List.of();
        };

        new FindApplicablePriceService(spy).findApplicablePrice(new PriceQuery(1, 35455, MOMENT));

        assertThat(received).containsExactly(new PriceQuery(1, 35455, MOMENT));
    }

    private PriceRepository stubReturning(List<Price> prices) {
        return (brandId, productId, moment) -> prices;
    }

    private Price price(long priceListId, int priority, String start, String end, String amount) {
        return new Price(1, 35455, priceListId, priority,
                new DateRange(LocalDateTime.parse(start), LocalDateTime.parse(end)),
                new Money(new BigDecimal(amount), EUR));
    }
}
