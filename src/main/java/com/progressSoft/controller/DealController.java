package com.progressSoft.controller;

import com.progressSoft.exception.DealAlreadyExistsException;
import com.progressSoft.exception.ErrorResponse;
import com.progressSoft.model.Deal;
import com.progressSoft.service.DealService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/deals")
public class DealController {
    @Autowired
    private DealService dealService;

    private static final Logger logger = LoggerFactory.getLogger(DealController.class);

    @PostMapping("/save-deal")
    public ResponseEntity<?> createDeal(@Valid @RequestBody Deal deal) {
        logger.info("Received deal creation request: {}", deal);
        try {
            Deal savedDeal = dealService.saveDeal(deal);
            logger.info("Deal created successfully with ID: {}", deal.getUniqueId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDeal);
        } catch (DealAlreadyExistsException e) {
            logger.error("Duplicate deal ID: {}", deal.getUniqueId());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("Duplicate deal ID"));
        } catch (Exception e) {
            logger.error("Error creating deal: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while processing the request."));
        }
    }

    @GetMapping("/all-deals")
    public ResponseEntity<List<Deal>> getAllDeals() {
        List<Deal> deals = dealService.getAllDeals();
        logger.info("Retrieved {} deals", deals.size());
        return ResponseEntity.ok(deals);
    }
}
