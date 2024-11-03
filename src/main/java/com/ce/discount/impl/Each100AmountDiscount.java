package com.ce.discount.impl;

import com.ce.discount.Discount;
import com.ce.discount.DiscountConfig;
import com.ce.discount.DiscountData;
import com.ce.discount.DiscountType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@DiscountType(name = "Each100AmountDiscount", priority = 2)
public class Each100AmountDiscount implements Discount {

    @Override
    public double calculate(final DiscountData discountData) {

        DiscountConfig config = discountData.getConfig();
        int hundreds = (int)discountData.getOriginalTotalAmount() / 100;
        return hundreds * config.getAmountDiscount();
    }
}
