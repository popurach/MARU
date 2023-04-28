package com.bird.maru.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@TestConfiguration
public class MockMvcConfig {

    @Autowired
    private WebApplicationContext context;

    @Bean
    public MockMvc mockMvc() {
        return MockMvcBuilders.webAppContextSetup(context)
                              .apply(SecurityMockMvcConfigurers.springSecurity())
                              .alwaysDo(MockMvcResultHandlers.print())
                              .build();
    }

}
