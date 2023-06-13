package com.juzi.jubi.config;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ai服务线程池配置
 *
 * @author codejuzi
 */
@Data
@Configuration
@ConfigurationProperties("jubi.thread")
public class ThreadPoolExecutorConfig {

    private int corePollSize;
    private int maximumPoolSize;

    /**
     * 非核心空闲线程存活时间（单位s）
     */
    private long keepAliveTime;

    /**
     * 队列容量
     */
    private int queueCapacity;


    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        // 创建一个线程工厂
        ThreadFactory threadFactory = new ThreadFactory() {
            private int count = 1;

            @Override
            public Thread newThread(@NotNull Runnable r) {
                String AI_THREAD_NAME_PREFIX = "ai_thread-";
                return new Thread(r, AI_THREAD_NAME_PREFIX + count++);
            }
        };

        return new ThreadPoolExecutor(corePollSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(4), threadFactory);
    }
}
