package com.progressSoft.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.progressSoft.exception.DealAlreadyExistsException;
import com.progressSoft.exception.ErrorResponse;
import com.progressSoft.exception.GlobalExceptionHandler;
import com.progressSoft.model.Deal;
import com.progressSoft.service.DealService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DealController.class)
class DealControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DealService dealService;

    @Autowired
    private ObjectMapper objectMapper;

    private Deal deal;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());

        deal = new Deal();
        deal.setUniqueId("unique-id");
        deal.setFromCurrency("USD");
        deal.setToCurrency("EUR");
        deal.setDealTimestamp(LocalDateTime.now());
        deal.setDealAmount(1000.00);
    }

    @Test
    void createDeal() throws Exception {
        Mockito.when(dealService.saveDeal(any(Deal.class))).thenReturn(deal);

        String dealJson = objectMapper.writeValueAsString(deal);

        mockMvc.perform(post("/v1/deals/save-deal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dealJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uniqueId").value("unique-id"))
                .andExpect(jsonPath("$.dealAmount").value(1000.0));
    }

    @Test
    void createDeal_duplicate() throws Exception {
        Mockito.when(dealService.saveDeal(any(Deal.class))).thenThrow(new DealAlreadyExistsException("Duplicate deal ID"));

        String dealJson = objectMapper.writeValueAsString(deal);

        mockMvc.perform(post("/v1/deals/save-deal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dealJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Duplicate deal ID"));
    }

    @Test
    void createDeal_validationError() throws Exception {
        deal.setUniqueId(null);  // Make deal invalid

        String dealJson = objectMapper.writeValueAsString(deal);

        mockMvc.perform(post("/v1/deals/save-deal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dealJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Unique ID is mandatory")));
    }

    @Test
    void createDeal_unexpectedError() throws Exception {
        Mockito.when(dealService.saveDeal(any(Deal.class))).thenThrow(new RuntimeException("Unexpected error"));

        String dealJson = objectMapper.writeValueAsString(deal);

        mockMvc.perform(post("/v1/deals/save-deal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dealJson))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("An error occurred while processing the request."));
    }

    @Test
    void handleValidationExceptions() throws Exception {
        // Mock the binding result to simulate validation error
        BindingResult bindingResult = new BeanPropertyBindingResult(deal, "deal");
        bindingResult.addError(new FieldError("deal", "uniqueId", "Unique ID is mandatory"));

        // Manually invoke the validation exception handler
        Method method = DealController.class.getDeclaredMethod("createDeal", Deal.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(methodParameter, bindingResult);

        // Call the exception handler directly and assert the response
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationExceptions(exception);

        // Validate the response
        assert response.getStatusCode() == HttpStatus.BAD_REQUEST;
        assert response.getBody() != null;
        assert response.getBody().getMessage().contains("Unique ID is mandatory");
    }

    @Test
    void testGetAllDeals() throws Exception {
        List<Deal> deals = Arrays.asList(deal, deal);
        Mockito.when(dealService.getAllDeals()).thenReturn(deals);

        mockMvc.perform(get("/v1/deals/all-deals")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].uniqueId").value("unique-id"));
    }
}
