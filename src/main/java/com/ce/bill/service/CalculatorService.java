package com.ce.bill.service;


import com.ce.bill.dto.Bill;
import jakarta.servlet.http.HttpServletRequest;

public interface CalculatorService {

    double calculateBill(Bill bill, final HttpServletRequest request);

}
