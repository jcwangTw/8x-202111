package com.example.demo.service;

import com.example.demo.adapter.StorageAdapter;
import com.example.demo.adapter.response.StorageAdapterResponse;
import com.example.demo.exception.BadRequestException;
import com.example.demo.repository.OrderStorageRepository;
import com.example.demo.repository.entity.OrderStorageEntity;
import com.example.demo.response.StorageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StorageService {
    private final OrderStorageRepository orderStorageRepository;
    private final StorageAdapter storageAdapter;

    public StorageResponse getStorageStatus(Long orderId) throws RuntimeException {
        Optional<OrderStorageEntity> orderStorageEntity = orderStorageRepository.findById(orderId);
        if (orderStorageEntity.isPresent()) {
            System.out.println(orderStorageEntity.get().getStorageId());
            StorageAdapterResponse storageAdapterResponse =  storageAdapter.getStatus(orderStorageEntity.get().getStorageId());
            return StorageResponse.builder()
                    .orderNumber(orderStorageEntity.get().getOrderNumber())
                    .storageId(storageAdapterResponse.getStorageId())
                    .status(storageAdapterResponse.getStatus())
                    .build();
        } else {
            throw new BadRequestException("not have storage, please try again later");
        }
    }
}
