package com.classora.prices.application.service;

import com.classora.prices.application.port.in.FindApplicablePriceUseCase;
import com.classora.prices.application.port.in.PriceQuery;
import com.classora.prices.application.port.out.PriceRepository;
import com.classora.prices.domain.Price;
import com.classora.prices.domain.PriceSelector;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class FindApplicablePriceService implements FindApplicablePriceUseCase {

    private final PriceRepository priceRepository;

    FindApplicablePriceService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Price> findApplicablePrice(PriceQuery priceQuery) {
        Objects.requireNonNull(priceQuery, "The priceQuery is required.");
        List<Price> candidates = priceRepository.findApplicableAt(priceQuery.brandId(), priceQuery.productId(), priceQuery.moment());
        return PriceSelector.selectAt(candidates, priceQuery.moment());
    }
}
