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

    @MockBean
    private MockHttpServletRequest request;

    private ExchangeRateResponse exchangeRateResponse;
    private Bill bill;

    @BeforeEach
    public void setUp() {
        request = new MockHttpServletRequest();
        Map<String, Double> conversion_rates = new HashMap<>();
        conversion_rates.put("AED", 2.0);
        exchangeRateResponse = new ExchangeRateResponse();
        exchangeRateResponse.setConversion_rates(conversion_rates);

        bill = new Bill();
        Item item1 = new Item();
        item1.setTitle("item1");
        item1.setCategory("fff");
        item1.setTotalPrice(250);
        Item item2 = new Item();
        item2.setTitle("item2");
        item2.setCategory("ddd");
        item2.setTotalPrice(250);
        List<Item> items = Arrays.asList(item1, item2);
        bill.setItems(items);
        bill.setTotalAmount(500);
        bill.setOriginalCurrency("USD");
        bill.setTargetCurrency("AED");

    }


    @Test
    public void employeeCalculateTest() throws ParseException {

        User employee = User.builder()
                .type(UserType.EMPLOYEE)
                .tenure(2)
                .build();
        request.setAttribute("user", gson.toJson(employee));

        when(exchangeRateClient.getExchangeRate(any())).thenReturn(exchangeRateResponse);

        double result = calculatorService.calculateBill(bill, request);
        assert(result == 650.0);
    }

    @Test
    public void employeeExcludedCategoryCalculateTest() throws ParseException {
        Item item1 = new Item();
        item1.setTitle("item1");
        item1.setCategory("groceries");
        item1.setTotalPrice(250);

        List<Item> items = Arrays.asList(item1, bill.getItems().get(1));
        bill.setItems(items);


        User employee = User.builder()
                .type(UserType.EMPLOYEE)
                .tenure(2)
                .build();
        request.setAttribute("user", gson.toJson(employee));

        when(exchangeRateClient.getExchangeRate(any())).thenReturn(exchangeRateResponse);

        double result = calculatorService.calculateBill(bill, request);
        assert(result == 800.0);
    }

    @Test
    public void affiliateCalculateTest() throws ParseException {

        User affiliate = User.builder()
                .type(UserType.AFFILIATE)
                .tenure(2)
                .build();
        request.setAttribute("user", gson.toJson(affiliate));

        when(exchangeRateClient.getExchangeRate(any())).thenReturn(exchangeRateResponse);

        double result = calculatorService.calculateBill(bill, request);
        assert(result == 850.0);
    }

    @Test
    public void oldCustomerCalculateTest() throws ParseException {

        User customer = User.builder()
                .type(UserType.CUSTOMER)
                .tenure(2)
                .build();
        request.setAttribute("user", gson.toJson(customer));

        when(exchangeRateClient.getExchangeRate(any())).thenReturn(exchangeRateResponse);

        double result = calculatorService.calculateBill(bill, request);
        assert(result == 900.0);
    }

    @Test
    public void newCustomerCalculateTest() {

        User customer = User.builder()
                .type(UserType.CUSTOMER)
                .tenure(1)
                .build();
        request.setAttribute("user", gson.toJson(customer));

        when(exchangeRateClient.getExchangeRate(any())).thenReturn(exchangeRateResponse);

        double result = calculatorService.calculateBill(bill, request);
        assert(result == 950.0);
    }
}
