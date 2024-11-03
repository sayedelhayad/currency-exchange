package com.ce.bill.service.impl;

import com.ce.bill.dto.ExchangeRateResponse;
import com.ce.bill.service.ExchangeRateClient;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.cache.annotation.Cacheable;

@Service
@RequiredArgsConstructor
public class ExchangeRateClientImpl implements ExchangeRateClient {

    @Value("${ce.exchange-rate.url}")
    private String url;

    private final Gson gson;

    @Override
    @Cacheable(value = "exchangeRates",
            key = "#originalCurrency",
            unless = "#result == null || #result.result != 'success'")
    public ExchangeRateResponse getExchangeRate(final String originalCurrency) {

        String fullUrl = url.replace("{original-currency}", originalCurrency);
        RestTemplate restTemplate = new RestTemplate();
        final ExchangeRateResponse response = restTemplate.getForObject(fullUrl, ExchangeRateResponse.class);
        return response;
    }

}
