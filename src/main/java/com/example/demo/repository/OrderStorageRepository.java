package com.example.demo.repository;

import com.example.demo.repository.entity.OrderStorageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStorageRepository extends JpaRepository<OrderStorageEntity, Long> {
}
