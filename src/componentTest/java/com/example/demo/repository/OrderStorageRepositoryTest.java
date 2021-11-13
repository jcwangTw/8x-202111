package com.example.demo.repository;

import com.example.demo.repository.entity.OrderStorageEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class OrderStorageRepositoryTest {

    @Autowired
    private OrderStorageRepository orderStorageRepository;

    @Test
    public void should_get_order_storage_entity_when_exist_order_storage() {
        // given
        String storageId = "storageId";
        String orderNumber = "order";
        OrderStorageEntity newEntity = OrderStorageEntity.builder()
                .storageId(storageId)
                .orderNumber(orderNumber)
                .build();
        OrderStorageEntity orderStorageEntity = orderStorageRepository.save(newEntity);

        // when
        Optional<OrderStorageEntity> foundEntity = orderStorageRepository.findById(orderStorageEntity.getId());

        // then
        Assertions.assertTrue(foundEntity.isPresent());
        assertEquals(storageId, foundEntity.get().getStorageId());
        assertEquals(orderNumber, foundEntity.get().getOrderNumber());
    }

    @Test
    public void should_get_empty_when_exist_order_storageId_is_not_match() {
        // given
        String storageId = "storageId";
        String orderNumber = "order";
        OrderStorageEntity newEntity = OrderStorageEntity.builder()
                .id(1L)
                .storageId(storageId)
                .orderNumber(orderNumber)
                .build();
        orderStorageRepository.save(newEntity);

        // when
        Optional<OrderStorageEntity> foundEntity = orderStorageRepository.findById(0L);

        // then
        Assertions.assertFalse(foundEntity.isPresent());
    }

}
