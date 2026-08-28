package com.classora.prices.infrastructure.persistence;

import com.classora.prices.application.port.out.PriceRepository;
import com.classora.prices.domain.Price;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
class PriceRepositoryAdapter implements PriceRepository {

    private final PriceJpaRepository priceJpaRepository;

    PriceRepositoryAdapter(PriceJpaRepository priceJpaRepository) {
        this.priceJpaRepository = priceJpaRepository;
    }

    @Override
    public List<Price> findApplicableAt(long brandId, long productId, LocalDateTime moment) {
        return priceJpaRepository.findApplicableAt(brandId, productId, moment).stream().map(PriceEntityMapper::toDomain).toList();
    }
}
