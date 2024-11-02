package com.ce.test;

import com.ce.auth.config.TokenProvider;
import com.ce.bill.controller.BillController;
import com.ce.bill.dto.Bill;
import com.ce.bill.dto.Item;
import com.ce.bill.service.CalculatorService;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = BillController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class BillControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    private CalculatorService calculatorService;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private com.ce.auth.repository.UserRepository userRepository;

    @Test
    public void calculateTest() throws Exception {

        Bill bill = new Bill();
        Item item1 = new Item();
        item1.setTitle("item1");
        item1.setCategory("fff");
        item1.setTotalPrice(150.5);
        List<Item> items = Arrays.asList(item1);
        bill.setItems(items);
        bill.setTotalAmount(1000);
        bill.setOriginalCurrency("USD");
        bill.setTargetCurrency("USD");
        when(calculatorService.calculateBill(any(Bill.class), any(HttpServletRequest.class))).thenReturn(163.73499999999999);

        // Perform the POST request and verify the response
        mockMvc.perform(MockMvcRequestBuilders.post("/api/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(bill)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("163.73499999999999"));
    }

}
