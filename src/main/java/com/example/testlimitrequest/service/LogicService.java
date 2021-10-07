package com.example.testlimitrequest.service;

import com.example.testlimitrequest.advice.IpLimit;
import com.example.testlimitrequest.config.Config;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogicService {

    private final Config config;

    @IpLimit
    public void doSomething() {
        // do some logic
    }

}
