package com.example.demo.controller;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.RestServiceException;
import com.example.demo.response.StorageResponse;
import com.example.demo.service.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class EntrustOrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StorageService storageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void should_get_storage_status() throws Exception {
        StorageResponse storageResponse = StorageResponse.builder()
                .storageId("storageId")
                .orderNumber("orderNumber")
                .status("status")
                .build();
        Mockito.when(storageService.getStorageStatus(any())).thenReturn(storageResponse);
        mockMvc.perform(MockMvcRequestBuilders.get("/entrust-order/1/storage-status"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderNumber").value("orderNumber"))
                .andExpect(jsonPath("$.storageId").value("storageId"))
                .andExpect(jsonPath("$.status").value("status"))
                .andReturn().getResponse().getContentAsString();


        verify(storageService).getStorageStatus(1L);
    }

    @Test
    public void should_get_error_message() throws Exception {
        Mockito.when(storageService.getStorageStatus(any())).thenThrow(new BadRequestException("error"));
        mockMvc.perform(MockMvcRequestBuilders.get("/entrust-order/1/storage-status"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorMessage").value("error"))
                .andReturn().getResponse().getContentAsString();

    }

    @Test
    public void should_get_error_message_when_service_throw_rest_service_error() throws Exception {
        Mockito.when(storageService.getStorageStatus(any())).thenThrow(new RestServiceException("error"));
        mockMvc.perform(MockMvcRequestBuilders.get("/entrust-order/1/storage-status"))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorMessage").value("error"))
                .andReturn().getResponse().getContentAsString();

    }
}
