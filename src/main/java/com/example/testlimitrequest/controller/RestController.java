package com.example.testlimitrequest.controller;

import com.example.testlimitrequest.service.LogicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class RestController {

    private final LogicService service;

    @GetMapping("/ip")
    public ResponseEntity checkIp(HttpServletRequest httpRequest) {
        service.doSomething();
        return ResponseEntity.ok().build();
    }

}
