package com.juzi.jubi.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author codejuzi
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
public class RedissonConfig {

    private String host;

    private String port;

    private Integer database;

    @Bean
    public RedissonClient redissonClient() {
        String redisAddress = String.format("redis://%s:%s", host, port);
        Config config = new Config();
        config.useSingleServer()
                .setDatabase(database)
                .setAddress(redisAddress);
        return Redisson.create(config);
    }
}
