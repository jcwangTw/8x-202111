package com.example.demo.controller;

import com.example.demo.response.StorageResponse;
import com.example.demo.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/entrust-order")
@RequiredArgsConstructor
public class EntrustOrderController {
    private final StorageService storageService;

    @GetMapping("/{oId}/storage-status")
    public StorageResponse getStorageStatus(@PathVariable(name = "oId") Long oId) {
        return storageService.getStorageStatus(oId);
    }

    @GetMapping("/health-check")
    public String healthCheck() {
        return "health";
    }


}
