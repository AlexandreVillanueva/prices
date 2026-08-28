package com.classora.prices.presentation;

import com.classora.prices.application.port.in.FindApplicablePriceUseCase;
import com.classora.prices.application.port.in.PriceQuery;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/prices")
class PriceController {

    private final FindApplicablePriceUseCase findApplicablePriceUseCase;

    PriceController(FindApplicablePriceUseCase findApplicablePriceUseCase) {
        this.findApplicablePriceUseCase = findApplicablePriceUseCase;
    }

    @GetMapping("/applicable")
    ResponseEntity<ApplicablePriceResponse> findApplicablePrice(
            @RequestParam @Positive long brandId,
            @RequestParam @Positive long productId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate) {
        return findApplicablePriceUseCase.findApplicablePrice(new PriceQuery(brandId, productId, applicationDate))
                .map(ApplicablePriceResponseMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
