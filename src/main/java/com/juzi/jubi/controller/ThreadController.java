package com.juzi.jubi.controller;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池测试
 *
 * @author codejuzi
 */
@Slf4j
@RestController
@RequestMapping("/thread")
@Profile({"test", "dev"})
public class ThreadController {

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @GetMapping("/add")
    public void add(String taskName) {
        CompletableFuture.runAsync(() -> {
            log.info("执行任务: {}, 执行人: {}", taskName, Thread.currentThread().getName());
            try {
                TimeUnit.MINUTES.sleep(6);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, threadPoolExecutor);
    }

    @GetMapping("/get")
    public String get() {
        // hash map存储线程池状态信息
        Map<String, Object> map = new HashMap<>();

        int queueSize = threadPoolExecutor.getQueue().size();
        map.put("queueSize", queueSize);
        long taskCount = threadPoolExecutor.getTaskCount();
        map.put("taskCount", taskCount);
        long completedTaskCount = threadPoolExecutor.getCompletedTaskCount();
        map.put("completedTaskCount", completedTaskCount);
        int activeCount = threadPoolExecutor.getActiveCount();
        map.put("activeCount", activeCount);
        return JSONUtil.toJsonStr(map);
    }
}
