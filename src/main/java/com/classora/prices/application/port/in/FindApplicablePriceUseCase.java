package com.classora.prices.application.port.in;

import com.classora.prices.domain.Price;
import java.util.Optional;

public interface FindApplicablePriceUseCase {

    Optional<Price> findApplicablePrice(PriceQuery priceQuery);
}
