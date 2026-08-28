package com.classora.prices.presentation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class FindApplicablePriceEndpointTest {

    private static final String ENDPOINT = "/api/v1/prices/applicable";

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest(name = "Test {index}: {0} -> price list {1}, {2} EUR")
    @CsvSource({
            "2020-06-14T10:00:00, 1, 35.50, 2020-06-14T00:00:00, 2020-12-31T23:59:59",
            "2020-06-14T16:00:00, 2, 25.45, 2020-06-14T15:00:00, 2020-06-14T18:30:00",
            "2020-06-14T21:00:00, 1, 35.50, 2020-06-14T00:00:00, 2020-12-31T23:59:59",
            "2020-06-15T10:00:00, 3, 30.50, 2020-06-15T00:00:00, 2020-06-15T11:00:00",
            "2020-06-16T21:00:00, 4, 38.95, 2020-06-15T16:00:00, 2020-12-31T23:59:59"
    })
    void returnsThePriceOfTheStatementForEachMoment(String applicationDate, long priceList, double price, String startDate, String endDate) throws Exception {
        mockMvc.perform(get(ENDPOINT)
                        .param("brandId", "1")
                        .param("productId", "35455")
                        .param("applicationDate", applicationDate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.priceList").value(priceList))
                .andExpect(jsonPath("$.price").value(price))
                .andExpect(jsonPath("$.currency").value("EUR"))
                .andExpect(jsonPath("$.startDate").value(startDate))
                .andExpect(jsonPath("$.endDate").value(endDate));
    }

    @Test
    void returnsNotFoundWhenNoRateIsApplicable() throws Exception {
        mockMvc.perform(get(ENDPOINT)
                        .param("brandId", "1")
                        .param("productId", "35455")
                        .param("applicationDate", "2021-01-01T00:00:00"))
                .andExpect(status().isNotFound());
    }

    @Test
    void returnsNotFoundForAProductThatDoesNotExist() throws Exception {
        mockMvc.perform(get(ENDPOINT)
                        .param("brandId", "1")
                        .param("productId", "99999")
                        .param("applicationDate", "2020-06-14T10:00:00"))
                .andExpect(status().isNotFound());
    }

    @Test
    void returnsBadRequestWhenTheDateIsNotIso() throws Exception {
        mockMvc.perform(get(ENDPOINT)
                        .param("brandId", "1")
                        .param("productId", "35455")
                        .param("applicationDate", "14-06-2020"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void returnsBadRequestWhenAnIdentifierIsNotPositive() throws Exception {
        mockMvc.perform(get(ENDPOINT)
                        .param("brandId", "0")
                        .param("productId", "35455")
                        .param("applicationDate", "2020-06-14T10:00:00"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnsBadRequestWhenAParameterIsMissing() throws Exception {
        mockMvc.perform(get(ENDPOINT)
                        .param("brandId", "1")
                        .param("productId", "35455"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(statements = "INSERT INTO PRICES (BRAND_ID, START_DATE, END_DATE, PRICE_LIST, PRODUCT_ID, PRIORITY, PRICE, CURR) VALUES (1, '2020-06-14 15:00:00', '2020-06-14 18:30:00', 99, 35455, 1, 99.99, 'EUR')")
    @Sql(statements = "DELETE FROM PRICES WHERE PRICE_LIST = 99", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void returnsConflictWhenTwoApplicableRatesShareTheHighestPriority() throws Exception {
        mockMvc.perform(get(ENDPOINT)
                        .param("brandId", "1")
                        .param("productId", "35455")
                        .param("applicationDate", "2020-06-14T16:00:00"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }
}
