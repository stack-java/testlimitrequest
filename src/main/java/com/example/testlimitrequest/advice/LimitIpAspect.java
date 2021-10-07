package com.example.testlimitrequest.advice;

import com.example.testlimitrequest.config.Config;
import com.example.testlimitrequest.execption.LimitIpException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LimitIpAspect {

    private final Config config;

    private ConcurrentHashMap<String, LinkedList<LocalDateTime>> ipMap =new ConcurrentHashMap<>();

    @Pointcut("@annotation(com.example.testlimitrequest.advice.IpLimit)")
    public void annotationCut() {}

    @Before("annotationCut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }

        LinkedList<LocalDateTime> localDateTimeLinkedList = ipMap.getOrDefault(ipAddress, new LinkedList<>());
        LocalDateTime localDateTimeNow = LocalDateTime.now();

        boolean isLimitOver = true;

        if (localDateTimeLinkedList.size() < config.getMaxCountRequest()) {
            localDateTimeLinkedList.add(localDateTimeNow);
            ipMap.put(ipAddress, localDateTimeLinkedList);
            isLimitOver = false;
        } else {
            LocalDateTime localDateTimeOld = localDateTimeLinkedList.get(0);
            if (localDateTimeNow.minusMinutes(config.getMinuteExpire())
                    .isAfter(localDateTimeOld)) {
                localDateTimeLinkedList.poll();
                localDateTimeLinkedList.add(localDateTimeNow);
                isLimitOver = false;
            }
        }

        if(isLimitOver) throw new LimitIpException();
    }

}
