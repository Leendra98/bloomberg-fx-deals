package com.progressSoft.repository;

import com.progressSoft.model.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DealRepository extends JpaRepository<Deal, Long> {
    Optional<Deal> findByUniqueId(String uniqueId);
}
