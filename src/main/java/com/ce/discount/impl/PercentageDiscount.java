package com.ce.discount.impl;

import com.ce.discount.Discount;
import com.ce.discount.DiscountConfig;
import com.ce.discount.DiscountData;
import com.ce.discount.DiscountType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import static com.ce.bill.dto.UserType.*;

@Service
@RequiredArgsConstructor
@DiscountType(name = "PercentageDiscount", priority = 1)
public class PercentageDiscount implements Discount {

    @Override
    public double calculate(final DiscountData discountData) {

        double discountableAmount;
        DiscountConfig config = discountData.getConfig();
        switch (discountData.getUser().getType()) {
            case EMPLOYEE:
                discountableAmount = calculateDiscountableAmount(discountData);
                return (discountableAmount * config.getEmployeeDiscountPercent()) / 100;
            case AFFILIATE:
                discountableAmount = calculateDiscountableAmount(discountData);
                return (discountableAmount * config.getAffiliateDiscountPercent()) / 100;
            case CUSTOMER:
                if (discountData.getUser().getTenure() >= config.getTenureYearThreshold()) {
                    discountableAmount = calculateDiscountableAmount(discountData);
                    return (discountableAmount * config.getTenureDiscountPercent()) / 100;
                }
                break;
        }
        return 0;
    }

    private double calculateDiscountableAmount(final DiscountData discountData) {

        DiscountConfig config = discountData.getConfig();
        double discountableAmount = discountData.getBill().getItems().stream().map((item) -> {
            if (!CollectionUtils.isEmpty(config.getExcludedCategories()) && config.getExcludedCategories().contains(item.getCategory())) {
                return 0.0;
            } else {
                return item.getTotalPrice() * discountData.getTargetEXRate();
            }
        }).reduce(0.0, Double::sum);
        return discountableAmount;
    }

}
