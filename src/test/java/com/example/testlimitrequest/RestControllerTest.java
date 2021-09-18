package com.example.testlimitrequest;

import com.example.testlimitrequest.config.Config;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    Config config;

    private final String URL_IP = "/ip";
    private final String HEADER_IP = "X-FORWARDED-FOR";


    @Test
    public void checkIpTest() {

        Arrays.asList("127.0.0.0", "127.0.0.1", "127.0.0.2", "127.0.0.3")
                .parallelStream().peek(this::parallelTest).findAny();

    }

    private void parallelTest(String ip) {
        try {
            for (int i = 1; i <= config.getMaxCountRequest() + 2; i++) {
                if (i <= config.getMaxCountRequest()) {
                    doRequestTest(ip, URL_IP, HEADER_IP, status().isOk());
                } else {
                    doRequestTest(ip, URL_IP, HEADER_IP, status().is(502));
                }
            }
            // ждем пока откроется возможность для ip сделать запрос
            Thread.sleep(config.getMinuteExpire() * 60 * 1000);
            doRequestTest(ip, URL_IP, HEADER_IP, status().isOk());

        } catch (Exception e) {
        }
    }

    private void doRequestTest(String ip, String url, String headerName,
                           ResultMatcher result) throws Exception {
        mockMvc.perform(get(url)
                        .header(headerName, ip)
                ).andExpect(result)
                .andReturn();
    }

}
