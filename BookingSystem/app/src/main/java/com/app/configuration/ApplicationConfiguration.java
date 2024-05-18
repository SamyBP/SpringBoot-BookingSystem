package com.app.configuration;

import com.app.mappers.IUserMapper;
import com.app.mappers.UserMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfiguration {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public IUserMapper iUserMapper() {
        return new UserMapper();
    }
}
