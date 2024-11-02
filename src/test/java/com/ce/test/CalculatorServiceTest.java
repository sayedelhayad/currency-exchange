package com.ce.test;

import com.ce.auth.dto.User;
import com.ce.bill.dto.Bill;
import com.ce.bill.dto.ExchangeRateResponse;
import com.ce.bill.dto.Item;
import com.ce.bill.dto.UserType;
import com.ce.bill.service.CalculatorService;
import com.ce.bill.service.ExchangeRateClient;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CalculatorServiceTest {

    @Autowired
    private CalculatorService calculatorService;

    @Autowired
    private Gson gson;

    @MockBean
    private ExchangeRateClient exchangeRateClient;

    private ExchangeRateResponse exchangeRateResponse;


    @BeforeEach
    public void setUp() {
        Map<String, Double> conversion_rates = new HashMap<>();
        conversion_rates.put("AED", 3.67);
        exchangeRateResponse = new ExchangeRateResponse();
        exchangeRateResponse.setConversion_rates(conversion_rates);
    }


    @Test
    public void calculateTest() throws ParseException {
        Bill bill = new Bill();
        Item item1 = new Item();
        item1.setTitle("item1");
        item1.setCategory("fff");
        item1.setTotalPrice(150.5);
        List<Item> items = Arrays.asList(item1);
        bill.setItems(items);
        bill.setTotalAmount(1000);
        bill.setOriginalCurrency("USD");
        bill.setTargetCurrency("AED");

        SimpleDateFormat smp = new SimpleDateFormat("yyyy-MM-dd");
        Date joiningDate = smp.parse("2020-05-01");
        User user = User.builder()
                .type(UserType.EMPLOYEE)
                .joiningDate(joiningDate)
                .build();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("user", gson.toJson(user));


        when(exchangeRateClient.getExchangeRate(any())).thenReturn(exchangeRateResponse);

        double result = calculatorService.calculateBill(bill, request);
        assert(result == 3329.2995);
    }
}
