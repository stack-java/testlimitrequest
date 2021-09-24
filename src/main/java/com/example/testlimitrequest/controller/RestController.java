package com.example.testlimitrequest.controller;

import com.example.testlimitrequest.service.LimitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class RestController {

    private final LimitService limitService;

    @GetMapping("/ip")
    public ResponseEntity checkIp(HttpServletRequest httpRequest) {
        String ipAddress = httpRequest.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = httpRequest.getRemoteAddr();
        }

        if (limitService.isLimitOver(ipAddress)) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }

        return ResponseEntity.ok().build();

    }

}
