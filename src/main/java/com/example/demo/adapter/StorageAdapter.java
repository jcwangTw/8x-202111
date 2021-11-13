package com.example.demo.adapter;

import com.example.demo.adapter.response.StorageAdapterResponse;
import com.example.demo.config.property.StorageServiceProperty;
import com.example.demo.exception.RestServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
@RequiredArgsConstructor
public class StorageAdapter {

    private final RestTemplate restTemplate;
    private final StorageServiceProperty storageServiceProperty;

    @Cacheable(value = "storage")
    public StorageAdapterResponse getStatus(String storageId) {
        try {
            return restTemplate.getForObject(
                    storageServiceProperty.getHost() +
                            storageServiceProperty.getPaths().get("get-storage-status"),
                    StorageAdapterResponse.class,
                    storageId
            );
        } catch (Exception e) {
            throw new RestServiceException("storage service is down, please try again later.");
        }
    }
}
