package com.ce.discount;

import com.ce.auth.dto.User;
import com.ce.bill.dto.Bill;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DiscountData {

    private Bill bill;

    private double targetEXRate;

    private User user;

    private DiscountConfig config;

}
