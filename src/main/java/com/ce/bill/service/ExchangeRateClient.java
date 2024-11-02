package com.ce.bill.service;

import com.ce.bill.dto.ExchangeRateResponse;

public interface ExchangeRateClient {

    ExchangeRateResponse getExchangeRate(String originalCurrency);

}
