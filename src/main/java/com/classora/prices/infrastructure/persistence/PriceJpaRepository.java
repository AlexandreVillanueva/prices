package com.classora.prices.infrastructure.persistence;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface PriceJpaRepository extends JpaRepository<PriceEntity, Long> {

    @Query("""
            select price from PriceEntity price
            where price.brandId = :brandId
              and price.productId = :productId
              and :moment between price.startDate and price.endDate
            """)
    List<PriceEntity> findApplicableAt(@Param("brandId") long brandId, @Param("productId") long productId, @Param("moment") LocalDateTime moment);
}
