package com.jpmc.midascore;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.jpmc.midascore.foundation.Balance;

@Component
public class BalanceQuerier {
    private final RestTemplate restTemplate = new RestTemplate();

    public Balance query(Long userId) {
        String url = "http://localhost:33400/balance?userId=" + userId;
        return restTemplate.getForObject(url, Balance.class);
    }
}