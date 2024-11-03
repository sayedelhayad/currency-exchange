package com.ce.bill.service.impl;

import com.ce.auth.dto.User;
import com.ce.bill.dto.Bill;
import com.ce.bill.dto.ExchangeRateResponse;
import com.ce.bill.service.CalculatorService;
import com.ce.bill.service.ExchangeRateClient;
import com.ce.discount.Discount;
import com.ce.discount.DiscountConfig;
import com.ce.discount.DiscountData;
import com.ce.discount.DiscountType;
import com.ce.bill.utils.ReflectionUtils;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalculatorServiceImpl implements CalculatorService {

    private final ExchangeRateClient exchangeRateClient;

    private final Gson gson;
    private final DiscountConfig discountConfig;


    @Override
    public double calculateBill(final Bill bill, final HttpServletRequest request) {

        final ExchangeRateResponse exResponse = exchangeRateClient.getExchangeRate(bill.getOriginalCurrency());
        double targetEXRate = exResponse.getConversion_rates().get(bill.getTargetCurrency());
        double targetTotalAmount = bill.getTotalAmount() * targetEXRate;
        bill.setTotalAmount(targetTotalAmount);

        String userString = request.getAttribute("user").toString();
        User user = gson.fromJson(userString, User.class);
        DiscountData discountData = new DiscountData(bill, bill.getTotalAmount(), targetEXRate, user, discountConfig);

        applyDiscounts(discountData);

        return discountData.getBill().getTotalAmount();
    }

    private void applyDiscounts(final DiscountData discountData) {

        try {
            List<Class<?>> discountClasses = ReflectionUtils.loadDiscounts(DiscountType.class);
            for (Class<?> clazz : discountClasses) {
                try {
                    Discount discount = (Discount) clazz.getDeclaredConstructor().newInstance();
                    discount.apply(discountData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
