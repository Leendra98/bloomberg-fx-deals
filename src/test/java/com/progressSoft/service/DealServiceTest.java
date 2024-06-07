
package com.progressSoft.service;

import com.progressSoft.exception.DealAlreadyExistsException;
import com.progressSoft.model.Deal;
import com.progressSoft.repository.DealRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = DealService.class)
public class DealServiceTest {

    @Autowired
    private DealService dealService;

    @MockBean
    private DealRepository dealRepository;

    @Test
    void testSaveDeal() {
        Deal deal = new Deal();
        deal.setUniqueId("unique-id");
        deal.setFromCurrency("USD");
        deal.setToCurrency("EUR");
        deal.setDealTimestamp(LocalDateTime.now());
        deal.setDealAmount(1000.00);

        when(dealRepository.findByUniqueId(any(String.class))).thenReturn(Optional.empty());
        when(dealRepository.save(any(Deal.class))).thenReturn(deal);

        Deal savedDeal = dealService.saveDeal(deal);

        assertNotNull(savedDeal);
        assertEquals("unique-id", savedDeal.getUniqueId());
    }

    @Test
    void testSaveDuplicateDeal() {
        Deal deal = new Deal();
        deal.setUniqueId("unique-id");
        deal.setFromCurrency("USD");
        deal.setToCurrency("EUR");
        deal.setDealTimestamp(LocalDateTime.now());
        deal.setDealAmount(1000.00);

        when(dealRepository.findByUniqueId(any(String.class))).thenReturn(Optional.of(deal));

        assertThrows(DealAlreadyExistsException.class, () -> {
            dealService.saveDeal(deal);
        });
    }
}
