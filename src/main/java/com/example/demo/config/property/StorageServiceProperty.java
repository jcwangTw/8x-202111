package com.example.demo.config.property;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "service.storage")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageServiceProperty {
    private String host;
    private Map<String, String> paths;
}
