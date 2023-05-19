package com.juzi.jubi.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author codejuzi
 */
@SpringBootTest
class ModelConfigPropertiesTest {

    @Resource
    private ModelConfigProperties modelConfigProperties;

    @Test
    void getModelId() {
        System.out.println("--->");
        System.out.println(modelConfigProperties.getModelId());
    }
}