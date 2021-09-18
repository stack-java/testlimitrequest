package com.example.testlimitrequest.service;

import com.example.testlimitrequest.config.Config;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class LimitService {

    private final Config config;

    private final ConcurrentHashMap<String, LinkedList<LocalDateTime>> ipMap = new ConcurrentHashMap<>();

    public boolean isLimitOver(String ip){

        LinkedList<LocalDateTime> localDateTimeLinkedList = ipMap.getOrDefault(ip, new LinkedList<>());
        LocalDateTime localDateTimeNow = LocalDateTime.now();

        if (localDateTimeLinkedList.size()<config.getMaxCountRequest()){
            localDateTimeLinkedList.add(localDateTimeNow);
            ipMap.put(ip, localDateTimeLinkedList);
            return false;
        }

        LocalDateTime localDateTimeOld = localDateTimeLinkedList.get(0);
        if(localDateTimeNow.minusMinutes(config.getMinuteExpire())
                .isAfter(localDateTimeOld)){
            localDateTimeLinkedList.poll();
            localDateTimeLinkedList.add(localDateTimeNow);
            return false;
        }

        return true;

    }

}
