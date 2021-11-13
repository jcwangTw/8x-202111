package com.example.demo.service;


import com.example.demo.adapter.StorageAdapter;
import com.example.demo.adapter.response.StorageAdapterResponse;
import com.example.demo.exception.BadRequestException;
import com.example.demo.repository.OrderStorageRepository;
import com.example.demo.repository.entity.OrderStorageEntity;
import com.example.demo.response.StorageResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StorageServiceTest {
    @InjectMocks
    private StorageService storageService;

    @Mock
    private OrderStorageRepository orderStorageRepository;

    @Mock
    private StorageAdapter storageAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void should_get_storage_response_when_storage_is_exist() {
        // given
        String orderNumber = "orderNumber";
        String storageId = "storageId";
        String status = "status";
        Long id = 1L;
        Optional<OrderStorageEntity> orderStroageEntityOptional = Optional.of(
                OrderStorageEntity.builder().id(id).storageId(storageId).orderNumber(orderNumber).build()
        );
        StorageAdapterResponse storageAdapterResponse = StorageAdapterResponse.builder()
                .storageId(storageId)
                .status(status)
                .build();
        when(orderStorageRepository.findById(any())).thenReturn(orderStroageEntityOptional);
        when(storageAdapter.getStatus(storageId)).thenReturn(storageAdapterResponse);

        // when
        StorageResponse storageResponse = storageService.getStorageStatus(id);

        // then
        assertEquals(orderNumber, storageResponse.getOrderNumber());
        assertEquals(storageId, storageResponse.getStorageId());
        assertEquals(status, storageResponse.getStatus());
        verify(orderStorageRepository).findById(any());
        verify(storageAdapter).getStatus(storageId);
    }

    @Test
    public void should_throw_exception_when_storage_is_not_exist() {
        // given
        Long id = 1L;
        Optional<OrderStorageEntity> orderStorageEntityOptional = Optional.empty();
        when(orderStorageRepository.findById(any())).thenReturn(orderStorageEntityOptional);

        // when
        // then
        Assertions.assertThrows(BadRequestException.class, () -> {
            storageService.getStorageStatus(id);
        });
    }
}
