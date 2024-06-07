package com.progressSoft.service;

import com.progressSoft.exception.DealAlreadyExistsException;
import com.progressSoft.model.Deal;
import com.progressSoft.repository.DealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DealService {
    @Autowired
    private DealRepository dealRepository;

    @Transactional
    public Deal saveDeal(Deal deal) {
        Optional<Deal> existingDeal = dealRepository.findByUniqueId(deal.getUniqueId());
        if (existingDeal.isPresent()) {
            throw new DealAlreadyExistsException("Duplicate deal ID: " + deal.getUniqueId());
        }
        return dealRepository.save(deal);
    }

    public List<Deal> getAllDeals() {
        return dealRepository.findAll();
    }
}
