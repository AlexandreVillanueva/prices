package com.classora.prices.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import org.junit.jupiter.api.Test;

class PriceSelectorTest {

    private static final Currency EUR = Currency.getInstance("EUR");
    private static final LocalDateTime MOMENT = LocalDateTime.parse("2020-06-14T16:00:00");

    private final Price basePrice = price(1, 0, "2020-06-14T00:00:00", "2020-12-31T23:59:59", "35.50");
    private final Price promotion = price(2, 1, "2020-06-14T15:00:00", "2020-06-14T18:30:00", "25.45");

    @Test
    void returnsNothingWhenThereAreNoCandidates() {
        assertThat(PriceSelector.selectAt(List.of(), MOMENT)).isEmpty();
    }

    @Test
    void returnsNothingWhenNoCandidateIsApplicable() {
        assertThat(PriceSelector.selectAt(List.of(promotion), LocalDateTime.parse("2020-06-14T21:00:00"))).isEmpty();
    }

    @Test
    void returnsTheOnlyApplicableCandidate() {
        assertThat(PriceSelector.selectAt(List.of(basePrice, promotion), LocalDateTime.parse("2020-06-14T21:00:00")))
                .contains(basePrice);
    }

    @Test
    void returnsTheApplicableCandidateWithTheHighestPriority() {
        assertThat(PriceSelector.selectAt(List.of(basePrice, promotion), MOMENT)).contains(promotion);
    }

    @Test
    void ignoresAHigherPriorityThatIsNotApplicableAtThatMoment() {
        Price futureRate = price(4, 9, "2020-06-15T16:00:00", "2020-12-31T23:59:59", "38.95");

        assertThat(PriceSelector.selectAt(List.of(basePrice, promotion, futureRate), MOMENT)).contains(promotion);
    }

    @Test
    void failsWhenTwoApplicableCandidatesShareTheHighestPriority() {
        Price collision = price(3, 1, "2020-06-14T15:00:00", "2020-06-14T18:30:00", "30.50");

        assertThatExceptionOfType(MoreThanOneApplicablePriceWithSamePriorityException.class)
                .isThrownBy(() -> PriceSelector.selectAt(List.of(basePrice, promotion, collision), MOMENT))
                .withMessageContaining("[2, 3]")
                .withMessageContaining("the brand 1")
                .withMessageContaining("the product 35455");
    }

    @Test
    void doesNotFailWhenTheTiedCandidatesAreNotApplicableAtTheSameMoment() {
        Price otherDay = price(3, 1, "2020-06-15T00:00:00", "2020-06-15T11:00:00", "30.50");

        assertThat(PriceSelector.selectAt(List.of(basePrice, promotion, otherDay), MOMENT)).contains(promotion);
    }

    private Price price(long priceListId, int priority, String start, String end, String amount) {
        return new Price(1, 35455, priceListId, priority,
                new DateRange(LocalDateTime.parse(start), LocalDateTime.parse(end)),
                new Money(new BigDecimal(amount), EUR));
    }
}
