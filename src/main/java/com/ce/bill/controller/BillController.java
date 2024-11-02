package com.ce.bill.controller;

import com.ce.bill.dto.Bill;
import com.ce.bill.service.CalculatorService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class BillController {

    private final CalculatorService calculatorService;

    @PostMapping(value = "/calculate", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Double> calculateBill(@RequestBody final Bill bill,
                                                final HttpServletRequest request) {

        final double payableAmount = calculatorService.calculateBill(bill, request);
        return ResponseEntity.ok(payableAmount);
    }
}
