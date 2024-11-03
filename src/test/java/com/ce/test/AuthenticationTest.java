package com.ce.test;

import com.ce.auth.config.TokenProvider;
import com.ce.auth.repository.UserRepository;
import com.ce.auth.dto.JwtDto;
import com.ce.auth.dto.SignInDto;
import com.ce.auth.dto.SignUpDto;
import com.ce.bill.dto.Bill;
import com.ce.bill.dto.ExchangeRateResponse;
import com.ce.bill.dto.Item;
import com.ce.bill.dto.UserType;
import com.ce.bill.service.CalculatorService;
import com.ce.bill.service.ExchangeRateClient;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.POST;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthenticationTest {

    @LocalServerPort
    private int port;

    @MockBean
    private ExchangeRateClient exchangeRateClient;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    private static String empToken;

    private ExchangeRateResponse exchangeRateResponse;

    @BeforeEach
    public void setUp() {
        Map<String, Double> conversion_rates = new HashMap<>();
        conversion_rates.put("AED", 2.0);
        exchangeRateResponse = new ExchangeRateResponse();
        exchangeRateResponse.setConversion_rates(conversion_rates);
    }

    @Test
    @DisplayName("Test 1:signup Test")
    @Order(1)
    void signupTest() throws Exception {

        final String url = "http://localhost:" + port + "/api/auth/signup";

        SimpleDateFormat smp = new SimpleDateFormat("yyyy-MM-dd");
        Date joiningDate = smp.parse("2020-05-01");

        SignUpDto SignUpDto = new SignUpDto("empTest", "123", joiningDate, UserType.EMPLOYEE);

        HttpEntity<?> request = new HttpEntity<>(SignUpDto);

        ResponseEntity<String> response = restTemplate.exchange(url, POST, request, String.class);

        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    @DisplayName("Test 2:signin Test")
    @Order(2)
    void signinTest() throws Exception {

        final String url = "http://localhost:" + port + "/api/auth/signin";

        SignInDto signInDto = new SignInDto("empTest", "123");

        HttpEntity<?> request = new HttpEntity<>(signInDto);

        ResponseEntity<JwtDto> response = restTemplate.exchange(url, POST, request, JwtDto.class);
        final JwtDto jwtDto = response.getBody();
        empToken = jwtDto.accessToken();
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertFalse(jwtDto.accessToken().isBlank());
    }


    @Test
    @DisplayName("Test 3:authenticationTokenSuccess Test")
    @Order(3)
    void authenticationTokenSuccess() throws Exception {

        final String url = "http://localhost:" + port + "/api/calculate";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + empToken);

        Bill bill = new Bill();
        bill.setItems(new ArrayList<>());
        bill.setTotalAmount(1000);
        bill.setOriginalCurrency("USD");
        bill.setTargetCurrency("AED");

        when(exchangeRateClient.getExchangeRate(any())).thenReturn(exchangeRateResponse);

        HttpEntity<?> request = new HttpEntity<>(bill, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, POST, request, String.class);

        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    @DisplayName("Test 4:authenticationTokenInvalid Test")
    @Order(4)
    void authenticationTokenInvalid() throws Exception {

        final String url = "http://localhost:" + port + "/api/calculate";

        HttpHeaders headers = new HttpHeaders();
//    headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + empToken);

        Bill bill = new Bill();
        bill.setItems(new ArrayList<>());
        bill.setTotalAmount(1000);
        bill.setOriginalCurrency("USD");
        bill.setTargetCurrency("AED");

        when(exchangeRateClient.getExchangeRate(any())).thenReturn(exchangeRateResponse);

        HttpEntity<?> request = new HttpEntity<>(bill, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, POST, request, String.class);

        assertTrue(response.getStatusCode().is4xxClientError());
    }
}
