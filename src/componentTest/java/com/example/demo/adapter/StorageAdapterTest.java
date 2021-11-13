package com.example.demo.adapter;

import com.example.demo.adapter.response.StorageAdapterResponse;
import com.example.demo.config.property.StorageServiceProperty;
import com.example.demo.exception.RestServiceException;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.DefaultResponseCreator;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class StorageAdapterTest {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String storageId = "1";
    private final StorageServiceProperty storageServiceProperty = StorageServiceProperty.builder()
            .host("https://any")
            .paths(ImmutableMap.of("get-storage-status", "/storage-status/"+storageId))
            .build();
    private final StorageAdapter storageAdapter = new StorageAdapter(restTemplate, storageServiceProperty);

    @Autowired
    public CacheManager cacheManager;

    @Test
    public void should_get_storage_status_when_storage_service_return_status() {
        // given
        MockRestServiceServer mockService = mockStorageService(
                MockRestResponseCreators.withSuccess(
                        "{\"storageId\":\"1\",\"status\":\"test\"}", MediaType.APPLICATION_JSON
                )
        );

        // when
        StorageAdapterResponse storageAdapterResponse = storageAdapter.getStatus(storageId);

        //then
        assertEquals("1", storageAdapterResponse.getStorageId());
        assertEquals("test", storageAdapterResponse.getStatus());
        mockService.verify();
    }

    @Test
    public void should_get_empty_when_storage_service_return_empty() {
        // given
        MockRestServiceServer mockService = mockStorageService(
                MockRestResponseCreators.withSuccess("", MediaType.APPLICATION_JSON)
        );

        // when
        StorageAdapterResponse storageAdapterResponse = storageAdapter.getStatus(storageId);
        // then
        assertNull(storageAdapterResponse);
        mockService.verify();
    }

    @Test
    public void should_get_empty_when_storage_service_error() {
        // given
        MockRestServiceServer mockService = mockStorageService(
                MockRestResponseCreators.withServerError()
        );

        // when
        //then
        Assertions.assertThrows(RestServiceException.class, () -> {
            storageAdapter.getStatus(storageId);
        });
        mockService.verify();
    }

    private MockRestServiceServer mockStorageService(DefaultResponseCreator mockResponse) {
        MockRestServiceServer mockService = MockRestServiceServer.createServer(restTemplate);
        mockService.expect(ExpectedCount.manyTimes(), requestTo("https://any/storage-status/1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(mockResponse);

        return mockService;
    }
}
