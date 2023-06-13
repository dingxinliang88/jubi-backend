package com.juzi.jubi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author codejuzi
 */
@Data
@Component
@ConfigurationProperties(prefix = "jubi.ai.model")
public class ModelConfigProperties {

    /**
     * 模型id
     */
    private Long modelId;
}
