package com.example.testlimitrequest.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class Config {

    @Value("${limit.minuteExpire}")
    private Integer minuteExpire;

    @Value("${limit.maxCountRequest}")
    private Integer maxCountRequest;

}
