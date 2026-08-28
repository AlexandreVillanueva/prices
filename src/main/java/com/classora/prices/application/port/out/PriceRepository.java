package com.classora.prices.application.port.out;

import com.classora.prices.domain.Price;
import java.time.LocalDateTime;
import java.util.List;

public interface PriceRepository {

    List<Price> findApplicableAt(long brandId, long productId, LocalDateTime moment);
}
