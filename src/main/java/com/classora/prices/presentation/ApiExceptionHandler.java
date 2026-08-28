package com.classora.prices.presentation;

import com.classora.prices.domain.MoreThanOneApplicablePriceWithSamePriorityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(MoreThanOneApplicablePriceWithSamePriorityException.class)
    ProblemDetail handleMoreThanOneApplicablePriceWithSamePriority(MoreThanOneApplicablePriceWithSamePriorityException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
        problemDetail.setTitle("We have more than 1 price applicable at the same moment with the same priority.");
        return problemDetail;
    }
}
