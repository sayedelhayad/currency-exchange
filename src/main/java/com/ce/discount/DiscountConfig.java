package com.ce.discount;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Getter
public class DiscountConfig {

    @Value("${ce.discount.percent.employee:30}")
    private int employeeDiscountPercent;

    @Value("${ce.discount.percent.affiliate:10}")
    private int affiliateDiscountPercent;

    @Value("${ce.discount.percent.tenure:5}")
    private int tenureDiscountPercent;

    @Value("${ce.discount.percent.tenure-year-threshold:2}")
    private int tenureYearThreshold;

    @Value("${ce.discount.percent.excluded-categories:2}")
    private Set<String> excludedCategories;

    @Value("${ce.discount.amount:5}")
    private int amountDiscount;

}
