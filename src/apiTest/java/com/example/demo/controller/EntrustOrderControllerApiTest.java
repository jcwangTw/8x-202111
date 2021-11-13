package com.example.demo.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class EntrustOrderControllerApiTest {
    @Autowired
    private MockMvc mockMvc;
    private ClientAndServer server;

    @BeforeEach
    public void setupMockServer() {
        server = ClientAndServer.startClientAndServer(20212);
    }

    @AfterEach
    public void tearDownServer() {
        server.stop();
    }

    @Test
    public void should_get_storage_status_when_storage_service_return_info() throws Exception {
        // given
        server
            .when(
                request()
                    .withMethod("GET")
                    .withPath("/storage-status/1")
            )
            .respond(
                response()
                    .withStatusCode(200)
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody("{" +
                        "\"storageId\":\"1\"," +
                        "\"status\":\"status\"" +
                        "}"
                    )
            );

        mockMvc.perform(MockMvcRequestBuilders.get("/entrust-order/1/storage-status"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(org.springframework.http.MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.orderNumber").value("orderNumber"))
            .andExpect(jsonPath("$.storageId").value("1"))
            .andExpect(jsonPath("$.status").value("status"))
            .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void should_get_storage_status_when_storage_service_have_cache_return_info() throws Exception {
        // given
        server
            .when(
                request()
                    .withMethod("GET")
                    .withPath("/storage-status/1")
            )
            .respond(
                response()
                    .withStatusCode(200)
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody("{" +
                            "\"storageId\":\"1\"," +
                            "\"status\":\"status\"" +
                        "}"
                    )
            );

        mockMvc.perform(MockMvcRequestBuilders.get("/entrust-order/1/storage-status"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(org.springframework.http.MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.orderNumber").value("orderNumber"))
            .andExpect(jsonPath("$.storageId").value("1"))
            .andExpect(jsonPath("$.status").value("status"))
            .andReturn().getResponse().getContentAsString();

        server
            .when(
                request()
                    .withMethod("GET")
                    .withPath("/storage-status/1")
            )
            .respond(
                response()
                    .withStatusCode(502)
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody("")
            );

        mockMvc.perform(MockMvcRequestBuilders.get("/entrust-order/1/storage-status"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(org.springframework.http.MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.orderNumber").value("orderNumber"))
            .andExpect(jsonPath("$.storageId").value("1"))
            .andExpect(jsonPath("$.status").value("status"))
            .andReturn().getResponse().getContentAsString();
    }


    @Test
    public void should_return_error_when_storage_service_return_info() throws Exception {
        // given
        server
            .when(
                request()
                    .withMethod("GET")
                    .withPath("/storage-status/1")
            )
            .respond(
                response()
                    .withStatusCode(502)
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody("")
            );

        mockMvc.perform(MockMvcRequestBuilders.get("/entrust-order/1/storage-status"))
            .andDo(print())
            .andExpect(status().is5xxServerError())
            .andExpect(content().contentType(org.springframework.http.MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.errorMessage").value("storage service is down, please try again later."))
            .andReturn().getResponse().getContentAsString();
    }
}
